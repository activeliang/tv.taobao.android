package com.yunos.tv.core.util;

import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import mtopsdk.common.util.SymbolExpUtil;

public class ActivityPathRecorder {
    public static final String INTENTKEY_FIRST = "pr_is_first";
    public static final String INTENTKEY_PATHRECORDER_PREVIOUSACTIVITY = "pr_previous_activity";
    public static final String INTENTKEY_PATHRECORDER_PREVIOUSINTENT = "pr_previous_intent";
    public static final String INTENTKEY_PATHRECORDER_PREVIOUSURI = "pr_previous_uri";
    public static final String INTENTKEY_PATHRECORDER_URI = "pr_input_uri";
    private static final Object initLock = new Object();
    private static ActivityPathRecorder instance;
    private final String TAG = ActivityPathRecorder.class.getSimpleName();
    private SparseIntArray destroyedHashCodes = new SparseIntArray();
    private SparseArray<PathFilter> mFilters = new SparseArray<>();
    private final Object nodeLock = new Object();
    private SparseArray<SparseArray<ActivityNodeInfo>> nodeTable = new SparseArray<>();
    private TimerTask task;
    private Timer timer;

    public interface PathFilter {
        void filterPath(List<ActivityNodeInfo> list);
    }

    public interface PathNode {
        Uri getCurrentUri();

        int getHashCode();

        int getPreviousNodeHash();

        Uri getPreviousNodeUri();

        int getPreviousSecondHashCode();

        int getSecondHashCode();

        boolean isFirstNode();

        boolean isIgnored();

        boolean recordNewIntent();
    }

    public void applyFilter(@NonNull PathFilter filter, @NonNull String filterName) {
        this.mFilters.append(filterName.hashCode(), filter);
    }

    public void removeFilter(@NonNull String filterName) {
        this.mFilters.remove(filterName.hashCode());
    }

    private ActivityPathRecorder() {
        applyFilter(new ShopPathFilter(), "shopFilter");
        applyFilter(new RelativeCommentPathFilter(), "relativeRecommend");
        this.task = new TimerTask() {
            public void run() {
                ActivityPathRecorder.this.gc();
            }
        };
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.task, 15000, 15000);
    }

    public static ActivityPathRecorder getInstance() {
        if (instance == null) {
            synchronized (initLock) {
                if (instance == null) {
                    instance = new ActivityPathRecorder();
                }
            }
        }
        return instance;
    }

    public void onDestroy(PathNode node) {
        tryRemove(node);
    }

    private void tryRemove(PathNode node) {
        ZpLogger.d(this.TAG, "try remove node:" + node);
        ActivityNodeInfo info = getNodeForActivity(node);
        if (info != null) {
            removeInfoNode(info);
        }
    }

    private void removeInfoNode(ActivityNodeInfo info) {
        SparseArray<ActivityNodeInfo> list = this.nodeTable.get(info.activityHashCode);
        if (list != null && list.get(info.secondaryHash) != null) {
            ActivityNodeInfo nodeInfo = list.get(info.secondaryHash);
            if (nodeInfo.hasChildren()) {
                this.destroyedHashCodes.put(nodeInfo.activityHashCode, nodeInfo.activityHashCode);
                return;
            }
            ZpLogger.d(this.TAG, "removing node:" + info.getUri());
            list.remove(nodeInfo.secondaryHash);
            ActivityNodeInfo pre = findPrevNode(info);
            if (pre != null) {
                pre.removeChild(info);
            }
            if (list.size() == 0) {
                synchronized (this.nodeLock) {
                    this.nodeTable.remove(info.activityHashCode);
                }
                return;
            }
            this.destroyedHashCodes.put(info.activityHashCode, info.activityHashCode);
        }
    }

    /* access modifiers changed from: private */
    public void gc() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IllegalThreadStateException("should be called outside main thread");
        }
        ZpLogger.d(this.TAG, "running gc");
        for (int i = this.destroyedHashCodes.size() - 1; i >= 0; i--) {
            int hashCode = this.destroyedHashCodes.valueAt(i);
            SparseArray<ActivityNodeInfo> list = this.nodeTable.get(hashCode);
            if (list == null || list.size() == 0) {
                this.destroyedHashCodes.removeAt(i);
            } else {
                for (int j = list.size() - 1; j >= 0; j--) {
                    ActivityNodeInfo nodeInfo = list.valueAt(j);
                    if (!nodeInfo.hasChildren()) {
                        ZpLogger.d(this.TAG, "gc remove node:" + nodeInfo.getUri() + SymbolExpUtil.SYMBOL_COLON + nodeInfo.getActivityHashCode());
                        list.removeAt(j);
                        ActivityNodeInfo preNode = findPrevNode(nodeInfo);
                        if (preNode != null) {
                            preNode.removeChild(nodeInfo);
                        }
                    }
                }
                if (list.size() == 0) {
                    synchronized (this.nodeLock) {
                        ZpLogger.d(this.TAG, "gc remove list:" + hashCode);
                        this.nodeTable.remove(hashCode);
                    }
                    this.destroyedHashCodes.removeAt(i);
                } else {
                    continue;
                }
            }
        }
        ZpLogger.d(this.TAG, "running gc complete");
        ZpLogger.d(this.TAG, "after gc : nodeTable.size:" + this.nodeTable.size());
    }

    private void addPath(PathNode node) {
        ZpLogger.w(this.TAG, "addPath node " + node);
        ZpLogger.w(this.TAG, "addPath isIgnored " + node.isIgnored());
        if (!node.isIgnored()) {
            ActivityNodeInfo info = new ActivityNodeInfo(node);
            ZpLogger.w(this.TAG, "addPath isFirstNode " + node.isFirstNode());
            if (node.isFirstNode()) {
                info.setPreviousActivityHashCode(-1);
            } else {
                ZpLogger.w(this.TAG, "addPath className : " + node.getClass().getSimpleName());
                ZpLogger.e(this.TAG, "addPath previousActivity : " + node.getPreviousNodeHash() + "  ,prevUri : " + node.getPreviousNodeUri());
                int previousActivity = node.getPreviousNodeHash();
                Uri prevUri = node.getPreviousNodeUri();
                info.setPreviousActivityHashCode(previousActivity);
                info.setPreviousActivityUri(prevUri);
                int unused = info.previousIntentHashCode = node.getPreviousSecondHashCode();
            }
            ActivityNodeInfo pre = findPrevNode(info);
            if (pre != null) {
                pre.addChild(info);
            }
            ZpLogger.w(this.TAG, "addPath previousActivityUri " + info.previousActivityUri);
            SparseArray<ActivityNodeInfo> list = this.nodeTable.get(info.getActivityHashCode());
            ZpLogger.w(this.TAG, "addPath list " + list);
            if (list == null) {
                list = new SparseArray<>();
                synchronized (this.nodeLock) {
                    this.nodeTable.put(info.getActivityHashCode(), list);
                }
            }
            ZpLogger.d(this.TAG, "add node:" + node.getClass().getSimpleName() + node.hashCode() + ",uri " + info.getUri() + " previousActivity:" + info.previousActivityHashCode + " preuri:" + info.previousActivityUri);
            ZpLogger.i(this.TAG, "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            list.put(info.secondaryHash, info);
        }
    }

    public void recordPathNode(PathNode node) {
        addPath(node);
    }

    private ActivityNodeInfo getNodeForActivity(PathNode node) {
        SparseArray<ActivityNodeInfo> list = this.nodeTable.get(node.getHashCode());
        ZpLogger.d(this.TAG, "getNodeForActivity:" + list);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(node.getSecondHashCode());
    }

    private ActivityNodeInfo findPrevNode(ActivityNodeInfo info) {
        SparseArray<ActivityNodeInfo> activityList = this.nodeTable.get(info.previousActivityHashCode);
        if (activityList == null || activityList.size() == 0) {
            return null;
        }
        ZpLogger.i(this.TAG, "findPrev: info " + info.getActivityHashCode() + " uri:" + info.previousActivityUri);
        return activityList.get(info.previousIntentHashCode);
    }

    public List<String> getCurrentPath(PathNode node) {
        ActivityNodeInfo pre;
        List<ActivityNodeInfo> list = new ArrayList<>();
        ActivityNodeInfo info = getNodeForActivity(node);
        if (info == null) {
            return new ArrayList();
        }
        list.add(info);
        while (-1 != info.previousActivityHashCode && (pre = findPrevNode(info)) != null) {
            list.add(pre);
            info = pre;
        }
        for (int i = 0; i < this.mFilters.size(); i++) {
            this.mFilters.valueAt(i).filterPath(list);
        }
        List<String> path = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            ActivityNodeInfo nodeInfo = list.get(j);
            if (nodeInfo.getUri() != null && !nodeInfo.getUri().toString().contains("module=detail")) {
                path.add(nodeInfo.getUri() + "");
            }
        }
        list.clear();
        return path;
    }

    public static final class ActivityNodeInfo {
        /* access modifiers changed from: private */
        public int activityHashCode;
        private SparseArray<ActivityNodeInfo> children;
        /* access modifiers changed from: private */
        public int previousActivityHashCode;
        /* access modifiers changed from: private */
        public Uri previousActivityUri;
        /* access modifiers changed from: private */
        public int previousIntentHashCode;
        /* access modifiers changed from: private */
        public int secondaryHash;
        private Uri uri;

        public boolean hasChildren() {
            return this.children != null && this.children.size() > 0;
        }

        public void addChild(ActivityNodeInfo info) {
            if (info != this && info.previousActivityHashCode == this.activityHashCode) {
                if (this.children == null) {
                    this.children = new SparseArray<>();
                }
                this.children.put(info.secondaryHash, info);
            }
        }

        public void removeChild(ActivityNodeInfo info) {
            if (info.previousActivityHashCode == this.activityHashCode && this.children != null) {
                this.children.remove(info.secondaryHash);
            }
        }

        public Uri getUri() {
            return this.uri;
        }

        /* access modifiers changed from: private */
        public int getActivityHashCode() {
            return this.activityHashCode;
        }

        /* access modifiers changed from: private */
        public void setPreviousActivityUri(Uri previousActivityUri2) {
            this.previousActivityUri = previousActivityUri2;
        }

        private ActivityNodeInfo(@NonNull PathNode node) {
            this.previousIntentHashCode = -1;
            this.previousActivityHashCode = -1;
            this.secondaryHash = -1;
            setActivity(node);
        }

        private void setActivity(@NonNull PathNode node) {
            this.activityHashCode = node.getHashCode();
            this.uri = node.getCurrentUri();
            this.secondaryHash = node.getSecondHashCode();
        }

        /* access modifiers changed from: private */
        public void setPreviousActivityHashCode(int previousActivityHashCode2) {
            this.previousActivityHashCode = previousActivityHashCode2;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ActivityNodeInfo) || hashCode() != o.hashCode()) {
                return false;
            }
            return this.uri.equals(((ActivityNodeInfo) o).getUri());
        }
    }

    private static class ShopPathFilter implements PathFilter {
        private ShopPathFilter() {
        }

        public void filterPath(List<ActivityNodeInfo> originPath) {
            ActivityNodeInfo shopNode = null;
            ZpLogger.e("ShopPathFilter", "original path:" + originPath.size());
            ListIterator<ActivityNodeInfo> iterator = originPath.listIterator();
            while (iterator.hasNext()) {
                ActivityNodeInfo info = iterator.next();
                ZpLogger.e("ShopPathFilter", "original uri:" + info.getUri());
                if (info.getUri() != null) {
                    if (!"detail".equals(info.getUri().getQueryParameter("module"))) {
                        shopNode = null;
                    } else if (iterator.hasNext()) {
                        ActivityNodeInfo info1 = iterator.next();
                        if (info1.getUri() != null) {
                            if (!"shop".equals(info1.getUri().getQueryParameter("module")) || info.previousActivityHashCode != info1.getActivityHashCode()) {
                                shopNode = null;
                            } else if (shopNode == null) {
                                shopNode = info1;
                            } else if (TextUtils.equals(shopNode.getUri().getQueryParameter("shopId"), info1.getUri().getQueryParameter("shopId"))) {
                                ZpLogger.d("ShopPathFilter", "remove current node");
                                iterator.remove();
                                ZpLogger.d("ShopPathFilter", "removed path1:" + originPath.size());
                                ZpLogger.d("ShopPathFilter", "remove previous node");
                                iterator.previous();
                                iterator.remove();
                                ZpLogger.d("ShopPathFilter", "removed path2:" + originPath.size());
                            } else {
                                shopNode = info1;
                            }
                        }
                    }
                }
            }
            ZpLogger.d("ShopPathFilter", "filtered path:" + originPath.size());
        }
    }

    private static class RelativeCommentPathFilter implements PathFilter {
        private RelativeCommentPathFilter() {
        }

        public void filterPath(List<ActivityNodeInfo> originPath) {
            ListIterator<ActivityNodeInfo> iterator = originPath.listIterator();
            while (iterator.hasNext()) {
                ActivityNodeInfo info = iterator.next();
                if (info.getUri() != null && "relative_recomment".equals(info.getUri().getQueryParameter("module"))) {
                    originPath.clear();
                    return;
                }
            }
        }
    }
}
