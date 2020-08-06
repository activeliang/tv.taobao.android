package com.alibaba.wireless.security.framework.utils;

public class b {
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0042 A[SYNTHETIC, Splitter:B:30:0x0042] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.io.File r5) {
        /*
            r1 = 0
            if (r5 == 0) goto L_0x0009
            boolean r0 = r5.exists()
            if (r0 != 0) goto L_0x000b
        L_0x0009:
            r0 = r1
        L_0x000a:
            return r0
        L_0x000b:
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x004d, all -> 0x003f }
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Exception -> 0x004d, all -> 0x003f }
            r2.<init>(r5)     // Catch:{ Exception -> 0x004d, all -> 0x003f }
            r0.<init>(r2)     // Catch:{ Exception -> 0x004d, all -> 0x003f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0024, all -> 0x0048 }
            r2.<init>()     // Catch:{ Exception -> 0x0024, all -> 0x0048 }
        L_0x001a:
            java.lang.String r3 = r0.readLine()     // Catch:{ Exception -> 0x0024, all -> 0x0048 }
            if (r3 == 0) goto L_0x002c
            r2.append(r3)     // Catch:{ Exception -> 0x0024, all -> 0x0048 }
            goto L_0x001a
        L_0x0024:
            r2 = move-exception
        L_0x0025:
            if (r0 == 0) goto L_0x0050
            r0.close()     // Catch:{ Exception -> 0x003c }
            r0 = r1
            goto L_0x000a
        L_0x002c:
            r0.close()     // Catch:{ Exception -> 0x0024, all -> 0x0048 }
            r3 = 0
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x004d, all -> 0x003f }
            if (r1 == 0) goto L_0x000a
            r3.close()     // Catch:{ Exception -> 0x003a }
            goto L_0x000a
        L_0x003a:
            r1 = move-exception
            goto L_0x000a
        L_0x003c:
            r0 = move-exception
            r0 = r1
            goto L_0x000a
        L_0x003f:
            r0 = move-exception
        L_0x0040:
            if (r1 == 0) goto L_0x0045
            r1.close()     // Catch:{ Exception -> 0x0046 }
        L_0x0045:
            throw r0
        L_0x0046:
            r1 = move-exception
            goto L_0x0045
        L_0x0048:
            r1 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
            goto L_0x0040
        L_0x004d:
            r0 = move-exception
            r0 = r1
            goto L_0x0025
        L_0x0050:
            r0 = r1
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.utils.b.a(java.io.File):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x005a A[SYNTHETIC, Splitter:B:26:0x005a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.io.File r6, java.lang.String r7) {
        /*
            r1 = 0
            r0 = 0
            if (r6 == 0) goto L_0x0006
            if (r7 != 0) goto L_0x0007
        L_0x0006:
            return r0
        L_0x0007:
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = r6.getAbsolutePath()
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r4 = ".tmp"
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r2 = r2.toString()
            r3.<init>(r2)
            boolean r2 = r3.exists()     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            if (r2 != 0) goto L_0x002d
            r3.createNewFile()     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
        L_0x002d:
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            r5 = 0
            r4.<init>(r3, r5)     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            r2.<init>(r4)     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            r2.write(r7)     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            r2.flush()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            r2.close()     // Catch:{ Exception -> 0x0063, all -> 0x0060 }
            r2 = 0
            boolean r0 = r3.renameTo(r6)     // Catch:{ Exception -> 0x004e, all -> 0x0057 }
            if (r1 == 0) goto L_0x0006
            r2.close()     // Catch:{ Exception -> 0x004c }
            goto L_0x0006
        L_0x004c:
            r1 = move-exception
            goto L_0x0006
        L_0x004e:
            r2 = move-exception
        L_0x004f:
            if (r1 == 0) goto L_0x0006
            r1.close()     // Catch:{ Exception -> 0x0055 }
            goto L_0x0006
        L_0x0055:
            r1 = move-exception
            goto L_0x0006
        L_0x0057:
            r0 = move-exception
        L_0x0058:
            if (r1 == 0) goto L_0x005d
            r1.close()     // Catch:{ Exception -> 0x005e }
        L_0x005d:
            throw r0
        L_0x005e:
            r1 = move-exception
            goto L_0x005d
        L_0x0060:
            r0 = move-exception
            r1 = r2
            goto L_0x0058
        L_0x0063:
            r1 = move-exception
            r1 = r2
            goto L_0x004f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.utils.b.a(java.io.File, java.lang.String):boolean");
    }
}
