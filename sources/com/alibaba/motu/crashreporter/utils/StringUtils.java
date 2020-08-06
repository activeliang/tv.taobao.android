package com.alibaba.motu.crashreporter.utils;

import java.util.Map;

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String defaultString(String cs, String defualt) {
        return isBlank(cs) ? defualt : cs;
    }

    public static int hashCode(String value) {
        int h = 0;
        if (0 == 0 && value.length() > 0) {
            for (char c : value.toCharArray()) {
                h = (h * 31) + c;
            }
        }
        return h;
    }

    public static String convertObjectToString(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            return ((String) o).toString();
        }
        if (o instanceof Integer) {
            return "" + ((Integer) o).intValue();
        }
        if (o instanceof Long) {
            return "" + ((Long) o).longValue();
        }
        if (o instanceof Double) {
            return "" + ((Double) o).doubleValue();
        }
        if (o instanceof Float) {
            return "" + ((Float) o).floatValue();
        }
        if (o instanceof Short) {
            return "" + ((Short) o).shortValue();
        }
        if (o instanceof Byte) {
            return "" + ((Byte) o).byteValue();
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).toString();
        }
        if (o instanceof Character) {
            return ((Character) o).toString();
        }
        return o.toString();
    }

    public static String convertMapToString(Map<String, String> pMaps) {
        if (pMaps == null) {
            return null;
        }
        boolean lIsFirst = true;
        StringBuffer lSB = new StringBuffer();
        for (String lKey : pMaps.keySet()) {
            String lValue = pMaps.get(lKey);
            if (!(lValue == null || lKey == null)) {
                if (lIsFirst) {
                    if (!"--invalid--".equals(lValue)) {
                        lSB.append(lKey + "=" + lValue);
                    } else {
                        lSB.append(lKey);
                    }
                    lIsFirst = false;
                } else if (!"--invalid--".equals(lValue)) {
                    lSB.append(",").append(lKey + "=" + lValue);
                } else {
                    lSB.append(",").append(lKey);
                }
            }
        }
        return lSB.toString();
    }
}
