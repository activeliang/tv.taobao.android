package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.monitor.WVMonitorService;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class WVReporter extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("reportError".equals(action)) {
            reportError(callback, params);
        } else if (!"reportDomLoad".equals(action)) {
            return false;
        } else {
            reportDomLoad(callback, params);
        }
        return true;
    }

    public synchronized void reportError(WVCallBackContext callback, String param) {
        try {
            JSONObject obj = new JSONObject(param);
            String url = callback.getWebview().getUrl();
            if (WVMonitorService.getErrorMonitor() != null) {
                WVMonitorService.getErrorMonitor().didOccurJSError(url, obj.optString("msg"), obj.optString("file"), obj.optString("line"));
            }
            callback.success();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    public synchronized void reportDomLoad(WVCallBackContext callback, String param) {
        try {
            JSONObject obj = new JSONObject(param);
            String url = callback.getWebview().getUrl();
            long time = obj.optLong("time", 0);
            long firstBytetime = obj.optLong("firstByte", 0);
            Iterator<String> iterator = obj.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.startsWith("self_")) {
                    Long val = Long.valueOf(obj.optLong(key));
                    if (WVMonitorService.getPerformanceMonitor() != null) {
                        WVMonitorService.getPerformanceMonitor().didPageOccurSelfDefinedEvent(url, key.substring(5), val.longValue());
                    }
                }
            }
            if (WVMonitorService.getPerformanceMonitor() != null) {
                WVMonitorService.getPerformanceMonitor().didPageDomLoadAtTime(url, time);
                WVMonitorService.getPerformanceMonitor().didPageReceiveFirstByteAtTime(url, firstBytetime);
            }
            callback.success();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }
}
