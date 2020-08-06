package mtopsdk.extra.antiattack;

import android.util.Log;
import com.taobao.tlog.adapter.AdapterForTLog;

public final class LogTool {
    public static final int D = 2;
    public static final int E = 16;
    public static final int I = 4;
    public static final int N = 32;
    public static final int V = 1;
    public static final int W = 8;
    public static volatile boolean mEnableLog = true;
    public static volatile boolean mEnableTLog = true;
    public static volatile int mLogLevel = 4;

    private LogTool() {
    }

    public static int print(int logLevel, String tag, String msg, Throwable t) {
        if (!mEnableLog) {
            return 0;
        }
        switch (logLevel) {
            case 1:
                try {
                    if (!mEnableTLog) {
                        return Log.v(tag, msg);
                    }
                    AdapterForTLog.logv(tag, msg);
                    return 0;
                } catch (Throwable e) {
                    e.printStackTrace();
                    return 0;
                }
            case 2:
                if (!mEnableTLog) {
                    return Log.d(tag, msg);
                }
                AdapterForTLog.logd(tag, msg);
                return 0;
            case 4:
                if (!mEnableTLog) {
                    return Log.i(tag, msg);
                }
                AdapterForTLog.logi(tag, msg);
                return 0;
            case 8:
                if (!mEnableTLog) {
                    return Log.w(tag, msg, t);
                }
                AdapterForTLog.logw(tag, msg, t);
                return 0;
            case 16:
                if (!mEnableTLog) {
                    return Log.e(tag, msg, t);
                }
                AdapterForTLog.loge(tag, msg, t);
                return 0;
            default:
                return 0;
        }
    }
}
