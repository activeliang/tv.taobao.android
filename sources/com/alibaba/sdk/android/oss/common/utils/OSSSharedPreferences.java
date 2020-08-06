package com.alibaba.sdk.android.oss.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class OSSSharedPreferences {
    private static OSSSharedPreferences sInstance;
    private SharedPreferences mSp;

    private OSSSharedPreferences(Context context) {
        this.mSp = context.getSharedPreferences("oss_android_sdk_sp", 0);
    }

    public static OSSSharedPreferences instance(Context context) {
        if (sInstance == null) {
            synchronized (OSSSharedPreferences.class) {
                if (sInstance == null) {
                    sInstance = new OSSSharedPreferences(context);
                }
            }
        }
        return sInstance;
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor edit = this.mSp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String getStringValue(String key) {
        return this.mSp.getString(key, "");
    }

    public void removeKey(String key) {
        SharedPreferences.Editor edit = this.mSp.edit();
        edit.remove(key);
        edit.commit();
    }

    public boolean contains(String key) {
        return this.mSp.contains(key);
    }
}
