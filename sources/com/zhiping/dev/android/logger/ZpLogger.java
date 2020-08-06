package com.zhiping.dev.android.logger;

import android.util.Log;
import com.zhiping.dev.android.logger.ZpLoggerConfig;

public class ZpLogger {
    public static void v(String tag, String msg) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.v)) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.d)) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.i)) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.i)) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.e)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.e)) {
            Log.e(tag, msg, e);
        }
    }

    public static void a(String tag, String msg, Throwable t) {
        if (ZpLoggerConfig.approve(ZpLoggerConfig.Level.a)) {
            Log.wtf(tag, msg, t);
        }
    }

    public static void println(ZpLoggerConfig.Level l, String tag, String msg) {
        if (ZpLoggerConfig.approve(l)) {
            Log.println(l.getVal(), tag, msg);
        }
    }
}
