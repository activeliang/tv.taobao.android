package com.yunos.tv.blitz.request.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AppDebug {
    private static boolean debug = true;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean isdebug) {
        debug = isdebug;
    }

    public static void i(String tag, String msg) {
        if (isDebug()) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug()) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug()) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable e) {
        Log.v(tag, msg, e);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void showToast(Context context, String title) {
        Toast.makeText(context, title, 1).show();
    }
}
