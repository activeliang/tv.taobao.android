package com.taobao.orange.sync;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import com.alibaba.fastjson.JSON;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IndexUpdateHandler {
    private static final long CHECK_INDEX_UPD_INTERVAL = 20000;
    static final String TAG = "IndexUpdateHandler";
    private static boolean disableTaobaoClientIndexCheckUpd = true;
    private static volatile long lastIndexUpdTime = 0;
    static final Set<IndexUpdateInfo> mCurIndexUpdInfo = new HashSet();

    public static void checkIndexUpdate(final String appIndexVersion, final String versionIndexVersion) {
        if (!AndroidUtil.isTaobaoPackage(GlobalOrange.context) || !disableTaobaoClientIndexCheckUpd) {
            synchronized (IndexUpdateHandler.class) {
                long startUpdTime = System.currentTimeMillis();
                if (startUpdTime - lastIndexUpdTime <= CHECK_INDEX_UPD_INTERVAL) {
                    OLog.w(TAG, "checkIndexUpdate too frequently, interval should more than 20s", new Object[0]);
                    return;
                }
                lastIndexUpdTime = startUpdTime;
                OLog.i(TAG, "checkIndexUpdate", "appIndexVersion", appIndexVersion, "versionIndexVersion", versionIndexVersion);
                OThreadFactory.execute(new Runnable() {
                    public void run() {
                        IndexUpdateHandler.updateIndex(new AuthRequest<String>((String) null, false, OConstant.REQTYPE_INDEX_UPDATE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put(OConstant.KEY_CLIENTAPPINDEXVERSION, appIndexVersion);
                                params.put(OConstant.KEY_CLIENTVERSIONINDEXVERSION, versionIndexVersion);
                                return params;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String parseResContent(String content) {
                                return content;
                            }
                        }.syncRequest(), true);
                    }
                });
                return;
            }
        }
        OLog.w(TAG, "checkIndexUpdate skip as in com.taobao.taobao package", new Object[0]);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
        if (com.taobao.orange.util.OLog.isPrintLog(2) == false) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0035, code lost:
        com.taobao.orange.util.OLog.i(TAG, "updateIndex", r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004d, code lost:
        if ("https".equalsIgnoreCase(r0.protocol) == false) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004f, code lost:
        r2 = "https";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0052, code lost:
        com.taobao.orange.GlobalOrange.schema = r2;
        com.taobao.orange.ConfigCenter.getInstance().updateIndex(r0);
        r3 = mCurIndexUpdInfo;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005d, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        mCurIndexUpdInfo.remove(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0063, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0078, code lost:
        r2 = "http";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateIndex(java.lang.String r7, boolean r8) {
        /*
            r6 = 0
            com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo r0 = parseIndexUpdInfo(r7, r8)     // Catch:{ Throwable -> 0x0068 }
            if (r0 == 0) goto L_0x002c
            boolean r2 = r0.checkVaild()     // Catch:{ Throwable -> 0x0068 }
            if (r2 == 0) goto L_0x002c
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r3 = mCurIndexUpdInfo     // Catch:{ Throwable -> 0x0068 }
            monitor-enter(r3)     // Catch:{ Throwable -> 0x0068 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r2 = mCurIndexUpdInfo     // Catch:{ all -> 0x0075 }
            boolean r2 = r2.add(r0)     // Catch:{ all -> 0x0075 }
            if (r2 != 0) goto L_0x002d
            r2 = 0
            boolean r2 = com.taobao.orange.util.OLog.isPrintLog(r2)     // Catch:{ all -> 0x0075 }
            if (r2 == 0) goto L_0x002b
            java.lang.String r2 = "IndexUpdateHandler"
            java.lang.String r4 = "updateIndex is ongoing"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0075 }
            com.taobao.orange.util.OLog.v(r2, r4, r5)     // Catch:{ all -> 0x0075 }
        L_0x002b:
            monitor-exit(r3)     // Catch:{ all -> 0x0075 }
        L_0x002c:
            return
        L_0x002d:
            monitor-exit(r3)     // Catch:{ all -> 0x0075 }
            r2 = 2
            boolean r2 = com.taobao.orange.util.OLog.isPrintLog(r2)     // Catch:{ Throwable -> 0x0068 }
            if (r2 == 0) goto L_0x0044
            java.lang.String r2 = "IndexUpdateHandler"
            java.lang.String r3 = "updateIndex"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0068 }
            r5 = 0
            r4[r5] = r0     // Catch:{ Throwable -> 0x0068 }
            com.taobao.orange.util.OLog.i(r2, r3, r4)     // Catch:{ Throwable -> 0x0068 }
        L_0x0044:
            java.lang.String r2 = "https"
            java.lang.String r3 = r0.protocol     // Catch:{ Throwable -> 0x0068 }
            boolean r2 = r2.equalsIgnoreCase(r3)     // Catch:{ Throwable -> 0x0068 }
            if (r2 == 0) goto L_0x0078
            java.lang.String r2 = "https"
        L_0x0052:
            com.taobao.orange.GlobalOrange.schema = r2     // Catch:{ Throwable -> 0x0068 }
            com.taobao.orange.ConfigCenter r2 = com.taobao.orange.ConfigCenter.getInstance()     // Catch:{ Throwable -> 0x0068 }
            r2.updateIndex(r0)     // Catch:{ Throwable -> 0x0068 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r3 = mCurIndexUpdInfo     // Catch:{ Throwable -> 0x0068 }
            monitor-enter(r3)     // Catch:{ Throwable -> 0x0068 }
            java.util.Set<com.taobao.orange.sync.IndexUpdateHandler$IndexUpdateInfo> r2 = mCurIndexUpdInfo     // Catch:{ all -> 0x0065 }
            r2.remove(r0)     // Catch:{ all -> 0x0065 }
            monitor-exit(r3)     // Catch:{ all -> 0x0065 }
            goto L_0x002c
        L_0x0065:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0065 }
            throw r2     // Catch:{ Throwable -> 0x0068 }
        L_0x0068:
            r1 = move-exception
            java.lang.String r2 = "IndexUpdateHandler"
            java.lang.String r3 = "updateIndex"
            java.lang.Object[] r4 = new java.lang.Object[r6]
            com.taobao.orange.util.OLog.e(r2, r3, r1, r4)
            goto L_0x002c
        L_0x0075:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0075 }
            throw r2     // Catch:{ Throwable -> 0x0068 }
        L_0x0078:
            java.lang.String r2 = "http"
            goto L_0x0052
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.sync.IndexUpdateHandler.updateIndex(java.lang.String, boolean):void");
    }

    private static IndexUpdateInfo parseIndexUpdInfo(String info, boolean isJsonFormat) {
        if (TextUtils.isEmpty(info)) {
            return null;
        }
        if (isJsonFormat) {
            return (IndexUpdateInfo) JSON.parseObject(info, IndexUpdateInfo.class);
        }
        String[] values = info.split("&");
        if (values == null) {
            return null;
        }
        IndexUpdateInfo indexUpdInfo = new IndexUpdateInfo();
        for (String v : values) {
            if (v != null) {
                String temp = v.substring(v.indexOf("=") + 1);
                if (v.startsWith(ConnType.PK_CDN)) {
                    indexUpdInfo.cdn = temp.trim();
                } else if (v.startsWith("md5")) {
                    indexUpdInfo.md5 = temp.trim();
                } else if (v.startsWith("resourceId")) {
                    indexUpdInfo.resourceId = temp.trim();
                } else if (v.startsWith("protocol")) {
                    indexUpdInfo.protocol = temp;
                }
            }
        }
        return indexUpdInfo;
    }

    public static class IndexUpdateInfo implements Serializable {
        static final String SYNC_KEY_CDN = "cdn";
        static final String SYNC_KEY_MD5 = "md5";
        static final String SYNC_KEY_PROTOCOL = "protocol";
        static final String SYNC_KEY_RESOURCEID = "resourceId";
        public String cdn;
        public String md5;
        public String protocol;
        public String resourceId;

        public String toString() {
            StringBuilder sb = new StringBuilder("IndexUpdateInfo{");
            sb.append("cdn='").append(this.cdn).append('\'');
            sb.append(", resourceId='").append(this.resourceId).append('\'');
            sb.append(", md5='").append(this.md5).append('\'');
            sb.append(", protocol='").append(this.protocol).append('\'');
            sb.append('}');
            return sb.toString();
        }

        /* access modifiers changed from: package-private */
        public boolean checkVaild() {
            if (!TextUtils.isEmpty(this.cdn) && !TextUtils.isEmpty(this.resourceId) && !TextUtils.isEmpty(this.md5)) {
                return true;
            }
            OLog.w(IndexUpdateHandler.TAG, "lack param", new Object[0]);
            return false;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IndexUpdateInfo that = (IndexUpdateInfo) o;
            if (!this.cdn.equals(that.cdn) || !this.resourceId.equals(that.resourceId)) {
                return false;
            }
            return this.md5.equals(that.md5);
        }

        public int hashCode() {
            return (((this.cdn.hashCode() * 31) + this.resourceId.hashCode()) * 31) + this.md5.hashCode();
        }
    }
}
