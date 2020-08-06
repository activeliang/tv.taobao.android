package mtopsdk.common.util;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.log.LogAdapter;

public class TBSdkLog {
    private static final String TAG = "mtopsdk.TBSdkLog";
    private static Map<String, LogEnable> logEnabaleMap = new HashMap(5);
    private static LogEnable logEnable = LogEnable.DebugEnable;
    private static LogAdapter mLogAdapter;
    private static boolean printLog = true;
    private static boolean tLogEnabled = true;

    static {
        for (LogEnable logEnable2 : LogEnable.values()) {
            logEnabaleMap.put(logEnable2.getLogEnable(), logEnable2);
        }
    }

    public enum LogEnable {
        VerboseEnable("V"),
        DebugEnable("D"),
        InfoEnable("I"),
        WarnEnable("W"),
        ErrorEnable("E"),
        NoneEnable("L");
        
        private String logEnable;

        public String getLogEnable() {
            return this.logEnable;
        }

        private LogEnable(String logEnable2) {
            this.logEnable = logEnable2;
        }
    }

    public static void setLogAdapter(LogAdapter logAdapter) {
        mLogAdapter = logAdapter;
        Log.d(TAG, "[setLogAdapter] logAdapter=" + logAdapter);
    }

    public static void setPrintLog(boolean printLog2) {
        printLog = printLog2;
        Log.d(TAG, "[setPrintLog] printLog=" + printLog2);
    }

    public static boolean isPrintLog() {
        return printLog;
    }

    public static void setTLogEnabled(boolean tLogEnabled2) {
        tLogEnabled = tLogEnabled2;
        Log.d(TAG, "[setTLogEnabled] tLogEnabled=" + tLogEnabled2);
    }

    public static void setLogEnable(LogEnable logEnable2) {
        if (logEnable2 != null) {
            logEnable = logEnable2;
            Log.d(TAG, "[setLogEnable] logEnable=" + logEnable2);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, (String) null, msg);
    }

    public static void d(String tag, String seqNo, String msg) {
        if (!isLogEnable(LogEnable.DebugEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(2, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.d(tag, append(seqNo, msg));
        }
    }

    public static void d(String tag, String seqNo, String... msg) {
        if (!isLogEnable(LogEnable.DebugEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(2, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.d(tag, append(seqNo, msg));
        }
    }

    public static void i(String tag, String msg) {
        i(tag, (String) null, msg);
    }

    public static void i(String tag, String seqNo, String msg) {
        if (!isLogEnable(LogEnable.InfoEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(4, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.i(tag, append(seqNo, msg));
        }
    }

    public static void i(String tag, String seqNo, String... msg) {
        if (!isLogEnable(LogEnable.InfoEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(4, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.i(tag, append(seqNo, msg));
        }
    }

    public static void w(String tag, String msg) {
        w(tag, (String) null, msg);
    }

    public static void w(String tag, String seqNo, String msg) {
        if (!isLogEnable(LogEnable.WarnEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(8, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.w(tag, append(seqNo, msg));
        }
    }

    public static void w(String tag, String msg, Throwable t) {
        w(tag, (String) null, msg, t);
    }

    public static void w(String tag, String seqNo, String msg, Throwable t) {
        if (!isLogEnable(LogEnable.WarnEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(8, tag, append(seqNo, msg), t);
            }
        } else if (printLog) {
            Log.w(tag, append(seqNo, msg), t);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, (String) null, msg);
    }

    public static void e(String tag, String seqNo, String msg) {
        if (!isLogEnable(LogEnable.ErrorEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(16, tag, append(seqNo, msg), (Throwable) null);
            }
        } else if (printLog) {
            Log.e(tag, append(seqNo, msg));
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        e(tag, (String) null, msg, t);
    }

    public static void e(String tag, String seqNo, String msg, Throwable t) {
        if (!isLogEnable(LogEnable.ErrorEnable)) {
            return;
        }
        if (tLogEnabled) {
            if (mLogAdapter != null) {
                mLogAdapter.printLog(16, tag, append(seqNo, msg), t);
            }
        } else if (printLog) {
            Log.e(tag, append(seqNo, msg), t);
        }
    }

    private static String append(String seqNo, String... msg) {
        StringBuilder builder = new StringBuilder();
        if (seqNo != null) {
            builder.append("[seq:").append(seqNo).append("]|");
        }
        if (msg != null) {
            for (int i = 0; i < msg.length; i++) {
                builder.append(msg[i]);
                if (i < msg.length - 1) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    public static boolean isLogEnable(LogEnable logEnable2) {
        LogEnable logAdapterLevel;
        if (!(!tLogEnabled || mLogAdapter == null || (logAdapterLevel = logEnabaleMap.get(mLogAdapter.getLogLevel())) == null || logEnable.ordinal() == logAdapterLevel.ordinal())) {
            setLogEnable(logAdapterLevel);
        }
        return logEnable2.ordinal() >= logEnable.ordinal();
    }

    public static void logTraceId(String clientTraceId, String serverTraceId) {
        try {
            if (mLogAdapter != null) {
                mLogAdapter.traceLog(clientTraceId, serverTraceId);
            }
        } catch (Throwable th) {
            Log.w(TAG, "[logTraceId] call LogAdapter.traceLog error");
        }
    }
}
