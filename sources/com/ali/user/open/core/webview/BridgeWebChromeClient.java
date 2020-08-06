package com.ali.user.open.core.webview;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import com.ali.user.open.core.trace.SDKLogger;
import java.lang.reflect.Method;

public class BridgeWebChromeClient extends WebChromeClient {
    private static final String TAG = BridgeWebChromeClient.class.getSimpleName();
    /* access modifiers changed from: private */
    public static boolean evaluateJavascriptSupported = (Build.VERSION.SDK_INT >= 19);

    public final boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (defaultValue == null) {
            return false;
        }
        if (defaultValue.equals("wv_hybrid:")) {
            handleWindVaneNoHandler(view, message);
            result.confirm("");
            return true;
        } else if (!TextUtils.equals(defaultValue, "hv_hybrid:") || !(view instanceof MemberWebView)) {
            return false;
        } else {
            MemberWebView secureWebView = (MemberWebView) view;
            HavanaBridgeProtocal jbp = parseMessage(message);
            BridgeCallbackContext bridgeCallbackContext = new BridgeCallbackContext();
            bridgeCallbackContext.requestId = jbp.requestId;
            bridgeCallbackContext.webView = secureWebView;
            Object bridgeObject = secureWebView.getBridgeObj(jbp.objName);
            if (bridgeObject == null) {
                SDKLogger.e(TAG, jbp.objName + " JS_BRIDGE_MODULE_NOT_FOUND");
                bridgeCallbackContext.onFailure(10000, jbp.objName);
                result.confirm("");
                return true;
            }
            try {
                Method bridgeMethod = bridgeObject.getClass().getMethod(jbp.methodName, new Class[]{BridgeCallbackContext.class, String.class});
                if (bridgeMethod.isAnnotationPresent(BridgeMethod.class)) {
                    try {
                        Object[] objArr = new Object[2];
                        objArr[0] = bridgeCallbackContext;
                        objArr[1] = TextUtils.isEmpty(jbp.param) ? "{}" : jbp.param;
                        bridgeMethod.invoke(bridgeObject, objArr);
                    } catch (Exception e) {
                        SDKLogger.e(TAG, e.toString());
                        bridgeCallbackContext.onFailure(10010, e.getMessage());
                    }
                } else {
                    SDKLogger.e(TAG, jbp.objName + "," + jbp.methodName + "  JS_BRIDGE_ANNOTATION_NOT_PRESENT");
                    bridgeCallbackContext.onFailure(952, jbp.objName);
                }
                result.confirm("");
                return true;
            } catch (NoSuchMethodException e2) {
                SDKLogger.e(TAG, jbp.objName + "," + jbp.methodName + "," + e2.toString());
                bridgeCallbackContext.onFailure(951, jbp.objName);
                result.confirm("");
                return true;
            }
        }
    }

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (0 == 0) {
            return false;
        }
        Toast.makeText(view.getContext(), message, 1).show();
        return true;
    }

    private HavanaBridgeProtocal parseMessage(String message) {
        String param;
        Uri uri = Uri.parse(message);
        String objName = uri.getHost();
        int token = uri.getPort();
        String methodName = uri.getLastPathSegment();
        String query = uri.getQuery();
        int idx = message.indexOf(WVUtils.URL_DATA_CHAR);
        if (idx == -1) {
            param = null;
        } else {
            param = message.substring(idx + 1);
        }
        HavanaBridgeProtocal hbProtocal = new HavanaBridgeProtocal();
        hbProtocal.methodName = methodName;
        hbProtocal.objName = objName;
        hbProtocal.param = param;
        hbProtocal.requestId = token;
        return hbProtocal;
    }

    private void handleWindVaneNoHandler(WebView webView, String message) {
        try {
            int tmpIndex1 = message.indexOf(58, 9);
            webView.post(new JavaScriptTask(webView, String.format("window.WindVane&&window.WindVane.onFailure(%s,'{\"ret\":\"HY_NO_HANDLER\"');", new Object[]{message.substring(tmpIndex1 + 1, message.indexOf(47, tmpIndex1))})));
        } catch (Exception e) {
            SDKLogger.e("ui", "fail to handler windvane request, the error message is " + e.getMessage(), e);
        }
    }

    private static class JavaScriptTask implements Runnable {
        public String script;
        public WebView webView;

        public JavaScriptTask(WebView webView2, String script2) {
            this.webView = webView2;
            this.script = script2;
        }

        @TargetApi(19)
        public void run() {
            try {
                if (BridgeWebChromeClient.evaluateJavascriptSupported) {
                    try {
                        this.webView.evaluateJavascript(this.script, (ValueCallback) null);
                        return;
                    } catch (Exception e) {
                        SDKLogger.e("ui", "fail to evaluate the script " + e.getMessage(), e);
                    }
                }
                if (1 != 0) {
                    String loadUrlScript = "javascript:" + this.script;
                    if (this.webView instanceof MemberWebView) {
                        ((MemberWebView) this.webView).loadUrl(loadUrlScript);
                    } else {
                        this.webView.loadUrl(loadUrlScript);
                    }
                }
            } catch (Exception e2) {
            }
        }
    }
}
