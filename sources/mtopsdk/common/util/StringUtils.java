package mtopsdk.common.util;

import java.util.Locale;

public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
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

    public static String concatStr(String key1, String key2) {
        if (isBlank(key1) || isBlank(key2)) {
            return null;
        }
        return key1.trim() + SymbolExpUtil.SYMBOL_DOLLAR + key2.trim();
    }

    public static String concatStr2LowerCase(String key1, String key2) {
        if (isBlank(key1) || isBlank(key2)) {
            return null;
        }
        return (key1.trim() + SymbolExpUtil.SYMBOL_DOLLAR + key2.trim()).toLowerCase(Locale.US);
    }

    public static String concatStr2LowerCase(String key1, String... keys) {
        if (isBlank(key1) || keys == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(key1.trim());
        if (keys.length > 0) {
            for (String key : keys) {
                if (isNotBlank(key)) {
                    sb.append(SymbolExpUtil.SYMBOL_DOLLAR);
                    sb.append(key.trim());
                }
            }
        }
        return sb.toString().toLowerCase(Locale.US);
    }

    public static String[] splitString(String source, String seperator) {
        if (source == null) {
            return null;
        }
        if (!isBlank(seperator)) {
            return source.split(seperator);
        }
        return new String[]{seperator};
    }
}
