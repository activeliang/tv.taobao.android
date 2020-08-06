package com.tvtaobao.voicesdk.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalUnderstand {
    private static String TAG = "LocalUnderstand";
    private static Map<String, Integer> intList;

    public static int ChineseToNumber(String str) {
        if (intList == null) {
            String[] chnNumChinese = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
            intList = new HashMap();
            for (int i = 0; i < chnNumChinese.length; i++) {
                intList.put(chnNumChinese[i], Integer.valueOf(i));
            }
            intList.put("十", 10);
            intList.put("两", 2);
        }
        return intList.get(str).intValue();
    }

    public static boolean isNumeric(String str) {
        if (!Pattern.compile("[0-9]*").matcher(str).matches()) {
            return false;
        }
        return true;
    }

    public static int getNumeric(String str) {
        return Integer.parseInt(Pattern.compile("[^0-9]").matcher(str).replaceAll("").trim());
    }

    public static boolean containNumeric(String str) {
        return str.matches(".*?[\\d]+.*?");
    }

    public static int chooseNum(String str) {
        Matcher m = Pattern.compile(".*(第[一|二|三|四|五|六|七|八|九|1|2|3|4|5|6|7|8|9][个|件|款]).*").matcher(str);
        LogPrint.e(TAG, TAG + ".matches : " + m.matches());
        if (!m.matches()) {
            return -1;
        }
        int startIndex = str.indexOf("第") + 1;
        String num = str.substring(startIndex, startIndex + 1);
        if (isNumeric(num)) {
            return Integer.parseInt(num);
        }
        return ChineseToNumber(num);
    }

    public static boolean isContain(String asr, String matcher) {
        boolean b = Pattern.compile(matcher).matcher(asr).matches();
        LogPrint.i(TAG, TAG + ".isContain matcher : " + b);
        return b;
    }
}
