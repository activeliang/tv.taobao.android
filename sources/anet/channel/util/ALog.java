package anet.channel.util;

import android.text.TextUtils;
import android.util.Log;
import mtopsdk.common.util.SymbolExpUtil;

public class ALog {
    private static Object LOG_BREAK = "|";
    private static boolean canUseTlog = true;
    private static boolean isPrintLog = true;
    private static volatile ILog log = logcat;
    public static Logcat logcat = new Logcat();

    public interface ILog {
        void d(String str, String str2);

        void e(String str, String str2);

        void e(String str, String str2, Throwable th);

        void i(String str, String str2);

        boolean isPrintLog(int i);

        boolean isValid();

        void setLogLevel(int i);

        void w(String str, String str2);

        void w(String str, String str2, Throwable th);
    }

    public static class Level {
        public static final int D = 1;
        public static final int E = 4;
        public static final int I = 2;
        public static final int N = 5;
        public static final int V = 0;
        public static final int W = 3;
    }

    public static class Logcat implements ILog {
        int defaultLevel = 1;

        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        public void i(String tag, String msg) {
            Log.i(tag, msg);
        }

        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        public void w(String tag, String msg, Throwable e) {
            Log.w(tag, msg, e);
        }

        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        public void e(String tag, String msg, Throwable e) {
            Log.e(tag, msg, e);
        }

        public boolean isPrintLog(int level) {
            return level >= this.defaultLevel;
        }

        public void setLogLevel(int level) {
            if (level < 0 || level > 5) {
                this.defaultLevel = 5;
            } else {
                this.defaultLevel = level;
            }
        }

        public boolean isValid() {
            return true;
        }
    }

    public static void setLog(ILog log2) {
        if (log2 != null) {
            if ((canUseTlog || !log2.getClass().getSimpleName().toLowerCase().contains("tlog")) && log2.isValid()) {
                log = log2;
            }
        }
    }

    public static ILog getLog() {
        return log;
    }

    public static void setPrintLog(boolean printLog) {
        isPrintLog = printLog;
    }

    public static void setLevel(int level) {
        if (log != null) {
            log.setLogLevel(level);
        }
    }

    public static boolean isPrintLog(int logLevel) {
        if (isPrintLog && log != null) {
            return log.isPrintLog(logLevel);
        }
        return false;
    }

    public static void d(String tag, String msg, String seq, Object... kv) {
        if (isPrintLog(1) && log != null) {
            log.d(buildLogTag(tag), buildLogMsg(msg, seq, kv));
        }
    }

    public static void i(String tag, String msg, String seq, Object... kv) {
        if (isPrintLog(2) && log != null) {
            log.i(buildLogTag(tag), buildLogMsg(msg, seq, kv));
        }
    }

    public static void w(String tag, String msg, String seq, Object... kv) {
        if (isPrintLog(3) && log != null) {
            log.w(buildLogTag(tag), buildLogMsg(msg, seq, kv));
        }
    }

    public static void w(String tag, String msg, String seq, Throwable t, Object... kv) {
        if (isPrintLog(3) && log != null) {
            log.w(buildLogTag(tag), buildLogMsg(msg, seq, kv), t);
        }
    }

    public static void e(String tag, String msg, String seq, Object... kv) {
        if (isPrintLog(4) && log != null) {
            log.e(buildLogTag(tag), buildLogMsg(msg, seq, kv));
        }
    }

    public static void e(String tag, String msg, String seq, Throwable t, Object... kv) {
        if (isPrintLog(4) && log != null) {
            log.e(buildLogTag(tag), buildLogMsg(msg, seq, kv), t);
        }
    }

    private static String buildLogTag(String tag) {
        return tag;
    }

    private static String buildLogMsg(String msg, String seq, Object... kv) {
        if (msg == null && seq == null && kv == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(64);
        if (!TextUtils.isEmpty(seq)) {
            builder.append(LOG_BREAK).append("[seq:").append(seq).append("]");
        }
        if (msg != null) {
            builder.append(" ").append(msg);
        }
        if (kv != null) {
            int i = 0;
            while (i + 1 < kv.length) {
                builder.append(" ").append(kv[i] != null ? kv[i] : "").append(SymbolExpUtil.SYMBOL_COLON).append(kv[i + 1] != null ? kv[i + 1] : "");
                i += 2;
            }
            if (i < kv.length) {
                builder.append(" ");
                builder.append(kv[i]);
            }
        }
        return builder.toString();
    }

    @Deprecated
    public static void setUseTlog(boolean b) {
        if (!b) {
            canUseTlog = false;
            log = logcat;
            return;
        }
        canUseTlog = true;
    }

    @Deprecated
    public static void setEnableTLog(boolean b) {
        if (!b) {
            canUseTlog = false;
            log = logcat;
            return;
        }
        canUseTlog = true;
    }

    @Deprecated
    public static boolean isPrintLog() {
        return false;
    }
}
