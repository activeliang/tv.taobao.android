package com.alibaba.motu.crashreporter;

import android.util.Log;

public class LogUtil {
    public static final String TAG = "MotuCrashSDK";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void w(String message, Throwable e) {
        Log.w(TAG, message, e);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String message, Throwable e) {
        Log.e(TAG, message, e);
    }
}
