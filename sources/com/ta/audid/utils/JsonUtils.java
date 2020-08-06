package com.ta.audid.utils;

import com.ta.utdid2.android.utils.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class JsonUtils {
    public static Map<String, String> jsonToMap(String jsonStr) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        Map<String, String> outMap = new HashMap<>();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }

    public String jsonToSortString(String jsonStr) throws Exception {
        return new JSONObject(StringUtils.sortMapByKey(jsonToMap(jsonStr))).toString();
    }
}
