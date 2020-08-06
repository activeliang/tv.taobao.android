package com.edge.pcdn;

public class ZipUtil {
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x005e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void unzipSingleFileHereWithFileName(java.lang.String r12, java.lang.String r13) throws java.lang.Exception {
        /*
            java.io.File r7 = new java.io.File
            r7.<init>(r12)
            java.io.File r3 = new java.io.File
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = r7.getParent()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = "/"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r13)
            java.lang.String r10 = r10.toString()
            r3.<init>(r10)
            r8 = 0
            r4 = 0
            java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x0075 }
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0075 }
            r10.<init>(r7)     // Catch:{ Exception -> 0x0075 }
            r9.<init>(r10)     // Catch:{ Exception -> 0x0075 }
            java.util.zip.ZipEntry r6 = r9.getNextEntry()     // Catch:{ Exception -> 0x0077, all -> 0x006e }
            boolean r10 = r6.isDirectory()     // Catch:{ Exception -> 0x0077, all -> 0x006e }
            if (r10 != 0) goto L_0x0063
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0077, all -> 0x006e }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0077, all -> 0x006e }
            r10 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r10]     // Catch:{ Exception -> 0x0052, all -> 0x0071 }
            r2 = -1
        L_0x0046:
            int r2 = r9.read(r0)     // Catch:{ Exception -> 0x0052, all -> 0x0071 }
            r10 = -1
            if (r2 == r10) goto L_0x0062
            r10 = 0
            r5.write(r0, r10, r2)     // Catch:{ Exception -> 0x0052, all -> 0x0071 }
            goto L_0x0046
        L_0x0052:
            r1 = move-exception
            r4 = r5
            r8 = r9
        L_0x0055:
            throw r1     // Catch:{ all -> 0x0056 }
        L_0x0056:
            r10 = move-exception
        L_0x0057:
            if (r4 == 0) goto L_0x005c
            r4.close()
        L_0x005c:
            if (r8 == 0) goto L_0x0061
            r8.close()
        L_0x0061:
            throw r10
        L_0x0062:
            r4 = r5
        L_0x0063:
            if (r4 == 0) goto L_0x0068
            r4.close()
        L_0x0068:
            if (r9 == 0) goto L_0x006d
            r9.close()
        L_0x006d:
            return
        L_0x006e:
            r10 = move-exception
            r8 = r9
            goto L_0x0057
        L_0x0071:
            r10 = move-exception
            r4 = r5
            r8 = r9
            goto L_0x0057
        L_0x0075:
            r1 = move-exception
            goto L_0x0055
        L_0x0077:
            r1 = move-exception
            r8 = r9
            goto L_0x0055
        */
        throw new UnsupportedOperationException("Method not decompiled: com.edge.pcdn.ZipUtil.unzipSingleFileHereWithFileName(java.lang.String, java.lang.String):void");
    }
}
