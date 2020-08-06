package android.taobao.windvane.jsbridge;

import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WVCallBackContext {
    private static final String TAG = "WVCallBackContext";
    private IJsApiFailedCallBack failedCallBack;
    private String mAction = null;
    private boolean mNotiNavtive = false;
    /* access modifiers changed from: private */
    public String methodname;
    /* access modifiers changed from: private */
    public String objectname;
    private IJsApiSucceedCallBack succeedCallBack;
    private String token;
    /* access modifiers changed from: private */
    public IWVWebView webview;

    public WVCallBackContext(IWVWebView webview2) {
        this.webview = webview2;
    }

    public WVCallBackContext(IWVWebView webview2, String token2, String objectname2, String methodname2) {
        this.webview = webview2;
        this.token = token2;
        this.objectname = objectname2;
        this.methodname = methodname2;
    }

    public WVCallBackContext(IWVWebView webView, String token2, String objectName, String methodName, IJsApiSucceedCallBack succeedCallBack2, IJsApiFailedCallBack failedCallBack2) {
        this.webview = webView;
        this.token = token2;
        this.objectname = objectName;
        this.methodname = methodName;
        this.failedCallBack = failedCallBack2;
        this.succeedCallBack = succeedCallBack2;
    }

    public IWVWebView getWebview() {
        return this.webview;
    }

    public void setWebview(IWVWebView webview2) {
        this.webview = webview2;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token2) {
        this.token = token2;
    }

    public void fireEvent(final String eventName, String eventParam) {
        TaoLog.d(TAG, "call fireEvent ");
        if (WVMonitorService.getJsBridgeMonitor() != null) {
            this.webview.getView().post(new Runnable() {
                public void run() {
                    WVMonitorService.getJsBridgeMonitor().didFireEvent(WVCallBackContext.this.webview.getUrl(), eventName);
                }
            });
        }
        if (this.mNotiNavtive) {
            WVEventService.getInstance().onEvent(WVEventId.WV_JSFIRE_EVENT, this.mAction, eventName, eventParam);
        }
        callback(this.webview, String.format("window.WindVane && window.WindVane.fireEvent('%s', '%%s', %s);", new Object[]{eventName, this.token}), eventParam);
    }

    public static void fireEvent(final IWVWebView webview2, final String eventName, String eventParam) {
        TaoLog.d(TAG, "call fireEvent ");
        if (WVMonitorService.getJsBridgeMonitor() != null) {
            webview2.getView().post(new Runnable() {
                public void run() {
                    WVMonitorService.getJsBridgeMonitor().didFireEvent(webview2.getUrl(), eventName);
                }
            });
        }
        callback(webview2, String.format("window.WindVane && window.WindVane.fireEvent('%s', '%%s', %s);", new Object[]{eventName, null}), eventParam);
    }

    public void fireEvent(String eventName) {
        fireEvent(eventName, "{}");
    }

    public void success() {
        success(WVResult.RET_SUCCESS);
    }

    public void success(WVResult result) {
        if (result != null) {
            result.setSuccess();
            String resultStr = result.toJsonString();
            if (this.succeedCallBack != null) {
                this.succeedCallBack.succeed(resultStr);
            } else {
                success(resultStr);
            }
        }
    }

    public void success(String retString) {
        TaoLog.d(TAG, "call success ");
        if (this.succeedCallBack != null) {
            this.succeedCallBack.succeed(retString);
            return;
        }
        if (WVMonitorService.getJsBridgeMonitor() != null) {
            this.webview.getView().post(new Runnable() {
                public void run() {
                    WVMonitorService.getJsBridgeMonitor().didCallBackAtURL(WVCallBackContext.this.objectname, WVCallBackContext.this.methodname, WVCallBackContext.this.webview.getUrl(), "HY_SUCCESS");
                }
            });
        }
        if (this.mNotiNavtive) {
            WVEventService.getInstance().onEvent(WVEventId.WV_JSCALLBAK_SUCCESS, (IWVWebView) null, this.webview.getUrl(), this.mAction, retString);
            this.mNotiNavtive = false;
            this.mAction = null;
        }
        callback(this.webview, String.format("javascript:window.WindVane&&window.WindVane.onSuccess(%s,'%%s');", new Object[]{this.token}), retString);
    }

    public void error() {
        error("{}");
    }

    public void error(WVResult result) {
        if (result != null) {
            String resultStr = result.toJsonString();
            if (this.failedCallBack != null) {
                this.failedCallBack.fail(resultStr);
            } else {
                error(resultStr);
            }
        }
    }

    public void error(String retString) {
        TaoLog.d(TAG, "call error ");
        if (this.failedCallBack != null) {
            this.failedCallBack.fail(retString);
            return;
        }
        if (WVMonitorService.getJsBridgeMonitor() != null) {
            this.webview.getView().post(new Runnable() {
                public void run() {
                    WVMonitorService.getJsBridgeMonitor().didCallBackAtURL(WVCallBackContext.this.objectname, WVCallBackContext.this.methodname, WVCallBackContext.this.webview.getUrl(), "HY_FAILED");
                }
            });
        }
        if (this.mNotiNavtive) {
            WVEventService.getInstance().onEvent(WVEventId.WV_JSCALLBAK_ERROR, (IWVWebView) null, this.webview.getUrl(), this.mAction, retString);
            this.mNotiNavtive = false;
            this.mAction = null;
        }
        callback(this.webview, String.format("javascript:window.WindVane&&window.WindVane.onFailure(%s,'%%s');", new Object[]{this.token}), retString);
    }

    private static void callback(final IWVWebView webview2, String js, String retString) {
        if (TaoLog.getLogStatus() && EnvUtil.isDebug() && !TextUtils.isEmpty(retString)) {
            try {
                new JSONObject(retString);
            } catch (JSONException e) {
                TaoLog.e(TAG, "return param is not a valid json!\n" + js + "\n" + retString);
            }
        }
        if (TextUtils.isEmpty(retString)) {
            retString = "{}";
        }
        try {
            final String jsCode = String.format(js, new Object[]{formatJsonString(retString)});
            webview2.getView().post(new Runnable() {
                public void run() {
                    webview2.evaluateJavascript(jsCode);
                }
            });
        } catch (Exception e2) {
            TaoLog.e(TAG, "callback error. " + e2.getMessage());
        }
    }

    public void setNeedfireNativeEvent(String action, boolean flag) {
        this.mAction = action;
        this.mNotiNavtive = flag;
        TaoLog.e(TAG, "setNeedfireNativeEvent : " + action);
    }

    private static String formatJsonString(String str) {
        if (str.contains(" ")) {
            try {
                str = str.replace(" ", "\\u2028");
            } catch (Exception e) {
            }
        }
        if (str.contains(" ")) {
            try {
                str = str.replace(" ", "\\u2029");
            } catch (Exception e2) {
            }
        }
        return str.replace("\\", "\\\\").replace("'", "\\'");
    }
}
