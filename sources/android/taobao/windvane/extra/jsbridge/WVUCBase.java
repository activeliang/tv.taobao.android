package android.taobao.windvane.extra.jsbridge;

import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.util.TaoLog;
import com.uc.webview.export.extension.UCCore;

public class WVUCBase extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("onLowMemory".equals(action) && WVUCWebView.getUCSDKSupport()) {
            try {
                UCCore.onLowMemory();
                callback.success();
                return true;
            } catch (Exception e) {
                callback.error("Only UCSDKSupport !");
                TaoLog.d("WVUCBase", "UCCore :: onLowMemory error : " + e.getMessage());
            }
        }
        return false;
    }
}
