package com.yunos.tvtaobao.biz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Util {
    private static final String hashType = "MD5";
    private static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5(File file) throws Exception {
        return getHash(new FileInputStream(file));
    }

    private static String getHash(InputStream in) throws Exception {
        byte[] buffer = new byte[32768];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        while (true) {
            int numRead = in.read(buffer);
            if (numRead > 0) {
                md5.update(buffer, 0, numRead);
            } else {
                in.close();
                return toHexString(md5.digest());
            }
        }
    }

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 240) >>> 4]);
            sb.append(hexChar[b[i] & 15]);
        }
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0026 A[Catch:{ all -> 0x0092 }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002b A[SYNTHETIC, Splitter:B:16:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0061 A[Catch:{ all -> 0x0092 }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0066 A[SYNTHETIC, Splitter:B:39:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x007d A[Catch:{ all -> 0x0092 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0082 A[SYNTHETIC, Splitter:B:52:0x0082] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0095 A[SYNTHETIC, Splitter:B:61:0x0095] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:11:0x0020=Splitter:B:11:0x0020, B:47:0x0077=Splitter:B:47:0x0077, B:34:0x005b=Splitter:B:34:0x005b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileMD5(java.io.File r9) {
        /*
            r7 = 0
            r3 = 0
            java.lang.String r6 = "MD5"
            java.security.MessageDigest r0 = java.security.MessageDigest.getInstance(r6)     // Catch:{ FileNotFoundException -> 0x00ad, IOException -> 0x005a, NoSuchAlgorithmException -> 0x0076 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x00ad, IOException -> 0x005a, NoSuchAlgorithmException -> 0x0076 }
            r4.<init>(r9)     // Catch:{ FileNotFoundException -> 0x00ad, IOException -> 0x005a, NoSuchAlgorithmException -> 0x0076 }
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r6]     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
        L_0x0012:
            int r5 = r4.read(r1)     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            r6 = -1
            if (r5 == r6) goto L_0x0030
            r6 = 0
            r0.update(r1, r6, r5)     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            goto L_0x0012
        L_0x001e:
            r2 = move-exception
            r3 = r4
        L_0x0020:
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0092 }
            if (r6 == 0) goto L_0x0029
            r2.printStackTrace()     // Catch:{ all -> 0x0092 }
        L_0x0029:
            if (r3 == 0) goto L_0x002e
            r3.close()     // Catch:{ IOException -> 0x004f }
        L_0x002e:
            r6 = r7
        L_0x002f:
            return r6
        L_0x0030:
            java.lang.String r6 = new java.lang.String     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            byte[] r8 = r0.digest()     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            char[] r8 = org.apache.commons.codec.binary.Hex.encodeHex((byte[]) r8)     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            r6.<init>(r8)     // Catch:{ FileNotFoundException -> 0x001e, IOException -> 0x00aa, NoSuchAlgorithmException -> 0x00a7, all -> 0x00a4 }
            if (r4 == 0) goto L_0x0042
            r4.close()     // Catch:{ IOException -> 0x0044 }
        L_0x0042:
            r3 = r4
            goto L_0x002f
        L_0x0044:
            r2 = move-exception
            boolean r7 = com.yunos.tv.core.config.Config.isDebug()
            if (r7 == 0) goto L_0x0042
            r2.printStackTrace()
            goto L_0x0042
        L_0x004f:
            r2 = move-exception
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()
            if (r6 == 0) goto L_0x002e
            r2.printStackTrace()
            goto L_0x002e
        L_0x005a:
            r2 = move-exception
        L_0x005b:
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0092 }
            if (r6 == 0) goto L_0x0064
            r2.printStackTrace()     // Catch:{ all -> 0x0092 }
        L_0x0064:
            if (r3 == 0) goto L_0x0069
            r3.close()     // Catch:{ IOException -> 0x006b }
        L_0x0069:
            r6 = r7
            goto L_0x002f
        L_0x006b:
            r2 = move-exception
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()
            if (r6 == 0) goto L_0x0069
            r2.printStackTrace()
            goto L_0x0069
        L_0x0076:
            r2 = move-exception
        L_0x0077:
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()     // Catch:{ all -> 0x0092 }
            if (r6 == 0) goto L_0x0080
            r2.printStackTrace()     // Catch:{ all -> 0x0092 }
        L_0x0080:
            if (r3 == 0) goto L_0x0085
            r3.close()     // Catch:{ IOException -> 0x0087 }
        L_0x0085:
            r6 = r7
            goto L_0x002f
        L_0x0087:
            r2 = move-exception
            boolean r6 = com.yunos.tv.core.config.Config.isDebug()
            if (r6 == 0) goto L_0x0085
            r2.printStackTrace()
            goto L_0x0085
        L_0x0092:
            r6 = move-exception
        L_0x0093:
            if (r3 == 0) goto L_0x0098
            r3.close()     // Catch:{ IOException -> 0x0099 }
        L_0x0098:
            throw r6
        L_0x0099:
            r2 = move-exception
            boolean r7 = com.yunos.tv.core.config.Config.isDebug()
            if (r7 == 0) goto L_0x0098
            r2.printStackTrace()
            goto L_0x0098
        L_0x00a4:
            r6 = move-exception
            r3 = r4
            goto L_0x0093
        L_0x00a7:
            r2 = move-exception
            r3 = r4
            goto L_0x0077
        L_0x00aa:
            r2 = move-exception
            r3 = r4
            goto L_0x005b
        L_0x00ad:
            r2 = move-exception
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.util.MD5Util.getFileMD5(java.io.File):java.lang.String");
    }
}
