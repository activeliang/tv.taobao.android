package com.taobao.wireless.lang;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import java.net.URLEncoder;
import java.util.Map;

public class UrlBuilder {
    private StringBuilder stringBuilder;

    public UrlBuilder(String url) {
        this.stringBuilder = new StringBuilder(url);
    }

    public UrlBuilder appendQuery(String key, String value) {
        this.stringBuilder.append(getSplitChar());
        try {
            this.stringBuilder.append(URLEncoder.encode(key, "UTF-8"));
            this.stringBuilder.append("=");
            this.stringBuilder.append(URLEncoder.encode(value, "UTF-8"));
        } catch (Exception e) {
        }
        return this;
    }

    public String toString() {
        return this.stringBuilder.toString();
    }

    private String getSplitChar() {
        char lastChar = getLastChar(this.stringBuilder);
        if ('?' == lastChar || '&' == lastChar) {
            return "";
        }
        if (this.stringBuilder.indexOf(WVUtils.URL_DATA_CHAR) >= 0) {
            return "&";
        }
        return WVUtils.URL_DATA_CHAR;
    }

    private char getLastChar(StringBuilder str) {
        return str.charAt(str.length() - 1);
    }

    public static String appendQueryIfNotExist(String url, Map<String, String> safeMap) {
        String path;
        String[] kv;
        if (safeMap == null || safeMap.size() == 0) {
            return url;
        }
        String[] urls = url.split("\\?");
        String query = null;
        if (urls.length == 2) {
            path = urls[0];
            query = urls[1];
        } else {
            path = urls[0];
        }
        if (!CheckUtils.isEmpty(query)) {
            for (String pair : query.split("&")) {
                if (!CheckUtils.isEmpty(pair) && (kv = pair.split("=")) != null && kv.length == 2 && !CheckUtils.isEmpty(kv[0]) && !CheckUtils.isEmpty(kv[1])) {
                    safeMap.put(kv[0], kv[1]);
                }
            }
        }
        UrlBuilder urlBuilder = new UrlBuilder(path);
        for (String key : safeMap.keySet()) {
            urlBuilder.appendQuery(key, safeMap.get(key));
        }
        return urlBuilder.toString();
    }
}
