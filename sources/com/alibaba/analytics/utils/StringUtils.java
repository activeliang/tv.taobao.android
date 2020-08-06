package com.alibaba.analytics.utils;

import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class StringUtils {
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

    public static int hashCode(String value) {
        int h = 0;
        if (0 == 0 && value.length() > 0) {
            for (char c : value.toCharArray()) {
                h = (h * 31) + c;
            }
        }
        return h;
    }

    public static String convertMapToString(Map<String, String> mTPKCache) {
        if (mTPKCache == null) {
            return null;
        }
        boolean lIsFirst = true;
        StringBuffer lSB = new StringBuffer();
        for (Object o : mTPKCache.keySet()) {
            String lValue = convertObjectToString(mTPKCache.get(o));
            String lKey = convertObjectToString(o);
            if (!(lValue == null || lKey == null)) {
                if (lIsFirst) {
                    lSB.append(lKey + "=" + lValue);
                    lIsFirst = false;
                } else {
                    lSB.append(",").append(lKey + "=" + lValue);
                }
            }
        }
        return lSB.toString();
    }

    public static String convertStringAToString(String... aKvs) {
        if (aKvs != null && aKvs.length == 0) {
            return null;
        }
        StringBuffer lSB = new StringBuffer();
        boolean lFlag = false;
        if (aKvs != null && aKvs.length > 0) {
            for (int i = 0; i < aKvs.length; i++) {
                if (!isEmpty(aKvs[i])) {
                    if (lFlag) {
                        lSB.append(",");
                    }
                    lSB.append(aKvs[i]);
                    lFlag = true;
                }
            }
        }
        return lSB.toString();
    }

    public static String[] convertMapToStringA(Map<String, String> pMaps) {
        if (pMaps != null) {
            List<String> lStrings = new LinkedList<>();
            for (Object o : pMaps.keySet()) {
                String lValue = pMaps.get(o);
                String lKey = convertObjectToString(o);
                if (!(lValue == null || lKey == null)) {
                    lStrings.add(lKey + "=" + lValue);
                }
            }
            if (lStrings.size() > 0) {
                String[] lStringA = new String[lStrings.size()];
                for (int i = 0; i < lStrings.size(); i++) {
                    lStringA[i] = lStrings.get(i);
                }
                return lStringA;
            }
        }
        return null;
    }

    public static String convertToPostString(Map<String, String> pMaps) {
        if (pMaps == null) {
            return null;
        }
        boolean lIsFirst = true;
        StringBuffer lSB = new StringBuffer();
        for (Object o : pMaps.keySet()) {
            String lValue = convertObjectToString(pMaps.get(o));
            String lKey = convertObjectToString(o);
            if (!(lValue == null || lKey == null)) {
                if (lIsFirst) {
                    lSB.append(lKey + "=" + lValue);
                    lIsFirst = false;
                } else {
                    lSB.append("&").append(lKey + "=" + lValue);
                }
            }
        }
        return lSB.toString();
    }

    public static String convertMapToStringWithUrlEncoder(Map<String, String> pMaps) {
        if (pMaps == null) {
            return null;
        }
        boolean lIsFirst = true;
        StringBuffer lSB = new StringBuffer();
        for (Object o : pMaps.keySet()) {
            String lValue = convertObjectToString(pMaps.get(o));
            String lKey = convertObjectToString(o);
            if (!(lValue == null || lKey == null)) {
                if (lIsFirst) {
                    try {
                        lSB.append(URLEncoder.encode(lKey, "UTF-8") + "=" + URLEncoder.encode(lValue, "UTF-8"));
                        lIsFirst = false;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        lSB.append(",").append(URLEncoder.encode(lKey, "UTF-8") + "=" + URLEncoder.encode(lValue, "UTF-8"));
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        return lSB.toString();
    }

    public static String transMapToString(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            sb.append(entry.getKey().toString()).append("'").append(entry.getValue() == null ? "" : entry.getValue().toString()).append(iterator.hasNext() ? Constant.BETTER_ASR : "");
        }
        return sb.toString();
    }

    public static Map<String, String> transStringToMap(String mapString) {
        Map<String, String> map = new HashMap<>();
        StringTokenizer entrys = new StringTokenizer(mapString, Constant.BETTER_ASR);
        while (entrys.hasMoreTokens()) {
            StringTokenizer items = new StringTokenizer(entrys.nextToken(), "'");
            map.put(items.nextToken(), items.hasMoreTokens() ? items.nextToken() : null);
        }
        return map;
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

    public static boolean isEmpty(String imei) {
        return TextUtils.isEmpty(imei);
    }
}
