package com.taobao.orange.util;

import android.util.Log;
import com.taobao.orange.OConstant;
import com.taobao.tlog.adapter.AdapterForTLog;
import mtopsdk.common.util.SymbolExpUtil;

public class OLog {
    private static boolean isPrintLog = true;
    private static volatile boolean isUseTlog = false;
    private static final String preTag = "NOrange.";

    static {
        isUseTlog = false;
        try {
            Class.forName(OConstant.REFLECT_TLOG);
            isUseTlog = true;
        } catch (ClassNotFoundException e) {
            isUseTlog = false;
        }
    }

    public static class Level {
        public static final int D = 1;
        public static final int E = 4;
        public static final int I = 2;
        public static final int L = 5;
        public static final int V = 0;
        public static final int W = 3;

        static int valueOf(String s) {
            switch (s.charAt(0)) {
                case 'D':
                    return 1;
                case 'E':
                    return 4;
                case 'I':
                    return 2;
                case 'V':
                    return 0;
                case 'W':
                    return 3;
                default:
                    return 5;
            }
        }
    }

    public static void setUseTlog(boolean b) {
        isUseTlog = b;
    }

    public static void setPrintLog(boolean printLog) {
        isPrintLog = printLog;
    }

    public static boolean isPrintLog(int logLevel) {
        if (!isPrintLog) {
            return false;
        }
        if (!isUseTlog || logLevel >= Level.valueOf(AdapterForTLog.getLogLevel())) {
            return true;
        }
        return false;
    }

    public static void v(String tag, String msg, Object... kv) {
        if (!isPrintLog(0)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logv(buildLogTag(tag), buildLogMsg(msg, kv));
        } else {
            Log.v(buildLogTag(tag), buildLogMsg(msg, kv));
        }
    }

    public static void d(String tag, String msg, Object... kv) {
        if (!isPrintLog(1)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logd(buildLogTag(tag), buildLogMsg(msg, kv));
        } else {
            Log.d(buildLogTag(tag), buildLogMsg(msg, kv));
        }
    }

    public static void i(String tag, String msg, Object... kv) {
        if (!isPrintLog(2)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logi(buildLogTag(tag), buildLogMsg(msg, kv));
        } else {
            Log.i(buildLogTag(tag), buildLogMsg(msg, kv));
        }
    }

    public static void w(String tag, String msg, Object... kv) {
        if (!isPrintLog(3)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(tag), buildLogMsg(msg, kv));
        } else {
            Log.w(buildLogTag(tag), buildLogMsg(msg, kv));
        }
    }

    public static void w(String tag, String msg, Throwable t, Object... kv) {
        if (!isPrintLog(3)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.logw(buildLogTag(tag), buildLogMsg(msg, kv), t);
        } else {
            Log.w(buildLogTag(tag), buildLogMsg(msg, kv), t);
        }
    }

    public static void e(String tag, String msg, Object... kv) {
        if (!isPrintLog(4)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.loge(buildLogTag(tag), buildLogMsg(msg, kv));
        } else {
            Log.e(buildLogTag(tag), buildLogMsg(msg, kv));
        }
    }

    public static void e(String tag, String msg, Throwable t, Object... kv) {
        if (!isPrintLog(4)) {
            return;
        }
        if (isUseTlog) {
            AdapterForTLog.loge(buildLogTag(tag), buildLogMsg(msg, kv), t);
        } else {
            Log.e(buildLogTag(tag), buildLogMsg(msg, kv), t);
        }
    }

    private static String formatKv(Object key, Object value) {
        StringBuilder sb = new StringBuilder();
        if (key == null) {
            key = "";
        }
        StringBuilder append = sb.append(key).append(SymbolExpUtil.SYMBOL_COLON);
        if (value == null) {
            value = "";
        }
        return append.append(value).toString();
    }

    private static String buildLogTag(String tag) {
        return preTag + tag;
    }

    private static String buildLogMsg(String msg, Object... kv) {
        if (msg == null && kv == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (msg != null) {
            builder.append(" ").append(msg);
        }
        if (kv != null) {
            int i = 0;
            while (i + 1 < kv.length) {
                builder.append(" ");
                Object obj = kv[i];
                int i2 = i + 1;
                builder.append(formatKv(obj, kv[i2]));
                i = i2 + 1;
            }
            if (i == kv.length - 1) {
                builder.append(" ");
                builder.append(kv[i]);
            }
        }
        return builder.toString();
    }
}
