package anet.channel.strategy.utils;

import android.text.TextUtils;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class Utils {
    public static boolean isIPV4Address(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return false;
        }
        char[] bytes = ip.toCharArray();
        if (bytes.length < 7 || bytes.length > 15) {
            return false;
        }
        int n = 0;
        for (char c : bytes) {
            if (c >= '0' && c <= '9') {
                n = ((n * 10) + c) - 48;
                if (n > 255) {
                    return false;
                }
            } else if (c != '.') {
                return false;
            } else {
                n = 0;
            }
        }
        return true;
    }

    public static boolean checkHostValidAndNotIp(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        char[] bytes = host.toCharArray();
        if (bytes.length <= 0 || bytes.length > 255) {
            return false;
        }
        boolean containLetter = false;
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] >= 'A' && bytes[i] <= 'Z') || ((bytes[i] >= 'a' && bytes[i] <= 'z') || bytes[i] == '*')) {
                containLetter = true;
            } else if (!((bytes[i] >= '0' && bytes[i] <= '9') || bytes[i] == '.' || bytes[i] == '-')) {
                return false;
            }
        }
        return containLetter;
    }

    public static String getHeaderFieldByKey(Map<String, List<String>> header, String key) {
        if (header == null || header.isEmpty() || TextUtils.isEmpty(key)) {
            return null;
        }
        List<String> value = null;
        for (Map.Entry<String, List<String>> entry : header.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                value = entry.getValue();
            }
        }
        if (value == null) {
            return null;
        }
        return value.get(0);
    }

    public static String stringNull2Empty(String value) {
        return value == null ? "" : value;
    }

    public static String encodeQueryParams(Map<String, String> params, String encoding) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(64);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey() != null) {
                    builder.append(URLEncoder.encode(entry.getKey(), encoding)).append("=").append(URLEncoder.encode(StringUtils.stringNull2Empty(entry.getValue()), encoding).replace("+", "%20")).append("&");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
        } catch (UnsupportedEncodingException e) {
            ALog.e("Request", "format params failed", (String) null, e, new Object[0]);
        }
        return builder.toString();
    }
}
