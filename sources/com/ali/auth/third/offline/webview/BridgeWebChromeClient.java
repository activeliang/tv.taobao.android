package com.ali.auth.third.offline.webview;

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
import com.ali.auth.third.core.message.Message;
import com.ali.auth.third.core.message.MessageUtils;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.context.BridgeCallbackContext;
import java.lang.reflect.Method;

public class BridgeWebChromeClient extends WebChromeClient {
    private static final String TAG = BridgeWebChromeClient.class.getSimpleName();
    /* access modifiers changed from: private */
    public static boolean evaluateJavascriptSupported = (Build.VERSION.SDK_INT >= 19);

    public final boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        String str;
        if (defaultValue == null) {
            return false;
        }
        if (defaultValue.equals("wv_hybrid:")) {
            handleWindVaneNoHandler(view, message);
            result.confirm("");
            return true;
        } else if (!TextUtils.equals(defaultValue, "hv_hybrid:") || !(view instanceof AuthWebView)) {
            return false;
        } else {
            AuthWebView secureWebView = (AuthWebView) view;
            HavanaBridgeProtocal jbp = parseMessage(message);
            BridgeCallbackContext bridgeCallbackContext = new BridgeCallbackContext();
            bridgeCallbackContext.requestId = jbp.requestId;
            bridgeCallbackContext.webView = secureWebView;
            Object bridgeObject = secureWebView.getBridgeObj(jbp.objName);
            if (bridgeObject == null) {
                Message errorMessage = MessageUtils.createMessage(10000, jbp.objName);
                SDKLogger.e(TAG, errorMessage.toString());
                bridgeCallbackContext.onFailure(errorMessage.code, errorMessage.message);
                result.confirm("");
                return true;
            }
            try {
                Method bridgeMethod = bridgeObject.getClass().getMethod(jbp.methodName, new Class[]{BridgeCallbackContext.class, String.class});
                if (bridgeMethod.isAnnotationPresent(BridgeMethod.class)) {
                    try {
                        Object[] objArr = new Object[2];
                        objArr[0] = bridgeCallbackContext;
                        if (TextUtils.isEmpty(jbp.param)) {
                            str = "{}";
                        } else {
                            str = jbp.param;
                        }
                        objArr[1] = str;
                        bridgeMethod.invoke(bridgeObject, objArr);
                    } catch (Exception e) {
                        Message errorMessage2 = MessageUtils.createMessage(10010, e.getMessage());
                        SDKLogger.e(TAG, errorMessage2.toString() + "," + e.toString());
                        bridgeCallbackContext.onFailure(errorMessage2.code, errorMessage2.message);
                    }
                } else {
                    Message errorMessage3 = MessageUtils.createMessage(952, jbp.objName, jbp.methodName);
                    SDKLogger.e(TAG, errorMessage3.toString());
                    bridgeCallbackContext.onFailure(errorMessage3.code, errorMessage3.message);
                }
                result.confirm("");
                return true;
            } catch (NoSuchMethodException e2) {
                Message errorMessage4 = MessageUtils.createMessage(951, jbp.objName, jbp.methodName);
                SDKLogger.e(TAG, errorMessage4 + "," + e2.toString());
                bridgeCallbackContext.onFailure(errorMessage4.code, errorMessage4.message);
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
                    if (this.webView instanceof AuthWebView) {
                        ((AuthWebView) this.webView).loadUrl(loadUrlScript);
                    } else {
                        this.webView.loadUrl(loadUrlScript);
                    }
                }
            } catch (Exception e2) {
            }
        }
    }
}
