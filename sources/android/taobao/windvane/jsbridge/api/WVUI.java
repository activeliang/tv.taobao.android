package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;

public class WVUI extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("showLoadingBox".equals(action)) {
            showLoading(params, callback);
            return true;
        } else if (!"hideLoadingBox".equals(action)) {
            return false;
        } else {
            hideLoading(params, callback);
            return true;
        }
    }

    public final void showLoading(String params, WVCallBackContext callback) {
        this.mWebView.showLoadingView();
        callback.success();
    }

    public final void hideLoading(String params, WVCallBackContext callback) {
        this.mWebView.hideLoadingView();
        callback.success();
    }
}
