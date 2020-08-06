package com.yunos.tvtaobao.uuid.client;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOUtil {
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String inputStreamToString(java.io.InputStream r8, java.lang.String r9) throws java.io.UnsupportedEncodingException {
        /*
            r6 = 0
            if (r8 != 0) goto L_0x0004
        L_0x0003:
            return r6
        L_0x0004:
            r0 = 4096(0x1000, float:5.74E-42)
            byte[] r3 = new byte[r0]
            r4 = 0
            r2 = -1
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x003a, all -> 0x0032 }
            r5.<init>()     // Catch:{ IOException -> 0x003a, all -> 0x0032 }
        L_0x000f:
            r7 = 0
            int r2 = r8.read(r3, r7, r0)     // Catch:{ IOException -> 0x001c, all -> 0x0037 }
            r7 = -1
            if (r2 == r7) goto L_0x002d
            r7 = 0
            r5.write(r3, r7, r2)     // Catch:{ IOException -> 0x001c, all -> 0x0037 }
            goto L_0x000f
        L_0x001c:
            r7 = move-exception
            r4 = r5
        L_0x001e:
            closeQuietly(r4)
        L_0x0021:
            byte[] r1 = r4.toByteArray()
            if (r1 == 0) goto L_0x0003
            java.lang.String r6 = new java.lang.String
            r6.<init>(r1, r9)
            goto L_0x0003
        L_0x002d:
            closeQuietly(r5)
            r4 = r5
            goto L_0x0021
        L_0x0032:
            r6 = move-exception
        L_0x0033:
            closeQuietly(r4)
            throw r6
        L_0x0037:
            r6 = move-exception
            r4 = r5
            goto L_0x0033
        L_0x003a:
            r7 = move-exception
            goto L_0x001e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.uuid.client.IOUtil.inputStreamToString(java.io.InputStream, java.lang.String):java.lang.String");
    }

    public static InputStream stringToInputStream(String string, String encode) throws UnsupportedEncodingException {
        if (string == null) {
            return null;
        }
        byte[] byteArray = null;
        try {
            byteArray = string.getBytes(encode);
        } catch (Exception e) {
        }
        return new ByteArrayInputStream(byteArray);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
