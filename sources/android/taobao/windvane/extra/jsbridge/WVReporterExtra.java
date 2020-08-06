package android.taobao.windvane.extra.jsbridge;

import android.net.Uri;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.api.WVReporter;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVWebView;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONObject;

public class WVReporterExtra extends WVReporter {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"reportPerformanceCheckResult".equals(action)) {
            return false;
        }
        reportPerformanceCheckResult(callback, params);
        return super.execute(action, params, callback);
    }

    public void reportPerformanceCheckResult(WVCallBackContext callback, String param) {
        try {
            JSONObject obj = new JSONObject(param);
            long score = obj.optLong("score", 0);
            String version = obj.optString("version", "");
            String result = obj.optString("result", "");
            String detail = obj.optString("detail", "");
            String url = this.mWebView.getUrl();
            String bizcode = null;
            try {
                if (this.mWebView instanceof WVWebView) {
                    bizcode = ((WVWebView) this.mWebView).bizCode;
                } else if (this.mWebView instanceof WVUCWebView) {
                    bizcode = ((WVUCWebView) this.mWebView).bizCode;
                }
            } catch (Throwable th) {
            }
            Uri uri = Uri.parse(url);
            if (uri != null && uri.isHierarchical()) {
                String wvBizCode = uri.getQueryParameter("wvBizCode");
                if (!TextUtils.isEmpty(wvBizCode)) {
                    bizcode = wvBizCode;
                }
            }
            if (WVMonitorService.getPerformanceMonitor() != null) {
                WVMonitorService.getPerformanceMonitor().didPerformanceCheckResult(url, score, version, bizcode, result);
            }
            if (TaoLog.getLogStatus()) {
                Log.e("WindVaneWebPerfCheck", String.format("WindVaneWebPerfCheck: %s|%d|%s", new Object[]{url, Long.valueOf(score), detail}));
            }
            callback.success();
        } catch (Exception e) {
            WVResult result2 = new WVResult();
            result2.addData("msg", e.getMessage());
            callback.error(result2);
        }
    }
}
