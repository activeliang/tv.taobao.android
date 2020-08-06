package com.alibaba.analytics.utils;

public class GzipUtils {
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003e A[SYNTHETIC, Splitter:B:26:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0043 A[SYNTHETIC, Splitter:B:29:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0054 A[SYNTHETIC, Splitter:B:37:0x0054] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0059 A[SYNTHETIC, Splitter:B:40:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] gzip(byte[] r7) {
        /*
            if (r7 == 0) goto L_0x0005
            int r6 = r7.length
            if (r6 != 0) goto L_0x0007
        L_0x0005:
            r5 = r7
        L_0x0006:
            return r5
        L_0x0007:
            r5 = 0
            r0 = 0
            r3 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0038 }
            r1.<init>()     // Catch:{ Exception -> 0x0038 }
            java.util.zip.GZIPOutputStream r4 = new java.util.zip.GZIPOutputStream     // Catch:{ Exception -> 0x006e, all -> 0x0067 }
            int r6 = r7.length     // Catch:{ Exception -> 0x006e, all -> 0x0067 }
            r4.<init>(r1, r6)     // Catch:{ Exception -> 0x006e, all -> 0x0067 }
            r4.write(r7)     // Catch:{ Exception -> 0x0071, all -> 0x006a }
            r4.finish()     // Catch:{ Exception -> 0x0071, all -> 0x006a }
            byte[] r5 = r1.toByteArray()     // Catch:{ Exception -> 0x0071, all -> 0x006a }
            if (r4 == 0) goto L_0x0024
            r4.close()     // Catch:{ IOException -> 0x002c }
        L_0x0024:
            if (r1 == 0) goto L_0x0075
            r1.close()     // Catch:{ IOException -> 0x0031 }
            r3 = r4
            r0 = r1
            goto L_0x0006
        L_0x002c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0024
        L_0x0031:
            r2 = move-exception
            r2.printStackTrace()
            r3 = r4
            r0 = r1
            goto L_0x0006
        L_0x0038:
            r2 = move-exception
        L_0x0039:
            r2.printStackTrace()     // Catch:{ all -> 0x0051 }
            if (r3 == 0) goto L_0x0041
            r3.close()     // Catch:{ IOException -> 0x004c }
        L_0x0041:
            if (r0 == 0) goto L_0x0006
            r0.close()     // Catch:{ IOException -> 0x0047 }
            goto L_0x0006
        L_0x0047:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0006
        L_0x004c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0041
        L_0x0051:
            r6 = move-exception
        L_0x0052:
            if (r3 == 0) goto L_0x0057
            r3.close()     // Catch:{ IOException -> 0x005d }
        L_0x0057:
            if (r0 == 0) goto L_0x005c
            r0.close()     // Catch:{ IOException -> 0x0062 }
        L_0x005c:
            throw r6
        L_0x005d:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0057
        L_0x0062:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005c
        L_0x0067:
            r6 = move-exception
            r0 = r1
            goto L_0x0052
        L_0x006a:
            r6 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x0052
        L_0x006e:
            r2 = move-exception
            r0 = r1
            goto L_0x0039
        L_0x0071:
            r2 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x0039
        L_0x0075:
            r3 = r4
            r0 = r1
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.GzipUtils.gzip(byte[]):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x002e A[SYNTHETIC, Splitter:B:18:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0033 A[SYNTHETIC, Splitter:B:21:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0038 A[SYNTHETIC, Splitter:B:24:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x007a A[SYNTHETIC, Splitter:B:53:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x007f A[SYNTHETIC, Splitter:B:56:0x007f] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0084 A[SYNTHETIC, Splitter:B:59:0x0084] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] unGzip(byte[] r12) {
        /*
            r9 = 0
            r0 = 0
            r6 = 0
            r2 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00a3 }
            r1.<init>(r12)     // Catch:{ Exception -> 0x00a3 }
            java.util.zip.GZIPInputStream r7 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x00a5, all -> 0x0097 }
            r7.<init>(r1)     // Catch:{ Exception -> 0x00a5, all -> 0x0097 }
            r10 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r10]     // Catch:{ Exception -> 0x00a8, all -> 0x009a }
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x00a8, all -> 0x009a }
            r3.<init>()     // Catch:{ Exception -> 0x00a8, all -> 0x009a }
        L_0x0017:
            r10 = 0
            int r11 = r4.length     // Catch:{ Exception -> 0x0025, all -> 0x009e }
            int r8 = r7.read(r4, r10, r11)     // Catch:{ Exception -> 0x0025, all -> 0x009e }
            r10 = -1
            if (r8 == r10) goto L_0x003c
            r10 = 0
            r3.write(r4, r10, r8)     // Catch:{ Exception -> 0x0025, all -> 0x009e }
            goto L_0x0017
        L_0x0025:
            r5 = move-exception
            r2 = r3
            r6 = r7
            r0 = r1
        L_0x0029:
            r5.printStackTrace()     // Catch:{ all -> 0x0077 }
            if (r2 == 0) goto L_0x0031
            r2.close()     // Catch:{ Exception -> 0x0068 }
        L_0x0031:
            if (r6 == 0) goto L_0x0036
            r6.close()     // Catch:{ IOException -> 0x006d }
        L_0x0036:
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0072 }
        L_0x003b:
            return r9
        L_0x003c:
            r3.flush()     // Catch:{ Exception -> 0x0025, all -> 0x009e }
            byte[] r9 = r3.toByteArray()     // Catch:{ Exception -> 0x0025, all -> 0x009e }
            if (r3 == 0) goto L_0x0048
            r3.close()     // Catch:{ Exception -> 0x0056 }
        L_0x0048:
            if (r7 == 0) goto L_0x004d
            r7.close()     // Catch:{ IOException -> 0x005b }
        L_0x004d:
            if (r1 == 0) goto L_0x00ad
            r1.close()     // Catch:{ IOException -> 0x0060 }
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        L_0x0056:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0048
        L_0x005b:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x004d
        L_0x0060:
            r5 = move-exception
            r5.printStackTrace()
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        L_0x0068:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0031
        L_0x006d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0036
        L_0x0072:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x003b
        L_0x0077:
            r10 = move-exception
        L_0x0078:
            if (r2 == 0) goto L_0x007d
            r2.close()     // Catch:{ Exception -> 0x0088 }
        L_0x007d:
            if (r6 == 0) goto L_0x0082
            r6.close()     // Catch:{ IOException -> 0x008d }
        L_0x0082:
            if (r0 == 0) goto L_0x0087
            r0.close()     // Catch:{ IOException -> 0x0092 }
        L_0x0087:
            throw r10
        L_0x0088:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x007d
        L_0x008d:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0082
        L_0x0092:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0087
        L_0x0097:
            r10 = move-exception
            r0 = r1
            goto L_0x0078
        L_0x009a:
            r10 = move-exception
            r6 = r7
            r0 = r1
            goto L_0x0078
        L_0x009e:
            r10 = move-exception
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x0078
        L_0x00a3:
            r5 = move-exception
            goto L_0x0029
        L_0x00a5:
            r5 = move-exception
            r0 = r1
            goto L_0x0029
        L_0x00a8:
            r5 = move-exception
            r6 = r7
            r0 = r1
            goto L_0x0029
        L_0x00ad:
            r2 = r3
            r6 = r7
            r0 = r1
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.GzipUtils.unGzip(byte[]):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0033 A[SYNTHETIC, Splitter:B:20:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003c A[SYNTHETIC, Splitter:B:25:0x003c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] gzipAndRc4Bytes(java.lang.String r7) {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r2 = 0
            java.util.zip.GZIPOutputStream r3 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x002d }
            r3.<init>(r0)     // Catch:{ IOException -> 0x002d }
            java.lang.String r5 = "UTF-8"
            byte[] r5 = r7.getBytes(r5)     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r3.write(r5)     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            r3.flush()     // Catch:{ IOException -> 0x0047, all -> 0x0044 }
            if (r3 == 0) goto L_0x004a
            r3.close()     // Catch:{ Exception -> 0x002a }
            r2 = r3
        L_0x001e:
            byte[] r5 = r0.toByteArray()
            byte[] r4 = com.alibaba.analytics.utils.RC4.rc4(r5)
            r0.close()     // Catch:{ Exception -> 0x0042 }
        L_0x0029:
            return r4
        L_0x002a:
            r5 = move-exception
            r2 = r3
            goto L_0x001e
        L_0x002d:
            r1 = move-exception
        L_0x002e:
            r1.printStackTrace()     // Catch:{ all -> 0x0039 }
            if (r2 == 0) goto L_0x001e
            r2.close()     // Catch:{ Exception -> 0x0037 }
            goto L_0x001e
        L_0x0037:
            r5 = move-exception
            goto L_0x001e
        L_0x0039:
            r5 = move-exception
        L_0x003a:
            if (r2 == 0) goto L_0x003f
            r2.close()     // Catch:{ Exception -> 0x0040 }
        L_0x003f:
            throw r5
        L_0x0040:
            r6 = move-exception
            goto L_0x003f
        L_0x0042:
            r5 = move-exception
            goto L_0x0029
        L_0x0044:
            r5 = move-exception
            r2 = r3
            goto L_0x003a
        L_0x0047:
            r1 = move-exception
            r2 = r3
            goto L_0x002e
        L_0x004a:
            r2 = r3
            goto L_0x001e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.utils.GzipUtils.gzipAndRc4Bytes(java.lang.String):byte[]");
    }
}
