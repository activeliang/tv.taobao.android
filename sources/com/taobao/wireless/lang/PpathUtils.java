package com.taobao.wireless.lang;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class PpathUtils {
    public static Map<String, String> sortPpathMapAsc(Map<String, String> map) {
        Map<String, String> result = new HashMap<>();
        if (!CheckUtils.isEmpty((Map) map)) {
            for (String key : map.keySet()) {
                result.put(sortPpathKeyAsc(key), map.get(key));
            }
        }
        return result;
    }

    public static String sortPpathKeyAsc(String str) {
        return sortSkuPropValuesAsc(Arrays.asList(str.split(SymbolExpUtil.SYMBOL_SEMICOLON)));
    }

    public static String sortSkuPropValuesAsc(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return PpathUtils.extractPropId(o1) - PpathUtils.extractPropId(o2);
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : list) {
            stringBuilder.append(SymbolExpUtil.SYMBOL_SEMICOLON).append(str);
        }
        return stringBuilder.substring(1);
    }

    public static int extractPropId(String prop) {
        return Integer.valueOf(extractPropStrId(prop)).intValue();
    }

    public static String extractPropStrId(String propValue) {
        return propValue.substring(0, propValue.indexOf(SymbolExpUtil.SYMBOL_COLON));
    }
}
