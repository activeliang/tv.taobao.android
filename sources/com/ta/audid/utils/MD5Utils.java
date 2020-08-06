package com.ta.audid.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String HMAC_MD5_PK = "QrMgt8GGYI6T52ZY5AnhtxkLzb8egpFn";
    private static final char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getHmacMd5Hex(String data) {
        return getHmacMd5Hex(getHmacMd5Key(), data);
    }

    private static String getHmacMd5Hex(String key, String data) {
        try {
            byte[] result = getHmacSHA256(key.getBytes(), data.getBytes());
            if (result != null) {
                return toHexString(result);
            }
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        return "0000000000000000";
    }

    private static byte[] md5(byte[] source) {
        if (source != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(source);
                return md.digest();
            } catch (Exception e) {
                UtdidLogger.e("", e, new Object[0]);
            }
        }
        return null;
    }

    private static String toHexString(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            sb.append(hexChar[(buf[i] & 240) >>> 4]);
            sb.append(hexChar[buf[i] & 15]);
        }
        return sb.toString();
    }

    private static byte[] getHmacSHA256(byte[] key, byte[] data) throws NoSuchAlgorithmException {
        byte[] ipad = new byte[64];
        byte[] opad = new byte[64];
        for (int i = 0; i < 64; i++) {
            ipad[i] = 54;
            opad[i] = 92;
        }
        byte[] actualKey = key;
        byte[] keyArr = new byte[64];
        if (key.length > 64) {
            actualKey = md5(key);
        }
        for (int i2 = 0; i2 < actualKey.length; i2++) {
            keyArr[i2] = actualKey[i2];
        }
        if (actualKey.length < 64) {
            for (int i3 = actualKey.length; i3 < keyArr.length; i3++) {
                keyArr[i3] = 0;
            }
        }
        byte[] kIpadXorResult = new byte[64];
        for (int i4 = 0; i4 < 64; i4++) {
            kIpadXorResult[i4] = (byte) (keyArr[i4] ^ ipad[i4]);
        }
        byte[] firstAppendResult = new byte[(kIpadXorResult.length + data.length)];
        for (int i5 = 0; i5 < kIpadXorResult.length; i5++) {
            firstAppendResult[i5] = kIpadXorResult[i5];
        }
        for (int i6 = 0; i6 < data.length; i6++) {
            firstAppendResult[keyArr.length + i6] = data[i6];
        }
        byte[] firstHashResult = md5(firstAppendResult);
        byte[] kOpadXorResult = new byte[64];
        for (int i7 = 0; i7 < 64; i7++) {
            kOpadXorResult[i7] = (byte) (keyArr[i7] ^ opad[i7]);
        }
        byte[] secondAppendResult = new byte[(kOpadXorResult.length + firstHashResult.length)];
        for (int i8 = 0; i8 < kOpadXorResult.length; i8++) {
            secondAppendResult[i8] = kOpadXorResult[i8];
        }
        for (int i9 = 0; i9 < firstHashResult.length; i9++) {
            secondAppendResult[keyArr.length + i9] = firstHashResult[i9];
        }
        return md5(secondAppendResult);
    }

    private static String getHmacMd5Key() {
        byte[] state = HMAC_MD5_PK.getBytes();
        try {
            int len = HMAC_MD5_PK.length();
            for (byte counter = 0; counter < len; counter = (byte) (counter + 1)) {
                state[counter] = (byte) (state[counter] + counter);
            }
            return toHexString(state);
        } catch (Exception e) {
            return null;
        }
    }
}
