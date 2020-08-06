package com.ta.audid.utils;

import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class UtdidLogger {
    private static String TAG = "Utdid";
    private static boolean isDebug = false;
    private static boolean isSDebug = false;

    public static void setDebug(boolean isDebug2) {
        Log.i(TAG, "setDebug:" + isDebug2);
        isDebug = isDebug2;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void d() {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void e() {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void i() {
        if (isDebug) {
            Log.i(buildLogTag(), buildLogMsg((String) null, new Object[0]));
        }
    }

    public static void d(String msg, Map<String, String> map) {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg(msg, map));
        }
    }

    public static void d(String msg, Object... kv) {
        if (isDebug) {
            Log.d(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void sd(String msg, Object... kv) {
        if (isSDebug) {
            Log.d(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void i(String msg, Object... kv) {
        if (isDebug) {
            Log.i(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void w(String msg, Object... kv) {
        if (isDebug) {
            Log.w(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void w(String msg, Throwable t, Object... kv) {
        if (isDebug) {
            Log.w(buildLogTag(), buildLogMsg(msg, kv), t);
        }
    }

    public static void e(String msg, Object... kv) {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg(msg, kv));
        }
    }

    public static void e(String msg, Throwable t, Object... kv) {
        if (isDebug) {
            Log.e(buildLogTag(), buildLogMsg(msg, kv), t);
        }
    }

    private static String formatKv(Object key, Object value) {
        if (!isDebug) {
            return "";
        }
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
        return buildLogTag(TAG + SymbolExpUtil.SYMBOL_COLON);
    }

    private static String buildLogTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        StackTraceElement element = getStackTrace();
        String className = "";
        if (element != null) {
            String name = element.getClassName();
            if (!TextUtils.isEmpty(name)) {
                className = name.substring(name.lastIndexOf(46) + 1);
            }
        }
        return tag + className + "." + String.valueOf(Process.myPid());
    }

    static String buildLogMsg(String msg, Object... kv) {
        if (msg == null && kv == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        StackTraceElement element = getStackTrace();
        String method = "";
        if (element != null) {
            method = element.getMethodName();
        }
        builder.append(String.format("[%s]", new Object[]{method}));
        if (msg != null) {
            builder.append(" ").append(msg);
        }
        if (kv != null) {
            int i = 0;
            while (i + 1 < kv.length) {
                builder.append("\n\t");
                Object obj = kv[i];
                int i2 = i + 1;
                builder.append(formatKv(obj, kv[i2]));
                i = i2 + 1;
            }
            if (i == kv.length - 1) {
                builder.append("\n\t");
                builder.append(kv[i]);
            }
        }
        return builder.toString();
    }

    static String buildLogMsg(String msg, Map<String, String> map) {
        if (msg == null || map == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        StackTraceElement element = getStackTrace();
        String method = "";
        if (element != null) {
            method = element.getMethodName();
        }
        builder.append(String.format("[%s]", new Object[]{method}));
        builder.append(" ").append(msg);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append("\n\t");
            builder.append(entry.getKey() + " : " + entry.getValue());
        }
        return builder.toString();
    }

    private static StackTraceElement getStackTrace() {
        for (StackTraceElement st : Thread.currentThread().getStackTrace()) {
            if (!st.isNativeMethod() && !st.getClassName().equals(Thread.class.getName()) && !st.getClassName().equals(UtdidLogger.class.getName())) {
                return st;
            }
        }
        return null;
    }
}
