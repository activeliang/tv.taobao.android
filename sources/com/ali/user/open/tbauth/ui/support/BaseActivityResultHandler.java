package com.ali.user.open.tbauth.ui.support;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.webview.BaseWebViewActivity;
import java.util.Map;

public abstract class BaseActivityResultHandler implements ActivityResultHandler {
    private static final String TAG = "system";

    /* access modifiers changed from: protected */
    public abstract void onCallbackContext(int i, int i2, Intent intent, Activity activity, Map<Class<?>, Object> map, WebView webView);

    /* access modifiers changed from: protected */
    public abstract void onTaeSDKActivity(int i, int i2, Intent intent, BaseWebViewActivity baseWebViewActivity, Map<Class<?>, Object> map, WebView webView);

    public void onActivityResult(int source, int requestCode, int resultCode, Intent data, Activity activity, Map<Class<?>, Object> callbacks, WebView webview) {
        if (source == 1) {
            onCallbackContext(requestCode, resultCode, data, activity, callbacks, webview);
        } else if (source != 2) {
            SDKLogger.e(TAG, "Unrecognized source " + source);
        } else if (activity instanceof BaseWebViewActivity) {
            onTaeSDKActivity(requestCode, resultCode, data, (BaseWebViewActivity) activity, callbacks, webview);
        } else {
            SDKLogger.e(TAG, "OnActivityResult is invoked from unsupported activity type " + activity.getClass().getName());
        }
    }
}
