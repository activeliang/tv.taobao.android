package com.ut.mini.exposure;

import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.SpSetting;

public class ExposureConfigMgr {
    private static final String EXP_CONFIG_TAG = "autoExposure";
    public static double dimThreshold = 0.5d;
    private static String mConfig = null;
    public static int maxTimeThreshold = 3600000;
    public static boolean notClearTagAfterDisAppear = false;
    public static int timeThreshold = 500;
    public static boolean trackerExposureOpen = true;

    public static void init() {
        updateExposureConfig(SpSetting.get(ClientVariables.getInstance().getContext(), EXP_CONFIG_TAG));
        TrackerManager.getInstance().getThreadHandle().postDelayed(new Runnable() {
            public void run() {
                ExposureConfigMgr.updateExposureConfig();
            }
        }, 15000);
    }

    public static void updateExposureConfig() {
        try {
            updateExposureConfig(AnalyticsMgr.getValue(EXP_CONFIG_TAG));
        } catch (Throwable th) {
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        if (r14.equalsIgnoreCase(mConfig) == false) goto L_0x000c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void updateExposureConfig(java.lang.String r14) {
        /*
            r13 = 0
            r12 = 1
            if (r14 == 0) goto L_0x000c
            java.lang.String r9 = mConfig     // Catch:{ Throwable -> 0x0112 }
            boolean r9 = r14.equalsIgnoreCase(r9)     // Catch:{ Throwable -> 0x0112 }
            if (r9 != 0) goto L_0x0012
        L_0x000c:
            if (r14 != 0) goto L_0x0047
            java.lang.String r9 = mConfig     // Catch:{ Throwable -> 0x0112 }
            if (r9 != 0) goto L_0x0047
        L_0x0012:
            java.lang.String r9 = "ExposureConfigMgr"
            r10 = 6
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.String r11 = "trackerExposureOpen"
            r10[r13] = r11
            boolean r11 = trackerExposureOpen
            java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)
            r10[r12] = r11
            r11 = 2
            java.lang.String r12 = "timeThreshold"
            r10[r11] = r12
            r11 = 3
            int r12 = timeThreshold
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            r10[r11] = r12
            r11 = 4
            java.lang.String r12 = " dimThreshold"
            r10[r11] = r12
            r11 = 5
            double r12 = dimThreshold
            java.lang.Double r12 = java.lang.Double.valueOf(r12)
            r10[r11] = r12
            com.ut.mini.exposure.ExpLogger.d(r9, r10)
            return
        L_0x0047:
            mConfig = r14     // Catch:{ Throwable -> 0x0112 }
            com.alibaba.analytics.core.ClientVariables r9 = com.alibaba.analytics.core.ClientVariables.getInstance()     // Catch:{ Throwable -> 0x0112 }
            android.content.Context r9 = r9.getContext()     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r10 = "autoExposure"
            java.lang.String r11 = mConfig     // Catch:{ Throwable -> 0x0112 }
            com.alibaba.analytics.utils.SpSetting.put(r9, r10, r11)     // Catch:{ Throwable -> 0x0112 }
            java.lang.String r9 = mConfig     // Catch:{ Throwable -> 0x0112 }
            if (r9 == 0) goto L_0x011f
            java.lang.String r9 = mConfig     // Catch:{ Exception -> 0x010c }
            java.lang.Class<java.util.Map> r10 = java.util.Map.class
            java.lang.Object r3 = com.alibaba.fastjson.JSONObject.parseObject((java.lang.String) r9, r10)     // Catch:{ Exception -> 0x010c }
            java.util.Map r3 = (java.util.Map) r3     // Catch:{ Exception -> 0x010c }
            if (r3 == 0) goto L_0x0012
            int r9 = r3.size()     // Catch:{ Exception -> 0x010c }
            if (r9 <= 0) goto L_0x0012
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010c }
            r9.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = ""
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = "turnOn"
            java.lang.Object r10 = r3.get(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r8 = r9.toString()     // Catch:{ Exception -> 0x010c }
            java.lang.String r9 = "1"
            boolean r9 = r8.equals(r9)     // Catch:{ Exception -> 0x010c }
            if (r9 == 0) goto L_0x0115
            r9 = 1
            trackerExposureOpen = r9     // Catch:{ Exception -> 0x010c }
        L_0x0096:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010c }
            r9.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = ""
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = "timeThreshold"
            java.lang.Object r10 = r3.get(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r7 = r9.toString()     // Catch:{ Exception -> 0x010c }
            r6 = -1
            int r6 = java.lang.Integer.parseInt(r7)     // Catch:{ Exception -> 0x012c }
        L_0x00b6:
            if (r6 < 0) goto L_0x00ba
            timeThreshold = r6     // Catch:{ Exception -> 0x010c }
        L_0x00ba:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010c }
            r9.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = ""
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = "areaThreshold"
            java.lang.Object r10 = r3.get(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r2 = r9.toString()     // Catch:{ Exception -> 0x010c }
            r0 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            double r0 = java.lang.Double.parseDouble(r2)     // Catch:{ Exception -> 0x012e }
        L_0x00db:
            r10 = 0
            int r9 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r9 < 0) goto L_0x00e3
            dimThreshold = r0     // Catch:{ Exception -> 0x010c }
        L_0x00e3:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010c }
            r9.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = ""
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r10 = "notClearTag"
            java.lang.Object r10 = r3.get(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x010c }
            java.lang.String r5 = r9.toString()     // Catch:{ Exception -> 0x010c }
            java.lang.String r9 = "1"
            boolean r9 = r5.equals(r9)     // Catch:{ Exception -> 0x010c }
            if (r9 == 0) goto L_0x011a
            r9 = 1
            notClearTagAfterDisAppear = r9     // Catch:{ Exception -> 0x010c }
            goto L_0x0012
        L_0x010c:
            r4 = move-exception
            r4.printStackTrace()     // Catch:{ Throwable -> 0x0112 }
            goto L_0x0012
        L_0x0112:
            r9 = move-exception
            goto L_0x0012
        L_0x0115:
            r9 = 0
            trackerExposureOpen = r9     // Catch:{ Exception -> 0x010c }
            goto L_0x0096
        L_0x011a:
            r9 = 0
            notClearTagAfterDisAppear = r9     // Catch:{ Exception -> 0x010c }
            goto L_0x0012
        L_0x011f:
            r9 = 1
            trackerExposureOpen = r9     // Catch:{ Throwable -> 0x0112 }
            r9 = 500(0x1f4, float:7.0E-43)
            timeThreshold = r9     // Catch:{ Throwable -> 0x0112 }
            r10 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            dimThreshold = r10     // Catch:{ Throwable -> 0x0112 }
            goto L_0x0012
        L_0x012c:
            r9 = move-exception
            goto L_0x00b6
        L_0x012e:
            r9 = move-exception
            goto L_0x00db
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.exposure.ExposureConfigMgr.updateExposureConfig(java.lang.String):void");
    }
}
