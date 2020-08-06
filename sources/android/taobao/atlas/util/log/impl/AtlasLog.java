package android.taobao.atlas.util.log.impl;

import android.taobao.atlas.util.log.ILog;
import android.util.Log;

public class AtlasLog {
    private static final String PreFix = "Atlas.";
    private static ILog externalLogger;

    public static void setExternalLogger(ILog logger) {
        externalLogger = logger;
    }

    public static void v(String tag, String msg) {
        if (externalLogger != null) {
            externalLogger.v(PreFix.concat(tag), msg);
        } else {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (externalLogger != null) {
            externalLogger.i(PreFix.concat(tag), msg);
        } else {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (externalLogger != null) {
            externalLogger.d(PreFix.concat(tag), msg);
        } else {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
        if (externalLogger != null) {
            externalLogger.w(PreFix.concat(tag), msg);
        }
    }

    public static void e(String tag, String msg) {
        if (externalLogger != null) {
            externalLogger.e(PreFix.concat(tag), msg);
        } else {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (externalLogger != null) {
            externalLogger.e(PreFix.concat(tag), msg, e);
        } else {
            Log.e(tag, msg, e);
        }
    }
}
