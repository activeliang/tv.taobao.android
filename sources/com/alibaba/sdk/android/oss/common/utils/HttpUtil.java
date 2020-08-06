package com.alibaba.sdk.android.oss.common.utils;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {
    public static String urlEncode(String value, String encoding) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(value, encoding).replace("+", "%20").replace("*", "%2A").replace("%7E", "~").replace("%2F", WVNativeCallbackUtil.SEPERATER);
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to encode url!", e);
        }
    }

    public static String paramToQueryString(Map<String, String> params, String charset) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder paramString = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> p : params.entrySet()) {
            String key = p.getKey();
            String value = p.getValue();
            if (!first) {
                paramString.append("&");
            }
            paramString.append(urlEncode(key, charset));
            if (value != null) {
                paramString.append("=").append(urlEncode(value, charset));
            }
            first = false;
        }
        return paramString.toString();
    }
}
