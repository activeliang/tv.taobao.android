package com.alibaba.analytics.utils;

import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

public class Logger {
    private static final int LOG_LEVLE_D = 4;
    private static final int LOG_LEVLE_E = 1;
    private static final int LOG_LEVLE_I = 3;
    private static final int LOG_LEVLE_L = 0;
    private static final int LOG_LEVLE_V = 5;
    private static final int LOG_LEVLE_W = 2;
    private static String TAG = "Analytics";
    private static final String TAG_ENABLE_LOG = "enablelog";
    private static final String TAG_LOG_PREFIX = "Analytics.";
    private static boolean isDebug = false;
    private static ILogger mLogger = null;

    @Deprecated
    public interface ILog {
        int e(String str, String str2);

        int e(String str, String str2, Throwable th);
    }

    public static void setLogger(ILogger logger) {
        mLogger = logger;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    private static boolean isExtendLogValid(int level) {
        if (mLogger == null || !mLogger.isValid() || level >= mLogger.getLogLevel()) {
            return false;
        }
        return true;
    }

    private static boolean isDefaultLogValid() {
        return isDebug;
    }

    public static void d() {
        if (isExtendLogValid(4)) {
            mLogger.logd(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        } else if (isDefaultLogValid()) {
            Log.d(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void d(String msg, Map<String, String> map) {
        if (isExtendLogValid(4)) {
            mLogger.logd(buildLogTag(), buildLogMsg(msg, map));
        } else if (isDefaultLogValid()) {
            Log.d(buildLogTag(), buildLogMsg(msg, map));
        }
    }

    public static void d(String msg, Object... kv) {
        if (isExtendLogValid(4)) {
            mLogger.logd(buildLogTag(), buildLogMsg(msg, kv));
        } else if (isDefaultLogValid()) {
            Log.d(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void w() {
        if (isExtendLogValid(2)) {
            mLogger.logw(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        } else if (isDefaultLogValid()) {
            Log.w(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void w(String msg, Map<String, String> map) {
        if (isExtendLogValid(2)) {
            mLogger.logw(buildLogTag(), buildLogMsg(msg, map));
        } else if (isDefaultLogValid()) {
            Log.w(buildLogTag(), buildLogMsg(msg, map));
        }
    }

    public static void w(String msg, Object... kv) {
        if (isExtendLogValid(2)) {
            mLogger.logw(buildLogTag(), buildLogMsg(msg, kv));
        } else if (isDefaultLogValid()) {
            Log.w(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void w(String msg, Throwable t, Object... kv) {
        if (isExtendLogValid(2)) {
            mLogger.logw(buildLogTag(), buildLogMsg(msg, kv), t);
        } else if (isDefaultLogValid()) {
            Log.w(buildLogTag(), buildLogMsg(msg, kv), t);
        }
    }

    public static void i() {
        if (isExtendLogValid(3)) {
            mLogger.logi(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        } else if (isDefaultLogValid()) {
            Log.i(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void i(String msg, Map<String, String> map) {
        if (isExtendLogValid(3)) {
            mLogger.logi(buildLogTag(), buildLogMsg(msg, map));
        } else if (isDefaultLogValid()) {
            Log.i(buildLogTag(), buildLogMsg(msg, map));
        }
    }

    public static void i(String msg, Object... kv) {
        if (isExtendLogValid(3)) {
            mLogger.logi(buildLogTag(), buildLogMsg(msg, kv));
        } else if (isDefaultLogValid()) {
            Log.i(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void e() {
        if (isExtendLogValid(1)) {
            mLogger.loge(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        } else if (isDefaultLogValid()) {
            Log.e(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void e(String msg, Map<String, String> map) {
        if (isExtendLogValid(1)) {
            mLogger.loge(buildLogTag(), buildLogMsg(msg, map));
        } else if (isDefaultLogValid()) {
            Log.e(buildLogTag(), buildLogMsg(msg, map));
        }
    }

    public static void e(String msg, Object... kv) {
        if (isExtendLogValid(1)) {
            mLogger.loge(buildLogTag(), buildLogMsg(msg, kv));
        } else if (isDefaultLogValid()) {
            Log.e(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void e(String msg, Throwable t, Object... kv) {
        if (isExtendLogValid(1)) {
            mLogger.loge(buildLogTag(), buildLogMsg(msg, kv), t);
        } else if (isDefaultLogValid()) {
            Log.e(buildLogTag(), buildLogMsg(msg, kv), t);
        }
    }

    private static String formatKv(Object key, Object value) {
        Object[] objArr = new Object[2];
        if (key == null) {
            key = "";
        }
        objArr[0] = key;
        if (value == null) {
            value = "";
        }
        objArr[1] = value;
        return String.format("%s:%s", objArr);
    }

    private static String buildLogTag() {
        if (!isDebug) {
            return TAG;
        }
        StackTraceElement element = getStackTrace();
        String className = "";
        String method = "";
        if (element != null) {
            String name = element.getClassName();
            if (!TextUtils.isEmpty(name)) {
                className = name.substring(name.lastIndexOf(46) + 1);
            }
            method = element.getMethodName();
        }
        return TAG_LOG_PREFIX + className + "." + method;
    }

    private static String buildLogMsg(String msg, Object... kv) {
        if (msg == null && kv == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Object[] objArr = new Object[1];
        if (msg == null) {
            msg = "-";
        }
        objArr[0] = msg;
        builder.append(String.format("[%s] ", objArr));
        if (kv != null) {
            int length = kv.length;
            int i = 0;
            while (i + 1 < kv.length) {
                Object obj = kv[i];
                int i2 = i + 1;
                builder.append(formatKv(obj, kv[i2]));
                if (i2 < length - 1) {
                    builder.append(",");
                }
                i = i2 + 1;
            }
            if (i == kv.length - 1) {
                builder.append(kv[i]);
            }
        }
        return builder.toString();
    }

    private static String buildLogMsg(String msg, Map<String, String> map) {
        if (msg == null && map == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Object[] objArr = new Object[1];
        if (msg == null) {
            msg = "-";
        }
        objArr[0] = msg;
        builder.append(String.format("[%s] ", objArr));
        if (map != null) {
            int count = map.size();
            int index = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.append(entry.getKey() + " : " + entry.getValue());
                index++;
                if (index < count) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    private static StackTraceElement getStackTrace() {
        if (!isDebug) {
            return null;
        }
        try {
            for (StackTraceElement st : Thread.currentThread().getStackTrace()) {
                if (!st.isNativeMethod() && !st.getClassName().equals(Thread.class.getName()) && !st.getClassName().equals(Logger.class.getName())) {
                    return st;
                }
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    @Deprecated
    public static void setLogAdapter(ILog logAdapter) {
    }
}
