package com.ta.audid.utils;

public class SystemProperties {
    public static String get(String key) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{key});
        } catch (Exception e) {
            UtdidLogger.e("", "get() ERROR!!! Exception!", e);
            return "";
        }
    }

    public static String get(String key, String defaultValue) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls.newInstance(), new Object[]{key, defaultValue});
        } catch (Exception e) {
            UtdidLogger.e("", "get() ERROR!!! Exception!", e);
            return "";
        }
    }
}
