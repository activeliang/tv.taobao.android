package anet.channel.log;

import anet.channel.util.ALog;
import com.taobao.tlog.adapter.AdapterForTLog;

public class TLogAdapter implements ALog.ILog {
    public void d(String tag, String msg) {
        AdapterForTLog.logd(tag, msg);
    }

    public void i(String tag, String msg) {
        AdapterForTLog.logi(tag, msg);
    }

    public void w(String tag, String msg) {
        AdapterForTLog.logw(tag, msg);
    }

    public void w(String tag, String msg, Throwable e) {
        AdapterForTLog.logw(tag, msg, e);
    }

    public void e(String tag, String msg) {
        AdapterForTLog.loge(tag, msg);
    }

    public void e(String tag, String msg, Throwable e) {
        AdapterForTLog.loge(tag, msg);
    }

    public boolean isPrintLog(int level) {
        return level >= convertTLogLevel(AdapterForTLog.getLogLevel().charAt(0));
    }

    public void setLogLevel(int level) {
    }

    public boolean isValid() {
        return AdapterForTLog.isValid();
    }

    private int convertTLogLevel(char c) {
        switch (c) {
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
