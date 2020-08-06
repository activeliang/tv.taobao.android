package android.taobao.atlas.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean startWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    public static String substringAfter(String str, String separator) {
        int pos;
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null || (pos = str.indexOf(separator)) == -1) {
            return "";
        }
        return str.substring(separator.length() + pos);
    }

    public static String substringBetween(String str, String open, String close) {
        int start;
        int end;
        if (str == null || open == null || close == null || (start = str.indexOf(open)) == -1 || (end = str.indexOf(close, open.length() + start)) == -1) {
            return null;
        }
        return str.substring(open.length() + start, end);
    }

    public static String[] split(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        int sizePlus1;
        int i;
        int sizePlus12;
        int i2;
        int sizePlus13;
        int sizePlus14;
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        int i3 = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            sizePlus1 = 1;
            while (i3 < len) {
                if (Character.isWhitespace(str.charAt(i3))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        sizePlus14 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            i3 = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i3));
                        match = false;
                    } else {
                        sizePlus14 = sizePlus1;
                    }
                    i3++;
                    start = i3;
                    sizePlus1 = sizePlus14;
                } else {
                    lastMatch = false;
                    match = true;
                    i3++;
                }
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            int sizePlus15 = 1;
            while (i3 < len) {
                if (str.charAt(i3) == sep) {
                    if (match || preserveAllTokens) {
                        boolean lastMatch2 = true;
                        sizePlus13 = sizePlus15 + 1;
                        if (sizePlus15 == max) {
                            i3 = len;
                            lastMatch2 = false;
                        }
                        list.add(str.substring(start, i3));
                        match = false;
                    } else {
                        sizePlus13 = sizePlus15;
                    }
                    i2 = i3 + 1;
                    start = i2;
                    sizePlus15 = sizePlus13;
                } else {
                    lastMatch = false;
                    match = true;
                    i2 = i3 + 1;
                }
            }
            int i4 = sizePlus15;
        } else {
            sizePlus1 = 1;
            while (i3 < len) {
                if (separatorChars.indexOf(str.charAt(i3)) >= 0) {
                    if (match || preserveAllTokens) {
                        boolean lastMatch3 = true;
                        sizePlus12 = sizePlus1 + 1;
                        if (sizePlus1 == max) {
                            i3 = len;
                            lastMatch3 = false;
                        }
                        list.add(str.substring(start, i3));
                        match = false;
                    } else {
                        sizePlus12 = sizePlus1;
                    }
                    i = i3 + 1;
                    start = i;
                    sizePlus1 = sizePlus12;
                } else {
                    lastMatch = false;
                    match = true;
                    i = i3 + 1;
                }
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i3));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
