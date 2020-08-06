package com.taobao.ju.track.util;

import android.net.Uri;

public class UriUtil {
    public static String getString(Uri uri, String key) {
        if (key == null || uri == null || uri.getQueryParameter(key) == null) {
            return null;
        }
        return uri.getQueryParameter(key);
    }

    public static String getString(Uri uri, String key, String defaultValue) {
        if (key == null || uri == null || uri.getQueryParameter(key) == null) {
            return defaultValue;
        }
        return uri.getQueryParameter(key);
    }
}
