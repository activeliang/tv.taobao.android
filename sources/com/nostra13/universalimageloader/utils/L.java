package com.nostra13.universalimageloader.utils;

import android.util.Log;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class L {
    private static volatile boolean DISABLED = false;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    private L() {
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void d(String message, Object... args) {
        log(3, (Throwable) null, message, args);
    }

    public static void i(String message, Object... args) {
        log(4, (Throwable) null, message, args);
    }

    public static void w(String message, Object... args) {
        log(5, (Throwable) null, message, args);
    }

    public static void e(Throwable ex) {
        log(6, ex, (String) null, new Object[0]);
    }

    public static void e(String message, Object... args) {
        log(6, (Throwable) null, message, args);
    }

    public static void e(Throwable ex, String message, Object... args) {
        log(6, ex, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        String logMessage;
        String log;
        if (!DISABLED) {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (ex == null) {
                log = message;
            } else {
                if (message == null) {
                    logMessage = ex.getMessage();
                } else {
                    logMessage = message;
                }
                log = String.format(LOG_FORMAT, new Object[]{logMessage, Log.getStackTraceString(ex)});
            }
            Log.println(priority, ImageLoader.TAG, log);
        }
    }
}
