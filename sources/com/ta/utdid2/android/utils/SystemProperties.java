package com.ta.utdid2.android.utils;

public class SystemProperties {
    public static String get(String key) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{key});
        } catch (Exception e) {
            return "";
        }
    }

    public static String get(String key, String defaultValue) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls.newInstance(), new Object[]{key, defaultValue});
        } catch (Exception e) {
            return "";
        }
    }
}
