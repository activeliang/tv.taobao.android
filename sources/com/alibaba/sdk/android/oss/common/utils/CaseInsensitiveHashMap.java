package com.alibaba.sdk.android.oss.common.utils;

import java.util.HashMap;

public class CaseInsensitiveHashMap<k, v> extends HashMap<k, v> {
    public v get(Object key) {
        if (key == null || containsKey(key) || !(key instanceof String)) {
            return super.get(key);
        }
        String lowCaseKey = ((String) key).toLowerCase();
        if (containsKey(lowCaseKey)) {
            return get(lowCaseKey);
        }
        return null;
    }
}
