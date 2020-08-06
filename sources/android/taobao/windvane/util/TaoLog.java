package android.taobao.windvane.util;

import android.taobao.windvane.util.log.AndroidLog;
import android.taobao.windvane.util.log.ILog;
import java.util.HashMap;
import java.util.Map;

public final class TaoLog {
    public static Map<String, Integer> LogLevel = new HashMap();
    private static boolean enabled = false;
    private static ILog impl = null;
    private static final String tagPre = "WindVane.";

    static {
        setImpl(new AndroidLog());
        for (ILog.LogLevelEnum leve : ILog.LogLevelEnum.values()) {
            LogLevel.put(leve.getLogLevelName(), Integer.valueOf(leve.getLogLevel()));
        }
    }

    public static void setImpl(ILog implement) {
        impl = implement;
    }

    public static boolean getLogStatus() {
        return impl != null && enabled;
    }

    public static void setLogSwitcher(boolean open) {
        enabled = open;
    }

    public static void d(String tag, String msg, Object... args) {
        if (shouldPrintDebug() && impl != null) {
            impl.d(tagPre + tag, format(msg, args));
        }
    }

    public static void d(String tag, String msg, Throwable tr, Object... args) {
        if (shouldPrintDebug() && impl != null) {
            impl.d(tagPre + tag, format(msg, args), tr);
        }
    }

    public static void v(String tag, String msg) {
        if (shouldPrintVerbose() && impl != null) {
            impl.v(tagPre + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (shouldPrintDebug() && impl != null) {
            impl.d(tagPre + tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (shouldPrintInfo() && impl != null) {
            impl.i(tagPre + tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (shouldPrintWarn() && impl != null) {
            impl.w(tagPre + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (shouldPrintError() && impl != null) {
            impl.e(tagPre + tag, msg);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        if (shouldPrintError() && impl != null) {
            impl.e(tagPre + tag, format(msg, args));
        }
    }

    public static void e(String tag, String msg, Throwable tr, Object... args) {
        if (shouldPrintError() && impl != null) {
            impl.e(tagPre + tag, format(msg, args), tr);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (shouldPrintInfo() && impl != null) {
            impl.i(tagPre + tag, format(msg, args));
        }
    }

    public static void i(String tag, String msg, Throwable tr, Object... args) {
        if (shouldPrintInfo() && impl != null) {
            impl.i(tagPre + tag, format(msg, args), tr);
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (shouldPrintVerbose() && impl != null) {
            impl.v(tagPre + tag, format(msg, args));
        }
    }

    public static void v(String tag, String msg, Throwable tr, Object... args) {
        if (shouldPrintVerbose() && impl != null) {
            impl.v(tagPre + tag, format(msg, args), tr);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        if (shouldPrintWarn() && impl != null) {
            impl.w(tagPre + tag, format(msg, args));
        }
    }

    public static void w(String tag, String msg, Throwable tr, Object... args) {
        if (shouldPrintWarn() && impl != null) {
            impl.w(tagPre + tag, format(msg, args), tr);
        }
    }

    public static boolean shouldPrintDebug() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.DEBUG.getLogLevel());
    }

    public static boolean shouldPrintError() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.ERROR.getLogLevel());
    }

    public static boolean shouldPrintInfo() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.INFO.getLogLevel());
    }

    public static boolean shouldPrintVerbose() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.VERBOSE.getLogLevel());
    }

    public static boolean shouldPrintWarn() {
        return getLogStatus() && impl.isLogLevelEnabled(ILog.LogLevelEnum.WARNING.getLogLevel());
    }

    private static String format(String msg, Object[] args) {
        return (args == null || args.length == 0) ? msg : String.format(msg, args);
    }
}
