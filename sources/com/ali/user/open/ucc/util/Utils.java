package com.ali.user.open.ucc.util;

import android.text.TextUtils;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.model.RpcResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String buidErrorMessage(RpcResponse response, String defaultMsg) {
        if (response == null) {
            return defaultMsg;
        }
        return TextUtils.isEmpty(response.message) ? "亲，您的手机网络不太顺畅喔~" : response.message;
    }

    public static int buidErrorCode(RpcResponse response, int defaultCode) {
        if (response != null) {
            return response.code;
        }
        return defaultCode;
    }

    public static String generateTraceId(String bizType) {
        if (TextUtils.isEmpty(bizType)) {
            return "";
        }
        return bizType + DeviceInfo.deviceId + (System.currentTimeMillis() / 1000);
    }

    public static Map<String, String> convertJsonStrToMap(String jsonString) {
        try {
            Map<String, String> map = new HashMap<>();
            for (Map.Entry<String, Object> next : JSONObject.parseObject(jsonString).entrySet()) {
                map.put((String) next.getKey(), (String) next.getValue());
            }
            return map;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertMapToJsonStr(Map map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        try {
            return JSON.toJSONString(map);
        } catch (Throwable th) {
            return "";
        }
    }
}
