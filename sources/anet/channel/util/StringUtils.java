package anet.channel.util;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import java.security.MessageDigest;

public class StringUtils {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String[] parseURL(String rawUrl) {
        if (TextUtils.isEmpty(rawUrl)) {
            return null;
        }
        if (rawUrl.startsWith(WVUtils.URL_SEPARATOR)) {
            rawUrl = "http:" + rawUrl;
        }
        int pos = rawUrl.indexOf(HttpConstant.SCHEME_SPLIT);
        if (pos == -1) {
            return null;
        }
        String[] ret = new String[2];
        String scheme = rawUrl.substring(0, pos);
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return null;
        }
        ret[0] = scheme;
        int length = rawUrl.length();
        int i = pos + 3;
        while (i < length) {
            char c = rawUrl.charAt(i);
            if (c == '/' || c == ':' || c == '?' || c == '#') {
                ret[1] = rawUrl.substring(pos + 3, i);
                return ret;
            }
            i++;
        }
        if (i != length) {
            return null;
        }
        ret[1] = rawUrl.substring(pos + 3);
        return ret;
    }

    public static String concatString(String arg1, String arg2) {
        return new StringBuilder(arg1.length() + arg2.length()).append(arg1).append(arg2).toString();
    }

    public static String concatString(String arg1, String arg2, String arg3) {
        return new StringBuilder(arg1.length() + arg2.length() + arg3.length()).append(arg1).append(arg2).append(arg3).toString();
    }

    public static String buildKey(String protocol, String host) {
        return concatString(protocol, HttpConstant.SCHEME_SPLIT, host);
    }

    public static String simplifyString(String arg, int length) {
        return arg.length() <= length ? arg : concatString(arg.substring(0, length), "......");
    }

    public static String stringNull2Empty(String value) {
        return value == null ? "" : value;
    }

    public static String md5ToHex(String input) {
        if (input == null) {
            return null;
        }
        try {
            return bytesToHexString(MessageDigest.getInstance("MD5").digest(input.getBytes("utf-8")));
        } catch (Exception e) {
            return null;
        }
    }

    private static String bytesToHexString(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[(l << 1)];
        int j = 0;
        for (int i = 0; i < l; i++) {
            int j2 = j + 1;
            out[j] = toDigits[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = toDigits[data[i] & 15];
        }
        return new String(out);
    }

    public static String bytesToHexString(byte[] src) {
        if (src == null) {
            return "";
        }
        return bytesToHexString(src, DIGITS_LOWER);
    }

    public static String longToIP(long l) {
        StringBuilder sb = new StringBuilder(16);
        long a = 1000000000;
        do {
            sb.append(l / a);
            sb.append('.');
            l %= a;
            a /= 1000;
        } while (a > 0);
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStringEqual(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }
}
