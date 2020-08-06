package com.ut.mini.exposure;

import com.alibaba.analytics.utils.Logger;

class ExpLogger {
    static boolean enableLog = false;

    ExpLogger() {
    }

    public static void d() {
        if (enableLog) {
            Logger.d();
        }
    }

    public static void d(String msg, Object... kv) {
        if (enableLog) {
            Logger.d(msg, kv);
        }
    }

    public static void w(String msg, Object... kv) {
        if (enableLog) {
            Logger.w(msg, kv);
        }
    }

    public static void e(String msg, Object... kv) {
        if (enableLog) {
            Logger.e(msg, kv);
        }
    }

    public static void e(String msg, Throwable t, Object... kv) {
        if (enableLog) {
            Logger.e(msg, t, kv);
        }
    }
}
