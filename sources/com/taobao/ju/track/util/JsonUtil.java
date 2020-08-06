package com.taobao.ju.track.util;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class JsonUtil {
    public static Map<String, String> jsonToMap(String json) {
        Map<String, String> result = new HashMap<>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject jsonObject = JSON.parseObject(json.replaceAll("'", "\"").replaceAll(SymbolExpUtil.SYMBOL_SEMICOLON, ","));
            if (jsonObject.keySet().size() > 0) {
                for (String valueOf : jsonObject.keySet()) {
                    String key = String.valueOf(valueOf);
                    result.put(key, (String) jsonObject.get(key));
                }
            }
        }
        return result;
    }
}
