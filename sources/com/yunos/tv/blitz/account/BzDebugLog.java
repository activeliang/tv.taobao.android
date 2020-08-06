package com.yunos.tv.blitz.account;

import android.annotation.SuppressLint;
import android.util.Log;

public final class BzDebugLog {
    private static boolean isPrintLog = true;

    public static boolean getLogStatus() {
        return isPrintLog;
    }

    @SuppressLint({"NewApi"})
    public static void setLogSwitcher(boolean open) {
        isPrintLog = open;
    }

    public static void d(String tag, String msg) {
        if (isPrintLog && tag != null && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isPrintLog && tag != null && msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isPrintLog && tag != null && msg != null) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrintLog && tag != null && msg != null) {
            Log.w(tag, msg);
        }
    }
}
