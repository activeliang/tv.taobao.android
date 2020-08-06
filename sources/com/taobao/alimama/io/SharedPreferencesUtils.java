package com.taobao.alimama.io;

import android.content.SharedPreferences;
import android.support.annotation.Keep;
import com.taobao.utils.Global;

@Keep
public class SharedPreferencesUtils {
    private static final String SP_FILE_NAME = "alimama_advertising";

    private static SharedPreferences.Editor edit() {
        return getSharedPreference().edit();
    }

    private static synchronized SharedPreferences getSharedPreference() {
        SharedPreferences sharedPreferences;
        synchronized (SharedPreferencesUtils.class) {
            sharedPreferences = Global.getApplication().getSharedPreferences(SP_FILE_NAME, 0);
        }
        return sharedPreferences;
    }

    public static String getString(String str, String str2) {
        return getSharedPreference().getString(str, str2);
    }

    public static void putString(String str, String str2) {
        SharedPreferences.Editor edit = edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static void removeKey(String str) {
        SharedPreferences.Editor edit = edit();
        edit.remove(str);
        edit.apply();
    }
}
