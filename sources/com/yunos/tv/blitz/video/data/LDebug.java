package com.yunos.tv.blitz.video.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LDebug {
    public static void i(String tag, String msg) {
        if (Config.isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (Config.isDebug()) {
            Log.i(tag, msg, tr);
        }
    }

    public static void isLoggable(String tag, int level) {
        if (Config.isDebug()) {
            Log.isLoggable(tag, level);
        }
    }

    public static void d(String tag, String msg) {
        if (Config.isDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (Config.isDebug()) {
            Log.d(tag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        if (Config.isDebug()) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable e) {
        if (Config.isDebug()) {
            Log.v(tag, msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (Config.isDebug()) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (Config.isDebug()) {
            Log.w(tag, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (Config.isDebug()) {
            Log.w(tag, msg, tr);
        }
    }

    public static void wtf(String tag, String msg) {
        if (Config.isDebug()) {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String tag, Throwable tr) {
        if (Config.isDebug()) {
            Log.wtf(tag, tr);
        }
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (Config.isDebug()) {
            Log.wtf(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (Config.isDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (Config.isDebug()) {
            Log.e(tag, msg, tr);
        }
    }

    public static void showToast(Context context, String title) {
        if (Config.isDebug()) {
            Toast.makeText(context, title, 1).show();
        }
    }
}
