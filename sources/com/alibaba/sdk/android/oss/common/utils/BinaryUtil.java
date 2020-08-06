package com.alibaba.sdk.android.oss.common.utils;

import android.util.Base64;
import com.alibaba.analytics.core.device.Constants;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BinaryUtil {
    public static String toBase64String(byte[] binaryData) {
        return new String(Base64.encode(binaryData, 0)).trim();
    }

    public static byte[] fromBase64String(String base64String) {
        return Base64.decode(base64String, 0);
    }

    public static byte[] calculateMd5(byte[] binaryData) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(binaryData);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
    }

    public static byte[] calculateMd5(String filePath) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[Constants.MAX_UPLOAD_SIZE];
            FileInputStream is = new FileInputStream(new File(filePath));
            while (true) {
                int len = is.read(buffer);
                if (len != -1) {
                    digest.update(buffer, 0, len);
                } else {
                    is.close();
                    return digest.digest();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
    }

    public static String calculateMd5Str(byte[] binaryData) {
        return getMd5StrFromBytes(calculateMd5(binaryData));
    }

    public static String calculateMd5Str(String filePath) throws IOException {
        return getMd5StrFromBytes(calculateMd5(filePath));
    }

    public static String calculateBase64Md5(byte[] binaryData) {
        return toBase64String(calculateMd5(binaryData));
    }

    public static String calculateBase64Md5(String filePath) throws IOException {
        return toBase64String(calculateMd5(filePath));
    }

    public static String getMd5StrFromBytes(byte[] md5bytes) {
        if (md5bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < md5bytes.length; i++) {
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(md5bytes[i])}));
        }
        return sb.toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x003b A[SYNTHETIC, Splitter:B:26:0x003b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String fileToSHA1(java.lang.String r9) {
        /*
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0048, all -> 0x0038 }
            r4.<init>(r9)     // Catch:{ Exception -> 0x0048, all -> 0x0038 }
            r7 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r7]     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            java.lang.String r7 = "SHA-1"
            java.security.MessageDigest r1 = java.security.MessageDigest.getInstance(r7)     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            r5 = 0
        L_0x0012:
            r7 = -1
            if (r5 == r7) goto L_0x0029
            int r5 = r4.read(r0)     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            if (r5 <= 0) goto L_0x0012
            r7 = 0
            r1.update(r0, r7, r5)     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            goto L_0x0012
        L_0x0020:
            r2 = move-exception
            r3 = r4
        L_0x0022:
            r7 = 0
            if (r3 == 0) goto L_0x0028
            r3.close()     // Catch:{ Exception -> 0x0041 }
        L_0x0028:
            return r7
        L_0x0029:
            byte[] r6 = r1.digest()     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            java.lang.String r7 = convertHashToString(r6)     // Catch:{ Exception -> 0x0020, all -> 0x0045 }
            if (r4 == 0) goto L_0x0036
            r4.close()     // Catch:{ Exception -> 0x003f }
        L_0x0036:
            r3 = r4
            goto L_0x0028
        L_0x0038:
            r7 = move-exception
        L_0x0039:
            if (r3 == 0) goto L_0x003e
            r3.close()     // Catch:{ Exception -> 0x0043 }
        L_0x003e:
            throw r7
        L_0x003f:
            r8 = move-exception
            goto L_0x0036
        L_0x0041:
            r8 = move-exception
            goto L_0x0028
        L_0x0043:
            r8 = move-exception
            goto L_0x003e
        L_0x0045:
            r7 = move-exception
            r3 = r4
            goto L_0x0039
        L_0x0048:
            r2 = move-exception
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.oss.common.utils.BinaryUtil.fileToSHA1(java.lang.String):java.lang.String");
    }

    private static String convertHashToString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal = returnVal + Integer.toString((hashBytes[i] & OnReminderListener.RET_FULL) + 256, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }
}
