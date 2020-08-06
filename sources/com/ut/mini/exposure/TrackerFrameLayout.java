package com.ut.mini.exposure;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.fastjson.JSONArray;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.UTTracker;
import com.ut.mini.internal.ExposureViewHandle;
import com.ut.mini.internal.ExposureViewTag;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TrackerFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    private static final float CLICK_LIMIT = 20.0f;
    private static final Object HasExposrueObjectLock = new Object();
    private static final String TAG = "TrackerFrameLayout";
    public static long TIME_INTERVAL = 100;
    public static final int TRIGGER_VIEW_CHANGED = 0;
    public static final int TRIGGER_VIEW_STATUS_CHANGED = 3;
    public static final int TRIGGER_WINDOW_CHANGED = 1;
    private static final int UT_EXPORSURE_MAX_LENGTH = 25600;
    private static final String UT_SCM_TAG = "scm";
    private static final String UT_SPM_TAG = "spm";
    private static final int eventId = 2201;
    private static HashMap<String, Object> mCommonInfo = new HashMap<>();
    private static HashMap<String, Integer> mHasExposrueDataLength = new HashMap<>();
    private static Map<String, ArrayList> mHasExposrueMap = Collections.synchronizedMap(new HashMap());
    private static HashMap<String, HashSet<String>> mHasExposureSet = new HashMap<>();
    /* access modifiers changed from: private */
    public static Map<String, HashSet<String>> mHasExposureViewTagSet = Collections.synchronizedMap(new HashMap());
    private static List<String> mImmediatelyCommitBlockList = new ArrayList();
    private Map<String, ExposureView> currentViews = new ConcurrentHashMap();
    private long lastDispatchDrawSystemTimeMillis = 0;
    private Rect mGlobalVisibleRect = new Rect();
    private float mOriX = 0.0f;
    private float mOriY = 0.0f;
    private Runnable traceTask = new Runnable() {
        public void run() {
            TrackerFrameLayout.this.trace(0, true);
        }
    };
    private long traverseTime;

    static {
        UTPageHitHelper.addPageChangerListener(new PageChangerMonitor());
    }

    public TrackerFrameLayout(Context context) {
        super(context);
        addCommonArgsInfo();
        ExposureConfigMgr.updateExposureConfig();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ExpLogger.enableLog) {
            ExpLogger.d(TAG, "action:", Integer.valueOf(ev.getAction()));
        }
        switch (ev.getAction()) {
            case 0:
                this.mOriX = ev.getX();
                this.mOriY = ev.getY();
                break;
            case 1:
                Handler handler = TrackerManager.getInstance().getThreadHandle();
                if (handler != null) {
                    handler.removeCallbacks(this.traceTask);
                    handler.postDelayed(this.traceTask, 1000);
                    break;
                }
                break;
            case 2:
                if (Math.abs(ev.getX() - this.mOriX) <= CLICK_LIMIT && Math.abs(ev.getY() - this.mOriY) <= CLICK_LIMIT) {
                    ExpLogger.d(TAG, "onInterceptTouchEvent ACTION_MOVE but not in click limit");
                    break;
                } else {
                    long time = System.currentTimeMillis();
                    ExpLogger.d(TAG, " begin");
                    trace(0, false);
                    if (ExpLogger.enableLog) {
                        ExpLogger.d(TAG, "end costTime=" + (System.currentTimeMillis() - time) + "--" + "\n");
                        break;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (ExpLogger.enableLog) {
            ExpLogger.d(TAG, "action:", Integer.valueOf(event.getAction()));
        }
        return super.onTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ExpLogger.d(TAG, "begin");
        long ts = System.currentTimeMillis();
        trace(0, false);
        if (ExpLogger.enableLog) {
            ExpLogger.d(TAG, "end costTime=" + (System.currentTimeMillis() - ts) + "--");
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        ExpLogger.d(TAG, "dispatchDraw");
        long time = System.currentTimeMillis();
        if (time - this.lastDispatchDrawSystemTimeMillis > 1000) {
            this.lastDispatchDrawSystemTimeMillis = time;
            addCommonArgsInfo();
        }
        super.dispatchDraw(canvas);
    }

    public void dispatchWindowFocusChanged(boolean hasFocus) {
        ExpLogger.d(TAG, "begin");
        long ts = System.currentTimeMillis();
        trace(1, false);
        if (ExpLogger.enableLog) {
            ExpLogger.d(TAG, "end" + (System.currentTimeMillis() - ts) + "--");
        }
        super.dispatchWindowFocusChanged(hasFocus);
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void dispatchVisibilityChanged(View changedView, int visibility) {
        if (visibility == 8) {
            ExpLogger.d(TAG, "begin");
            long ts = System.currentTimeMillis();
            trace(1, false);
            if (ExpLogger.enableLog) {
                ExpLogger.d(TAG, "end costTime=" + (System.currentTimeMillis() - ts) + "--");
            }
        } else {
            ExpLogger.d(TAG, "visibility", Integer.valueOf(visibility));
        }
        super.dispatchVisibilityChanged(changedView, visibility);
    }

    @TargetApi(4)
    private void addCommonArgsInfo() {
        if (getContext() != null && (getContext() instanceof Activity)) {
            View decorView = ((Activity) getContext()).getWindow().getDecorView();
            mCommonInfo.clear();
            HashMap<String, String> commonInfoMap = TrackerManager.getInstance().commonInfoMap;
            if (commonInfoMap != null) {
                mCommonInfo.putAll(commonInfoMap);
            }
            HashMap<String, Object> commonInfo = (HashMap) decorView.getTag(ExposureUtils.ut_exprosure_common_info_tag);
            if (commonInfo != null && !commonInfo.isEmpty()) {
                mCommonInfo.putAll(commonInfo);
                ExpLogger.d(TAG, "addCommonArgsInfo mCommonInfo ", commonInfo);
            }
            ExpLogger.d(TAG, "addCommonArgsInfo all mCommonInfo ", commonInfo);
        }
    }

    /* access modifiers changed from: private */
    public void trace(int triggerType, boolean isForceTraverse) {
        try {
            long triggerTime = System.currentTimeMillis();
            if (isForceTraverse || triggerTime - this.traverseTime >= TIME_INTERVAL) {
                ExpLogger.d(TAG, "扫描开始");
                this.traverseTime = triggerTime;
                traverseViewTree(this);
                checkViewsStates(triggerType);
                if (ExpLogger.enableLog) {
                    ExpLogger.d(TAG, "扫描结束，耗时:" + (System.currentTimeMillis() - triggerTime));
                }
            } else if (ExpLogger.enableLog) {
                ExpLogger.d(TAG, "triggerTime interval is too close to " + TIME_INTERVAL + "ms");
            }
        } catch (Throwable e) {
            ExpLogger.e(TAG, e, new Object[0]);
        }
    }

    @TargetApi(4)
    private void traverseViewTree(View view) {
        if (view != null) {
            if (!view.isShown()) {
                ExpLogger.d(TAG, "view invisalbe,return");
            } else if (ExposureUtils.isIngoneExposureView(view)) {
                ExpLogger.d(TAG, "view ingone by user,return. view:", view);
            } else {
                String block = null;
                String viewId = null;
                Map dataMap = null;
                if (ExposureUtils.isExposureViewForWeex(view)) {
                    Context currentActivity = view.getContext();
                    ExposureViewHandle handle = TrackerManager.getInstance().getExposureViewHandle();
                    if (handle != null) {
                        String url = null;
                        if (currentActivity != null && (currentActivity instanceof Activity)) {
                            url = UTPageHitHelper.getInstance().getPageUrl(currentActivity);
                            if (TextUtils.isEmpty(url)) {
                                ExpLogger.w(TAG, "Cannot get Current Page Url", currentActivity);
                            }
                        }
                        ExposureViewTag tag = handle.getExposureViewTag(url, view);
                        if (tag != null) {
                            if (TextUtils.isEmpty(tag.block) || TextUtils.isEmpty(tag.viewId)) {
                                if (tag.notExposure) {
                                    ExposureUtils.clearExposureForWeex(view);
                                    ExpLogger.w(TAG, "clear exposure tag. view", view);
                                }
                                ExpLogger.w(TAG, "block or viewId is valid,plase check input params!");
                            } else {
                                block = tag.block;
                                viewId = tag.viewId;
                            }
                        }
                        ExpLogger.d(TAG, "weex block", block, "viewId", viewId);
                    }
                }
                if (ExposureUtils.isExposureView(view)) {
                    Object data = view.getTag(ExposureUtils.ut_exprosure_tag);
                    if (data != null && (data instanceof Map)) {
                        dataMap = (Map) data;
                        block = (String) dataMap.get("UT_EXPROSURE_BLOCK");
                        viewId = (String) dataMap.get("UT_EXPROSURE_VIEWID");
                    }
                    ExpLogger.d(TAG, "native block", block, "viewId", viewId);
                }
                if (!TextUtils.isEmpty(block) && !TextUtils.isEmpty(viewId)) {
                    HashSet<String> set = mHasExposureViewTagSet.get(block);
                    if (set == null) {
                        set = new HashSet<>();
                    }
                    set.add(viewId);
                    mHasExposureViewTagSet.put(block, set);
                    ExposureView ev = this.currentViews.get(String.valueOf(view.hashCode()));
                    if (ev == null) {
                        for (ExposureView exposureView : this.currentViews.values()) {
                            if (viewId.equalsIgnoreCase(exposureView.tag)) {
                                ExpLogger.d(TAG, "this viewId has existed current view:", view, "oldView:", exposureView.view, "viewId", viewId);
                                return;
                            }
                        }
                    } else if (!viewId.equalsIgnoreCase(ev.tag) || ev.isSatisfyTimeRequired()) {
                        ExpLogger.d(TAG, "this view status has change or time > timeThreshold, block", block, " new viewId", viewId, "old viewId", ev.tag);
                        checkViewState(3, ev);
                    } else {
                        ExpLogger.d(TAG, "this view has existed block", block, "viewId", viewId);
                        return;
                    }
                    if (isExposured(block, viewId)) {
                        ExpLogger.d(TAG, "this view has exposured block", block, "viewId", viewId);
                        return;
                    }
                    double size = viewSize(view);
                    if (size >= ExposureConfigMgr.dimThreshold) {
                        long time = System.currentTimeMillis();
                        ExposureView eView = new ExposureView(view);
                        eView.beginTime = time;
                        eView.tag = viewId;
                        eView.block = block;
                        eView.viewData = dataMap;
                        eView.lastCalTime = time;
                        eView.area = size;
                        this.currentViews.put(String.valueOf(view.hashCode()), eView);
                        ExpLogger.d(TAG, "找到元素", viewId);
                    } else {
                        ExpLogger.d(TAG, "找到元素,但不满足曝光条件", viewId);
                    }
                }
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    int childCount = group.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        traverseViewTree(group.getChildAt(i));
                    }
                }
            }
        }
    }

    private void checkViewsStates(int triggerType) {
        if (this.currentViews != null && this.currentViews.size() > 0) {
            for (Map.Entry<String, ExposureView> entry : this.currentViews.entrySet()) {
                checkViewState(triggerType, this.currentViews.get(String.valueOf(((ExposureView) entry.getValue()).view.hashCode())));
            }
        }
    }

    private void checkViewState(int triggerType, ExposureView exposureView) {
        if (!isVisableToUser(exposureView.view)) {
            switch (exposureView.lastState) {
                case 1:
                    exposureView.lastState = 2;
                    exposureView.endTime = System.currentTimeMillis();
                    break;
            }
        } else {
            switch (exposureView.lastState) {
                case 0:
                    exposureView.lastState = 1;
                    exposureView.beginTime = System.currentTimeMillis();
                    break;
                case 1:
                    if (triggerType == 1 || triggerType == 3) {
                        exposureView.lastState = 2;
                        exposureView.endTime = System.currentTimeMillis();
                        break;
                    }
                case 2:
                    exposureView.lastState = 1;
                    exposureView.beginTime = System.currentTimeMillis();
                    break;
            }
        }
        if (exposureView.isSatisfyTimeRequired()) {
            addToCommit(exposureView);
            this.currentViews.remove(String.valueOf(exposureView.view.hashCode()));
        } else if (exposureView.lastState == 2) {
            this.currentViews.remove(String.valueOf(exposureView.view.hashCode()));
            ExpLogger.d(TAG, "时间不满足，元素", exposureView.tag);
        }
    }

    private boolean isVisableToUser(View view) {
        return viewSize(view) >= ExposureConfigMgr.dimThreshold;
    }

    private double viewSize(View view) {
        int size = view.getWidth() * view.getHeight();
        if (!view.getGlobalVisibleRect(this.mGlobalVisibleRect) || size <= 0) {
            return ClientTraceData.b.f47a;
        }
        return (((double) (this.mGlobalVisibleRect.width() * this.mGlobalVisibleRect.height())) * 1.0d) / ((double) size);
    }

    private boolean isExposured(String block, String viewId) {
        Set set = mHasExposureSet.get(block);
        if (set == null) {
            return false;
        }
        return set.contains(viewId);
    }

    private void setExposuredTag(String block, String viewId) {
        HashSet<String> set = mHasExposureSet.get(block);
        if (set == null) {
            set = new HashSet<>();
            mHasExposureSet.put(block, set);
        }
        set.add(viewId);
    }

    private void addToCommit(ExposureView ev) {
        String block = ev.block;
        String viewId = ev.tag;
        setExposuredTag(block, viewId);
        Map viewData = ev.viewData;
        Map<String, String> args = new HashMap<>();
        ExposureViewHandle handler = TrackerManager.getInstance().getExposureViewHandle();
        if (handler != null) {
            String url = null;
            Context currentActivity = ev.view.getContext();
            if (currentActivity != null) {
                url = UTPageHitHelper.getInstance().getPageUrl(currentActivity);
            }
            Map<String, String> properties = handler.getExposureViewProperties(url, ev.view);
            if (properties != null) {
                args.putAll(properties);
            }
        }
        if (!(viewData == null || viewData.get("UT_EXPROSURE_ARGS") == null)) {
            Map<String, String> argstemp = (Map) viewData.get("UT_EXPROSURE_ARGS");
            if (argstemp.size() > 0) {
                args.putAll(argstemp);
            }
        }
        String spm = null;
        String scm = null;
        if (args != null) {
            spm = args.remove("spm");
            scm = args.remove("scm");
        }
        synchronized (HasExposrueObjectLock) {
            ArrayList<ExposureEntity> entitys = mHasExposrueMap.get(block);
            if (entitys == null) {
                entitys = new ArrayList<>();
                mHasExposrueMap.put(block, entitys);
            }
            ExposureEntity entity = new ExposureEntity(spm, scm, args, System.currentTimeMillis() - ev.beginTime, ev.area, viewId);
            entitys.add(entity);
            Integer lengthInteger = mHasExposrueDataLength.get(block);
            if (lengthInteger == null) {
                lengthInteger = 0;
            }
            Integer lengthInteger2 = Integer.valueOf(lengthInteger.intValue() + entity.length());
            mHasExposrueDataLength.put(block, lengthInteger2);
            if (lengthInteger2.intValue() > UT_EXPORSURE_MAX_LENGTH) {
                commitToUT(block, mCommonInfo);
            } else if (mImmediatelyCommitBlockList.contains(block)) {
                commitToUT(block, mCommonInfo);
            }
        }
        ExpLogger.d(TAG, "提交元素viewId ", ev.tag, "block", block, "spm", spm, "scm", scm, "args", args);
    }

    private static void commitToUT(String block, HashMap<String, Object> commonInfo) {
        ExpLogger.d();
        ArrayList<ExposureEntity> es = mHasExposrueMap.remove(block);
        Map<String, String> outPutArgs = new HashMap<>();
        if (commonInfo != null && commonInfo.size() > 0) {
            outPutArgs.putAll(MapUtils.convertObjectMapToStringMap(commonInfo));
        }
        outPutArgs.put("expdata", getExpData(es));
        UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(UTPageHitHelper.getInstance().getCurrentPageName(), eventId, block, (String) null, (String) null, outPutArgs).build());
        mHasExposrueDataLength.remove(block);
    }

    private static String getExpData(ArrayList<ExposureEntity> es) {
        JSONArray js = new JSONArray();
        js.addAll(es);
        return js.toJSONString();
    }

    static class ExposureEntity implements Serializable {
        public double area;
        public long duration = 0;
        public Map<String, String> exargs;
        public String scm;
        public String spm;
        public String viewid;

        public ExposureEntity(String spm2, String scm2, Map exargs2, long duration2, double area2, String viewId) {
            this.spm = spm2;
            this.scm = scm2;
            this.exargs = exargs2;
            this.duration = duration2;
            this.area = area2;
            this.viewid = viewId;
        }

        public int length() {
            int length = 0;
            if (this.spm != null) {
                length = 0 + this.spm.length() + 8;
            }
            if (this.scm != null) {
                length += this.scm.length() + 8;
            }
            if (this.exargs != null) {
                for (String key : this.exargs.keySet()) {
                    if (key != null) {
                        length += key.length();
                    }
                    Object value = this.exargs.get(key);
                    if (value != null) {
                        length += value.toString().length();
                    }
                    length += 5;
                }
            }
            if (this.viewid != null) {
                length += this.viewid.length() + 11;
            }
            return length + 50;
        }
    }

    public void onPageDisAppear() {
        Handler handler = TrackerManager.getInstance().getThreadHandle();
        if (handler != null) {
            handler.removeCallbacks(this.traceTask);
        }
        trace(1, true);
        commitExposureData();
        try {
            Object[] ks = mHasExposureViewTagSet.keySet().toArray();
            if (ks.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Object obj : ks) {
                    sb.append(mHasExposureViewTagSet.get(obj)).append(",");
                }
                UTHitBuilders.UTCustomHitBuilder builder = new UTHitBuilders.UTCustomHitBuilder("ut_exposure_test");
                builder.setProperty("viewids", sb.toString().replaceAll("]", "").replaceAll("\\[", ""));
                UTTracker tracker = UTAnalytics.getInstance().getDefaultTracker();
                if (tracker != null) {
                    tracker.send(builder.build());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mHasExposureViewTagSet.clear();
        }
        mImmediatelyCommitBlockList.clear();
        this.currentViews.clear();
        if (!ExposureConfigMgr.notClearTagAfterDisAppear) {
            mHasExposureSet.clear();
        }
    }

    static class PageChangerMonitor implements UTPageHitHelper.PageChangeListener {
        PageChangerMonitor() {
        }

        public void onPageAppear(Object pageObject) {
            TrackerFrameLayout.mHasExposureViewTagSet.clear();
            if (pageObject != null && (pageObject instanceof Activity)) {
                View contentView = null;
                try {
                    contentView = ((Activity) pageObject).findViewById(16908290);
                } catch (Exception e) {
                    ExpLogger.e(TrackerFrameLayout.TAG, e, new Object[0]);
                }
                if (contentView == null || !(contentView instanceof ViewGroup)) {
                    ExpLogger.w(TrackerFrameLayout.TAG, "contentView", contentView);
                    return;
                }
                View v = ((ViewGroup) contentView).getChildAt(0);
                if (v == null || !(v instanceof TrackerFrameLayout)) {
                    ExpLogger.w(TrackerFrameLayout.TAG, "cannot found the trace view", v);
                    return;
                }
                ((TrackerFrameLayout) v).trace(1, true);
            }
        }

        public void onPageDisAppear(Object pageObject) {
            if (pageObject != null && (pageObject instanceof Activity)) {
                View contentView = null;
                try {
                    contentView = ((Activity) pageObject).findViewById(16908290);
                } catch (Exception e) {
                    ExpLogger.e(TrackerFrameLayout.TAG, e, new Object[0]);
                }
                if (contentView == null || !(contentView instanceof ViewGroup)) {
                    ExpLogger.w(TrackerFrameLayout.TAG, "contentView", contentView);
                    return;
                }
                View v = ((ViewGroup) contentView).getChildAt(0);
                if (v == null || !(v instanceof TrackerFrameLayout)) {
                    ExpLogger.w(TrackerFrameLayout.TAG, "cannot found the trace view ", v);
                    return;
                }
                ((TrackerFrameLayout) v).onPageDisAppear();
            }
        }
    }

    public static void refreshExposureData() {
        mHasExposureSet.clear();
        mHasExposureViewTagSet.clear();
    }

    public static void refreshExposureData(String block) {
        ExpLogger.d(TAG, "[refreshExposureData]block", block);
        if (!TextUtils.isEmpty(block)) {
            mHasExposureSet.remove(block);
        }
    }

    public static void refreshExposureDataByViewId(String block, String viewId) {
        Set set;
        if (!TextUtils.isEmpty(block) && !TextUtils.isEmpty(viewId) && (set = mHasExposureSet.get(block)) != null) {
            set.remove(viewId);
        }
    }

    public static void commitExposureData() {
        synchronized (HasExposrueObjectLock) {
            Object[] keys = null;
            try {
                keys = mHasExposrueMap.keySet().toArray();
            } catch (Throwable th) {
            }
            if (keys != null) {
                if (keys.length > 0) {
                    for (int i = 0; i < keys.length; i++) {
                        commitToUT(keys[i] + "", mCommonInfo);
                    }
                }
            }
            mHasExposrueMap.clear();
        }
    }

    public static void setCommitImmediatelyExposureBlock(String block) {
        mImmediatelyCommitBlockList.add(block);
    }
}
