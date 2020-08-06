package com.taobao.wireless.lang;

import java.util.List;
import java.util.Map;

public class CheckUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }
}
