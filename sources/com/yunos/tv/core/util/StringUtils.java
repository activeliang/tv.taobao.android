package com.yunos.tv.core.util;

public class StringUtils {
    public static String resolvePrice(String price) {
        String text = price;
        String[] s = price.split("\\.");
        if (s.length != 2) {
            return price;
        }
        String price2 = s[1];
        if (price2.matches("[1-9]0")) {
            return text.substring(0, text.indexOf(".") + 2);
        }
        if (price2.equals("00")) {
            return s[0];
        }
        if (price2.equals("0")) {
            return s[0];
        }
        return text;
    }
}
