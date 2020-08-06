package com.tvtaobao.android.takeoutwares;

import android.util.Log;

public class TOWLogger {
    private static final String TAG_PREFIX = (TOWLogger.class.getSimpleName() + "_");
    private static LoggerProxy loggerProxy = null;

    public interface LoggerProxy {
        void d(String str, String str2);

        void e(String str, String str2);

        void i(String str, String str2);
    }

    public static void i(String tagBody, String msg) {
        String tag = TAG_PREFIX + tagBody;
        if (loggerProxy != null) {
            loggerProxy.i(tag, msg);
        } else if (TOWConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tagBody, String msg) {
        String tag = TAG_PREFIX + tagBody;
        if (loggerProxy != null) {
            loggerProxy.d(tag, msg);
        } else if (TOWConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tagBody, String msg) {
        String tag = TAG_PREFIX + tagBody;
        if (loggerProxy != null) {
            loggerProxy.e(tag, msg);
        } else if (TOWConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void setLogger(LoggerProxy logger) {
        loggerProxy = logger;
    }
}
