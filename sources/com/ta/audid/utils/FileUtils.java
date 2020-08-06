package com.ta.audid.utils;

import android.text.TextUtils;
import java.io.File;

public class FileUtils {
    public static void isDirExist(String path) {
        if (!TextUtils.isEmpty(path)) {
            File outPutFile = new File(path);
            if (!outPutFile.exists()) {
                outPutFile.mkdirs();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0051 A[SYNTHETIC, Splitter:B:27:0x0051] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFile(java.lang.String r9) {
        /*
            r8 = 0
            r2 = 0
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0062, all -> 0x004e }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0062, all -> 0x004e }
            r6.<init>(r9)     // Catch:{ Exception -> 0x0062, all -> 0x004e }
            r3.<init>(r6)     // Catch:{ Exception -> 0x0062, all -> 0x004e }
            r6 = 100
            char[] r5 = new char[r6]     // Catch:{ Exception -> 0x0024, all -> 0x005f }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0024, all -> 0x005f }
            java.lang.String r6 = ""
            r4.<init>(r6)     // Catch:{ Exception -> 0x0024, all -> 0x005f }
        L_0x0018:
            int r0 = r3.read(r5)     // Catch:{ Exception -> 0x0024, all -> 0x005f }
            r6 = -1
            if (r0 == r6) goto L_0x002f
            r6 = 0
            r4.append(r5, r6, r0)     // Catch:{ Exception -> 0x0024, all -> 0x005f }
            goto L_0x0018
        L_0x0024:
            r6 = move-exception
            r2 = r3
        L_0x0026:
            if (r2 == 0) goto L_0x002b
            r2.close()     // Catch:{ Exception -> 0x0044 }
        L_0x002b:
            java.lang.String r6 = ""
        L_0x002e:
            return r6
        L_0x002f:
            java.lang.String r6 = r4.toString()     // Catch:{ Exception -> 0x0024, all -> 0x005f }
            if (r3 == 0) goto L_0x0038
            r3.close()     // Catch:{ Exception -> 0x003a }
        L_0x0038:
            r2 = r3
            goto L_0x002e
        L_0x003a:
            r1 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r7, r1, r8)
            goto L_0x0038
        L_0x0044:
            r1 = move-exception
            java.lang.String r6 = ""
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r6, r1, r7)
            goto L_0x002b
        L_0x004e:
            r6 = move-exception
        L_0x004f:
            if (r2 == 0) goto L_0x0054
            r2.close()     // Catch:{ Exception -> 0x0055 }
        L_0x0054:
            throw r6
        L_0x0055:
            r1 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r7, r1, r8)
            goto L_0x0054
        L_0x005f:
            r6 = move-exception
            r2 = r3
            goto L_0x004f
        L_0x0062:
            r6 = move-exception
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.utils.FileUtils.readFile(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x003e A[SYNTHETIC, Splitter:B:23:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0043 A[SYNTHETIC, Splitter:B:26:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0061 A[SYNTHETIC, Splitter:B:35:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0066 A[SYNTHETIC, Splitter:B:38:0x0066] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFileLine(java.lang.String r9) {
        /*
            r8 = 0
            r3 = 0
            r0 = 0
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ Exception -> 0x0032 }
            r4.<init>(r9)     // Catch:{ Exception -> 0x0032 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0085, all -> 0x007e }
            r1.<init>(r4)     // Catch:{ Exception -> 0x0085, all -> 0x007e }
            java.lang.String r5 = r1.readLine()     // Catch:{ Exception -> 0x0088, all -> 0x0081 }
            if (r1 == 0) goto L_0x0016
            r1.close()     // Catch:{ Exception -> 0x001e }
        L_0x0016:
            if (r4 == 0) goto L_0x001b
            r4.close()     // Catch:{ Exception -> 0x0028 }
        L_0x001b:
            r0 = r1
            r3 = r4
        L_0x001d:
            return r5
        L_0x001e:
            r2 = move-exception
            java.lang.String r6 = ""
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r6, r2, r7)
            goto L_0x0016
        L_0x0028:
            r2 = move-exception
            java.lang.String r6 = ""
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r6, r2, r7)
            goto L_0x001b
        L_0x0032:
            r2 = move-exception
        L_0x0033:
            java.lang.String r5 = ""
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x005e }
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r6)     // Catch:{ all -> 0x005e }
            if (r0 == 0) goto L_0x0041
            r0.close()     // Catch:{ Exception -> 0x004a }
        L_0x0041:
            if (r3 == 0) goto L_0x0046
            r3.close()     // Catch:{ Exception -> 0x0054 }
        L_0x0046:
            java.lang.String r5 = ""
            goto L_0x001d
        L_0x004a:
            r2 = move-exception
            java.lang.String r5 = ""
            java.lang.Object[] r6 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r6)
            goto L_0x0041
        L_0x0054:
            r2 = move-exception
            java.lang.String r5 = ""
            java.lang.Object[] r6 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r6)
            goto L_0x0046
        L_0x005e:
            r5 = move-exception
        L_0x005f:
            if (r0 == 0) goto L_0x0064
            r0.close()     // Catch:{ Exception -> 0x006a }
        L_0x0064:
            if (r3 == 0) goto L_0x0069
            r3.close()     // Catch:{ Exception -> 0x0074 }
        L_0x0069:
            throw r5
        L_0x006a:
            r2 = move-exception
            java.lang.String r6 = ""
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r6, r2, r7)
            goto L_0x0064
        L_0x0074:
            r2 = move-exception
            java.lang.String r6 = ""
            java.lang.Object[] r7 = new java.lang.Object[r8]
            com.ta.audid.utils.UtdidLogger.e(r6, r2, r7)
            goto L_0x0069
        L_0x007e:
            r5 = move-exception
            r3 = r4
            goto L_0x005f
        L_0x0081:
            r5 = move-exception
            r0 = r1
            r3 = r4
            goto L_0x005f
        L_0x0085:
            r2 = move-exception
            r3 = r4
            goto L_0x0033
        L_0x0088:
            r2 = move-exception
            r0 = r1
            r3 = r4
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.utils.FileUtils.readFileLine(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0046 A[SYNTHETIC, Splitter:B:24:0x0046] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x004b A[SYNTHETIC, Splitter:B:27:0x004b] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0067 A[SYNTHETIC, Splitter:B:36:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x006c A[SYNTHETIC, Splitter:B:39:0x006c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveFile(java.lang.String r9, java.lang.String r10) {
        /*
            r6 = 0
            r0 = 0
            r3 = 0
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch:{ Exception -> 0x003a }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x003a }
            r5.<init>(r9)     // Catch:{ Exception -> 0x003a }
            r4.<init>(r5)     // Catch:{ Exception -> 0x003a }
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x008b, all -> 0x0084 }
            r1.<init>(r4)     // Catch:{ Exception -> 0x008b, all -> 0x0084 }
            r1.write(r10)     // Catch:{ Exception -> 0x008e, all -> 0x0087 }
            r1.flush()     // Catch:{ Exception -> 0x008e, all -> 0x0087 }
            r5 = 1
            if (r1 == 0) goto L_0x001e
            r1.close()     // Catch:{ Exception -> 0x0026 }
        L_0x001e:
            if (r4 == 0) goto L_0x0023
            r4.close()     // Catch:{ Exception -> 0x0030 }
        L_0x0023:
            r3 = r4
            r0 = r1
        L_0x0025:
            return r5
        L_0x0026:
            r2 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r7, r2, r8)
            goto L_0x001e
        L_0x0030:
            r2 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r6 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r7, r2, r6)
            goto L_0x0023
        L_0x003a:
            r2 = move-exception
        L_0x003b:
            java.lang.String r5 = ""
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0064 }
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r7)     // Catch:{ all -> 0x0064 }
            if (r0 == 0) goto L_0x0049
            r0.close()     // Catch:{ Exception -> 0x0050 }
        L_0x0049:
            if (r3 == 0) goto L_0x004e
            r3.close()     // Catch:{ Exception -> 0x005a }
        L_0x004e:
            r5 = r6
            goto L_0x0025
        L_0x0050:
            r2 = move-exception
            java.lang.String r5 = ""
            java.lang.Object[] r7 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r7)
            goto L_0x0049
        L_0x005a:
            r2 = move-exception
            java.lang.String r5 = ""
            java.lang.Object[] r7 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r5, r2, r7)
            goto L_0x004e
        L_0x0064:
            r5 = move-exception
        L_0x0065:
            if (r0 == 0) goto L_0x006a
            r0.close()     // Catch:{ Exception -> 0x0070 }
        L_0x006a:
            if (r3 == 0) goto L_0x006f
            r3.close()     // Catch:{ Exception -> 0x007a }
        L_0x006f:
            throw r5
        L_0x0070:
            r2 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r7, r2, r8)
            goto L_0x006a
        L_0x007a:
            r2 = move-exception
            java.lang.String r7 = ""
            java.lang.Object[] r6 = new java.lang.Object[r6]
            com.ta.audid.utils.UtdidLogger.e(r7, r2, r6)
            goto L_0x006f
        L_0x0084:
            r5 = move-exception
            r3 = r4
            goto L_0x0065
        L_0x0087:
            r5 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x0065
        L_0x008b:
            r2 = move-exception
            r3 = r4
            goto L_0x003b
        L_0x008e:
            r2 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.utils.FileUtils.saveFile(java.lang.String, java.lang.String):boolean");
    }
}
