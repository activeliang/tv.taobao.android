package com.alibaba.analytics.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class SystemUtils {
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0022  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0074 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCpuInfo() {
        /*
            r7 = 0
            r4 = 0
            r1 = 0
            java.io.FileReader r5 = new java.io.FileReader     // Catch:{ Exception -> 0x0078 }
            java.lang.String r8 = "/proc/cpuinfo"
            r5.<init>(r8)     // Catch:{ Exception -> 0x0078 }
            if (r5 == 0) goto L_0x001f
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0033 }
            r8 = 1024(0x400, float:1.435E-42)
            r2.<init>(r5, r8)     // Catch:{ IOException -> 0x0033 }
            java.lang.String r7 = r2.readLine()     // Catch:{ IOException -> 0x007e, Exception -> 0x007a }
            r2.close()     // Catch:{ IOException -> 0x007e, Exception -> 0x007a }
            r5.close()     // Catch:{ IOException -> 0x007e, Exception -> 0x007a }
            r1 = r2
        L_0x001f:
            r4 = r5
        L_0x0020:
            if (r7 == 0) goto L_0x0074
            r8 = 58
            int r8 = r7.indexOf(r8)
            int r0 = r8 + 1
            java.lang.String r7 = r7.substring(r0)
            java.lang.String r8 = r7.trim()
        L_0x0032:
            return r8
        L_0x0033:
            r6 = move-exception
        L_0x0034:
            java.lang.String r8 = "SystemUtils"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0053 }
            r9.<init>()     // Catch:{ Exception -> 0x0053 }
            java.lang.String r10 = "Could not read from file /proc/cpuinfo :"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x0053 }
            java.lang.String r10 = r6.toString()     // Catch:{ Exception -> 0x0053 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Exception -> 0x0053 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x0053 }
            android.util.Log.e(r8, r9)     // Catch:{ Exception -> 0x0053 }
            goto L_0x001f
        L_0x0053:
            r3 = move-exception
            r4 = r5
        L_0x0055:
            java.lang.String r8 = "SystemUtils"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "BaseParameter-Could not open file /proc/cpuinfo :"
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = r3.toString()
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            android.util.Log.e(r8, r9)
            goto L_0x0020
        L_0x0074:
            java.lang.String r8 = ""
            goto L_0x0032
        L_0x0078:
            r3 = move-exception
            goto L_0x0055
        L_0x007a:
            r3 = move-exception
            r1 = r2
            r4 = r5
            goto L_0x0055
        L_0x007e:
            r6 = move-exception
            r1 = r2
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.SystemUtils.getCpuInfo():java.lang.String");
    }

    public static double getSystemFreeSize() {
        StatFs sf = new StatFs(Environment.getRootDirectory().getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            return (((double) sf.getAvailableBytes()) / 1024.0d) / 1024.0d;
        }
        return (((double) sf.getFreeBytes()) / 1024.0d) / 1024.0d;
    }
}
