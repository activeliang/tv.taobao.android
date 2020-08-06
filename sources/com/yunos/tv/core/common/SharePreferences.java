package com.yunos.tv.core.common;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.yunos.tv.core.CoreApplication;
import java.util.Set;

public class SharePreferences {
    private static SharedPreferences mPref;

    public static synchronized void initPref() {
        synchronized (SharePreferences.class) {
            if (mPref == null) {
                mPref = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getApplication());
            }
        }
    }

    public static SharedPreferences getSharedPreferences() {
        initPref();
        return mPref;
    }

    public static void destroy() {
        mPref = null;
    }

    public static String getString(String key) {
        return getString(key, (String) null);
    }

    public static String getString(String key, String defValue) {
        initPref();
        return mPref.getString(key, defValue);
    }

    public static void put(String key, String value) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static Boolean getBoolean(String key, boolean defValue) {
        initPref();
        return Boolean.valueOf(mPref.getBoolean(key, defValue));
    }

    public static void put(String key, boolean value) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Long getLong(String key) {
        return getLong(key, 0);
    }

    public static Long getLong(String key, long defValue) {
        initPref();
        return Long.valueOf(mPref.getLong(key, defValue));
    }

    public static void put(String key, long value) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public static Float getFloat(String key, float defValue) {
        initPref();
        return Float.valueOf(mPref.getFloat(key, defValue));
    }

    public static void put(String key, float value) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static Integer getInt(String key) {
        return getInt(key, 0);
    }

    public static Integer getInt(String key, int defValue) {
        initPref();
        return Integer.valueOf(mPref.getInt(key, defValue));
    }

    public static void put(String key, int value) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void rmv(String key) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(key);
        editor.commit();
    }

    @SuppressLint({"NewApi"})
    public static Set<String> getStringSet(String key) {
        return getStringSet(key, (Set<String>) null);
    }

    @SuppressLint({"NewApi"})
    public static Set<String> getStringSet(String key, Set<String> defValues) {
        initPref();
        return mPref.getStringSet(key, defValues);
    }

    @SuppressLint({"NewApi"})
    public static void put(String key, Set<String> values) {
        initPref();
        SharedPreferences.Editor editor = mPref.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }
}
