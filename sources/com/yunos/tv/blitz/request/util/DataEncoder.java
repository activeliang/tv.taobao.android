package com.yunos.tv.blitz.request.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class DataEncoder {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String HTTP_PARAMS_ENCODING = "UTF-8";

    public static byte[] encode2MD5(byte[] source) {
        byte[] result = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            return md.digest();
        } catch (Exception e) {
            return result;
        }
    }

    private static char[] encode2Hex(byte[] data) {
        int len = data.length;
        char[] out = new char[(len << 1)];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int j2 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = DIGITS[data[i] & 15];
        }
        return out;
    }

    public static String encode(String source) {
        if (source == null) {
            return null;
        }
        byte[] bytes = null;
        try {
            bytes = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        if (bytes != null) {
            return String.valueOf(encode2Hex(encode2MD5(bytes)));
        }
        return null;
    }

    public static String encodeGBK(String source) {
        if (source == null) {
            return null;
        }
        try {
            return String.valueOf(encode2Hex(encode2MD5(source.getBytes("GBK"))));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String md5Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return String.valueOf(encode2Hex(encode2MD5(bytes)));
    }

    public static String encodeUrl(String str) {
        return urlEncode(str, "UTF-8");
    }

    public static String decodeUrl(String str) {
        String tempStr;
        if (str == null) {
            return null;
        }
        try {
            tempStr = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            tempStr = null;
        }
        if (tempStr == null) {
            return str;
        }
        return tempStr;
    }

    public static String urlEncode(String str, String charsetName) {
        String tempStr;
        if (str == null) {
            return null;
        }
        try {
            tempStr = URLEncoder.encode(str, charsetName);
        } catch (Exception e) {
            tempStr = null;
        }
        if (tempStr == null) {
            return str;
        }
        return tempStr;
    }
}
