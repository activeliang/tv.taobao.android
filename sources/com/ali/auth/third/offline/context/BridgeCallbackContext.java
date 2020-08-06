package com.ali.auth.third.offline.context;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.WebView;
import com.ali.auth.third.core.context.KernelContext;
import org.json.JSONException;
import org.json.JSONObject;

public class BridgeCallbackContext {
    public int requestId;
    public WebView webView;

    public Activity getActivity() {
        return (Activity) this.webView.getContext();
    }

    public void success(final String retString) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                String js;
                if (TextUtils.isEmpty(retString)) {
                    js = String.format("javascript:window.HavanaBridge.onSuccess(%s);", new Object[]{Integer.valueOf(BridgeCallbackContext.this.requestId)});
                } else {
                    js = String.format("javascript:window.HavanaBridge.onSuccess(%s,'%s');", new Object[]{Integer.valueOf(BridgeCallbackContext.this.requestId), BridgeCallbackContext.formatJsonString(retString)});
                }
                BridgeCallbackContext.this.callback(js);
            }
        });
    }

    public void onFailure(int code, String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("message", message);
            onFailure(object.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFailure(final String retString) {
        KernelContext.executorService.postUITask(new Runnable() {
            public void run() {
                String js;
                if (TextUtils.isEmpty(retString)) {
                    js = String.format("javascript:window.HavanaBridge.onFailure(%s,'');", new Object[]{Integer.valueOf(BridgeCallbackContext.this.requestId)});
                } else {
                    js = String.format("javascript:window.HavanaBridge.onFailure(%s,'%s');", new Object[]{Integer.valueOf(BridgeCallbackContext.this.requestId), BridgeCallbackContext.formatJsonString(retString)});
                }
                BridgeCallbackContext.this.callback(js);
            }
        });
    }

    /* access modifiers changed from: private */
    public void callback(String js) {
        if (this.webView != null) {
            this.webView.loadUrl(js);
        }
    }

    /* access modifiers changed from: private */
    public static String formatJsonString(String str) {
        return str.replace("\\", "\\\\");
    }
}
