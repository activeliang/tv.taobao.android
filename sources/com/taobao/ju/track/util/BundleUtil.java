package com.taobao.ju.track.util;

import android.os.Bundle;

public class BundleUtil {
    public static String getString(Bundle bundle, String key) {
        if (bundle == null || key == null || !bundle.containsKey(key)) {
            return null;
        }
        return bundle.getString(key);
    }

    public static String getString(Bundle bundle, String key, String defaultValue) {
        if (bundle == null || key == null || !bundle.containsKey(key)) {
            return defaultValue;
        }
        return bundle.getString(key);
    }
}
