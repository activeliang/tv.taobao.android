package com.yunos.tvtaobao.uuid.utils;

import java.lang.reflect.Method;

public class SystemProperties {
    private static final String TAG = "MySystemProperties";
    private static Class<?> mClassType = null;
    private static Method mGetIntMethod = null;
    private static Method mGetMethod = null;
    private static Method mSetMethod = null;

    public static String get(String key, String def) {
        init();
        try {
            return (String) mGetMethod.invoke(mClassType, new Object[]{key, def});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getInt(String key, int def) {
        init();
        int value = def;
        try {
            return ((Integer) mGetIntMethod.invoke(mClassType, new Object[]{key, Integer.valueOf(def)})).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    public static int getSdkVersion() {
        return getInt("ro.build.version.sdk", -1);
    }

    public static void set(String key, String val) {
        init();
        try {
            mSetMethod.invoke(mClassType, new Object[]{key, val});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                mGetMethod = mClassType.getDeclaredMethod("get", new Class[]{String.class, String.class});
                mGetIntMethod = mClassType.getDeclaredMethod("getInt", new Class[]{String.class, Integer.TYPE});
                mSetMethod = mClassType.getDeclaredMethod("set", new Class[]{String.class, String.class});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
