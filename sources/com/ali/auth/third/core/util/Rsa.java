package com.ali.auth.third.core.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws NoSuchAlgorithmException, Exception {
        return KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(Base64.decode(bysKey)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0059 A[SYNTHETIC, Splitter:B:23:0x0059] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0066 A[SYNTHETIC, Splitter:B:30:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String encrypt(java.lang.String r11, java.lang.String r12) {
        /*
            r5 = 0
            r1 = 0
            java.lang.String r10 = "RSA"
            java.security.PublicKey r9 = getPublicKeyFromX509(r10, r12)     // Catch:{ Exception -> 0x0053 }
            java.lang.String r10 = "RSA/ECB/PKCS1Padding"
            javax.crypto.Cipher r3 = javax.crypto.Cipher.getInstance(r10)     // Catch:{ Exception -> 0x0053 }
            r10 = 1
            r3.init(r10, r9)     // Catch:{ Exception -> 0x0053 }
            java.lang.String r10 = "UTF-8"
            byte[] r8 = r11.getBytes(r10)     // Catch:{ Exception -> 0x0053 }
            int r0 = r3.getBlockSize()     // Catch:{ Exception -> 0x0053 }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0053 }
            r2.<init>()     // Catch:{ Exception -> 0x0053 }
            r7 = 0
        L_0x0025:
            int r10 = r8.length     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            if (r7 >= r10) goto L_0x0039
            int r10 = r8.length     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            int r10 = r10 - r7
            if (r10 >= r0) goto L_0x0037
            int r10 = r8.length     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            int r10 = r10 - r7
        L_0x002e:
            byte[] r10 = r3.doFinal(r8, r7, r10)     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            r2.write(r10)     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            int r7 = r7 + r0
            goto L_0x0025
        L_0x0037:
            r10 = r0
            goto L_0x002e
        L_0x0039:
            java.lang.String r6 = new java.lang.String     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            byte[] r10 = r2.toByteArray()     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            java.lang.String r10 = com.ali.auth.third.core.util.Base64.encode(r10)     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            r6.<init>(r10)     // Catch:{ Exception -> 0x0073, all -> 0x0070 }
            if (r2 == 0) goto L_0x0076
            r2.close()     // Catch:{ IOException -> 0x004e }
        L_0x004b:
            r1 = 0
            r5 = r6
        L_0x004d:
            return r5
        L_0x004e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x004b
        L_0x0053:
            r4 = move-exception
        L_0x0054:
            r4.printStackTrace()     // Catch:{ all -> 0x0063 }
            if (r1 == 0) goto L_0x004d
            r1.close()     // Catch:{ IOException -> 0x005e }
        L_0x005c:
            r1 = 0
            goto L_0x004d
        L_0x005e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x005c
        L_0x0063:
            r10 = move-exception
        L_0x0064:
            if (r1 == 0) goto L_0x006a
            r1.close()     // Catch:{ IOException -> 0x006b }
        L_0x0069:
            r1 = 0
        L_0x006a:
            throw r10
        L_0x006b:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0069
        L_0x0070:
            r10 = move-exception
            r1 = r2
            goto L_0x0064
        L_0x0073:
            r4 = move-exception
            r1 = r2
            goto L_0x0054
        L_0x0076:
            r1 = r2
            r5 = r6
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.core.util.Rsa.encrypt(java.lang.String, java.lang.String):java.lang.String");
    }
}
