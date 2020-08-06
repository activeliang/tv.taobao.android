package com.ta.utdid2.android.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern BlackPattern = Pattern.compile("([\t\r\n])+");

    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        }
        return false;
    }

    public static int hashCode(String value) {
        int h = 0;
        if (value.length() > 0) {
            for (char c : value.toCharArray()) {
                h = (h * 31) + c;
            }
        }
        return h;
    }

    public static boolean equals(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new TreeMap<>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                return key1.compareTo(key2);
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    public static String getStringWithoutBlank(String pStr) {
        if (pStr == null || "".equals(pStr)) {
            return pStr;
        }
        return BlackPattern.matcher(pStr).replaceAll("");
    }
}
