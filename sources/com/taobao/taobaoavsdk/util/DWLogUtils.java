package com.taobao.taobaoavsdk.util;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.adapter.ITLogAdapter;

public class DWLogUtils {
    public static final String TAG = "TBDWInstance";

    public static void d(ITLogAdapter logAdapter, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (logAdapter != null) {
                try {
                    logAdapter.tlogD(msg);
                } catch (Throwable th) {
                }
            } else {
                d(msg);
            }
        }
    }

    public static void i(ITLogAdapter logAdapter, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (logAdapter != null) {
                try {
                    logAdapter.tlogI(msg);
                } catch (Throwable th) {
                }
            } else {
                info(msg);
            }
        }
    }

    public static void e(ITLogAdapter logAdapter, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (logAdapter != null) {
                try {
                    logAdapter.tlogE(msg);
                } catch (Throwable th) {
                }
            } else {
                e(msg);
            }
        }
    }

    public static void w(ITLogAdapter logAdapter, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (logAdapter != null) {
                try {
                    logAdapter.tlogW(msg);
                } catch (Throwable th) {
                }
            } else {
                w(msg);
            }
        }
    }

    public static void d(String msg) {
    }

    public static void info(String msg) {
    }

    public static void v(String msg) {
    }

    public static void w(String msg) {
    }

    public static void e(String msg) {
    }

    public static void d(String tag, String msg) {
    }

    public static void info(String tag, String msg) {
    }

    public static void v(String tag, String msg) {
    }

    public static void w(String tag, String msg) {
    }

    public static void e(String tag, String msg) {
        if (msg != null) {
            Log.e(tag, msg);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0039 A[SYNTHETIC, Splitter:B:23:0x0039] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003e A[SYNTHETIC, Splitter:B:26:0x003e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(java.lang.Exception r7) {
        /*
            if (r7 != 0) goto L_0x0006
            java.lang.String r6 = ""
        L_0x0005:
            return r6
        L_0x0006:
            r3 = 0
            r1 = 0
            java.io.StringWriter r4 = new java.io.StringWriter     // Catch:{ all -> 0x0036 }
            r4.<init>()     // Catch:{ all -> 0x0036 }
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ all -> 0x0049 }
            r2.<init>(r4)     // Catch:{ all -> 0x0049 }
            r7.printStackTrace(r2)     // Catch:{ all -> 0x004c }
            r2.flush()     // Catch:{ all -> 0x004c }
            r4.flush()     // Catch:{ all -> 0x004c }
            if (r4 == 0) goto L_0x0020
            r4.close()     // Catch:{ IOException -> 0x002a }
        L_0x0020:
            if (r2 == 0) goto L_0x0025
            r2.close()     // Catch:{ Throwable -> 0x002f }
        L_0x0025:
            java.lang.String r6 = r4.toString()     // Catch:{ Throwable -> 0x002f }
            goto L_0x0005
        L_0x002a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x002f }
            goto L_0x0020
        L_0x002f:
            r5 = move-exception
            r1 = r2
            r3 = r4
        L_0x0032:
            java.lang.String r6 = ""
            goto L_0x0005
        L_0x0036:
            r6 = move-exception
        L_0x0037:
            if (r3 == 0) goto L_0x003c
            r3.close()     // Catch:{ IOException -> 0x0044 }
        L_0x003c:
            if (r1 == 0) goto L_0x0041
            r1.close()     // Catch:{ Throwable -> 0x0042 }
        L_0x0041:
            throw r6     // Catch:{ Throwable -> 0x0042 }
        L_0x0042:
            r5 = move-exception
            goto L_0x0032
        L_0x0044:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0042 }
            goto L_0x003c
        L_0x0049:
            r6 = move-exception
            r3 = r4
            goto L_0x0037
        L_0x004c:
            r6 = move-exception
            r1 = r2
            r3 = r4
            goto L_0x0037
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.taobaoavsdk.util.DWLogUtils.getStackTrace(java.lang.Exception):java.lang.String");
    }
}
