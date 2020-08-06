package com.taobao.wireless.lang;

import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.List;

public class Convert {
    public static final String EMPTY = "";

    public static int asInt(String value, int def) {
        if (check(value)) {
            return def;
        }
        try {
            return Integer.valueOf(value).intValue();
        } catch (Exception e) {
            return def;
        }
    }

    private static boolean check(String value) {
        if (CheckUtils.isEmpty(value)) {
            return true;
        }
        return false;
    }

    public static long asLong(String value, long def) {
        if (check(value)) {
            return def;
        }
        try {
            return Long.valueOf(value).longValue();
        } catch (Exception e) {
            return def;
        }
    }

    public static double asDouble(String value, double def) {
        if (check(value)) {
            return def;
        }
        try {
            return Double.valueOf(value).doubleValue();
        } catch (Exception e) {
            return def;
        }
    }

    public static float asFloat(String value, float def) {
        try {
            return Float.valueOf(value).floatValue();
        } catch (Exception e) {
            return def;
        }
    }

    public static String asString(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object);
    }

    public static String join(List<String> list, String seperator) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : list) {
            stringBuilder.append(seperator).append(str);
        }
        return stringBuilder.substring(seperator.length());
    }

    public static double convertPrice(String price) {
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            try {
                return Double.parseDouble(price.split("-")[0]);
            } catch (Exception e2) {
                return ClientTraceData.b.f47a;
            }
        }
    }
}
