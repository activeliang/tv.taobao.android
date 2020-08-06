package com.alibaba.analytics.utils;

import android.util.Log;

public class SystemProperties {
    private static final String TAG = SystemProperties.class.getSimpleName();

    public static String get(String key) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{key});
        } catch (Exception e) {
            Log.e(TAG, "get() ERROR!!! Exception!", e);
            return "";
        }
    }

    public static String get(String key, String defaultValue) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls.newInstance(), new Object[]{key, defaultValue});
        } catch (Exception e) {
            Log.e(TAG, "get() ERROR!!! Exception!", e);
            return "";
        }
    }
}
