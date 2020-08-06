package mtopsdk.common.log;

import com.taobao.tlog.adapter.AdapterForTLog;

public class TLogAdapterImpl implements LogAdapter {
    public String getLogLevel() {
        return AdapterForTLog.getLogLevel();
    }

    public void traceLog(String clientTraceId, String serverTraceId) {
        AdapterForTLog.traceLog(clientTraceId, serverTraceId);
    }

    public void printLog(int logLevel, String tag, String content, Throwable t) {
        switch (logLevel) {
            case 1:
                AdapterForTLog.logv(tag, content);
                return;
            case 2:
                AdapterForTLog.logd(tag, content);
                return;
            case 4:
                AdapterForTLog.logi(tag, content);
                return;
            case 8:
                AdapterForTLog.logw(tag, content, t);
                return;
            case 16:
                AdapterForTLog.loge(tag, content, t);
                return;
            default:
                return;
        }
    }
}
