package com.alibaba.analytics.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MapUtils {
    public static Map<String, String> convertObjectMapToStringMap(Map<String, Object> aMap) {
        if (aMap != null) {
            Map<String, String> lNewMap = new HashMap<>();
            if (!(aMap == null || aMap.size() <= 0 || (lKeys = aMap.keySet().iterator()) == null)) {
                for (String lKey : aMap.keySet()) {
                    String lValue = StringUtils.convertObjectToString(aMap.get(lKey));
                    if (lKey != null) {
                        lNewMap.put(lKey, lValue);
                    }
                }
                return lNewMap;
            }
        }
        return null;
    }

    public static Map<String, String> convertToUrlEncodedMap(Map<String, String> aMap) {
        if (aMap == null) {
            return aMap;
        }
        Map<String, String> aNewMap = new HashMap<>();
        for (String key : aMap.keySet()) {
            if (key instanceof String) {
                String lValue = aMap.get(key);
                if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(lValue)) {
                    try {
                        aNewMap.put(URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(lValue, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return aNewMap;
    }

    public static Map<String, String> convertPropertiesToMap(Properties aProperties) {
        if (aProperties == null) {
            return null;
        }
        Map<String, String> aNewMap = new HashMap<>();
        for (Object key : aProperties.keySet()) {
            if (key instanceof String) {
                String lKeyStr = StringUtils.convertObjectToString(key);
                String lValue = StringUtils.convertObjectToString(aProperties.get(key));
                if (!StringUtils.isEmpty(lKeyStr) && !StringUtils.isEmpty(lValue)) {
                    aNewMap.put(lKeyStr, lValue);
                }
            }
        }
        return aNewMap;
    }

    public static Map<String, String> convertStringAToMap(String... aKVS) {
        if (aKVS == null || aKVS.length <= 0) {
            return null;
        }
        for (int i = 0; i < aKVS.length; i++) {
            String str = aKVS[i];
            if (aKVS.length > i) {
                String str2 = aKVS[i + 1];
            }
        }
        return null;
    }
}
