package android.taobao.windvane.jsbridge;

import android.taobao.windvane.monitor.WVMonitorService;
import android.text.TextUtils;

public class WVAppEvent extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        return true;
    }

    public void onPause() {
        this.mWebView.fireEvent("WV.Event.APP.Background", "{}");
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didExitAtTime(this.mWebView.getUrl(), System.currentTimeMillis());
        }
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        String data = this.mWebView.getDataOnActive();
        if (TextUtils.isEmpty(data)) {
            data = "{}";
        }
        this.mWebView.fireEvent("WV.Event.APP.Active", data);
        this.mWebView.setDataOnActive((String) null);
    }
}
