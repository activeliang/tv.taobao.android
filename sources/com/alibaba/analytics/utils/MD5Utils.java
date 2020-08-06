package com.alibaba.analytics.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 240) >>> 4]);
            sb.append(hexChar[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String getMd5Hex(byte[] source) {
        byte[] result = getMd5(source);
        if (result != null) {
            return toHexString(result);
        }
        return "0000000000000000";
    }

    private static byte[] getMd5(byte[] source) {
        if (source != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(source);
                return md.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
