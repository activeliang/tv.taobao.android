package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpSetting {
    public static String get(Context context, String key) {
        SharedPreferences sPreferences;
        if (context == null || (sPreferences = context.getSharedPreferences("ut_setting", 4)) == null) {
            return null;
        }
        return sPreferences.getString(key, (String) null);
    }

    public static void put(Context context, String key, String value) {
        SharedPreferences sPreferences;
        SharedPreferences.Editor editor;
        if (context != null && (sPreferences = context.getSharedPreferences("ut_setting", 4)) != null && (editor = sPreferences.edit()) != null) {
            editor.putString(key, value);
            editor.apply();
        }
    }
}
