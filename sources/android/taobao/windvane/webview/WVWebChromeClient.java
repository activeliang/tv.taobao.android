package android.taobao.windvane.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class WVWebChromeClient extends WebChromeClient {
    private static final long MAX_QUOTA = 20971520;
    private static final String TAG = "WVWebChromeClient";
    protected Context mContext;

    public WVWebChromeClient() {
    }

    public WVWebChromeClient(Context context) {
        this.mContext = context;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (WVEventService.getInstance().onEvent(2001).isSuccess) {
            return true;
        }
        String data = consoleMessage.message();
        if (data == null || !data.startsWith("wvNativeCallback")) {
            if (TaoLog.getLogStatus()) {
                switch (AnonymousClass1.$SwitchMap$android$webkit$ConsoleMessage$MessageLevel[consoleMessage.messageLevel().ordinal()]) {
                    case 1:
                        TaoLog.d(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    case 2:
                        TaoLog.e(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    case 3:
                        TaoLog.w(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                    default:
                        TaoLog.d(TAG, "onConsoleMessage: %s at %s: %s", consoleMessage.message(), consoleMessage.sourceId(), String.valueOf(consoleMessage.lineNumber()));
                        break;
                }
            }
            return super.onConsoleMessage(consoleMessage);
        }
        String data2 = data.substring(data.indexOf(WVNativeCallbackUtil.SEPERATER) + 1);
        int secondPos = data2.indexOf(WVNativeCallbackUtil.SEPERATER);
        String id = data2.substring(0, secondPos);
        String data3 = data2.substring(secondPos + 1);
        ValueCallback<String> callback = WVNativeCallbackUtil.getNativeCallback(id);
        if (callback != null) {
            callback.onReceiveValue(data3);
            WVNativeCallbackUtil.clearNativeCallback(id);
            return true;
        }
        TaoLog.e(TAG, "NativeCallback failed: " + data3);
        return true;
    }

    /* renamed from: android.taobao.windvane.webview.WVWebChromeClient$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$webkit$ConsoleMessage$MessageLevel = new int[ConsoleMessage.MessageLevel.values().length];

        static {
            try {
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel[ConsoleMessage.MessageLevel.DEBUG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel[ConsoleMessage.MessageLevel.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel[ConsoleMessage.MessageLevel.WARNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel[ConsoleMessage.MessageLevel.LOG.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$webkit$ConsoleMessage$MessageLevel[ConsoleMessage.MessageLevel.TIP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    @TargetApi(5)
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        callback.invoke(origin, true, false);
    }

    @TargetApi(5)
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        if (estimatedDatabaseSize < MAX_QUOTA) {
            quotaUpdater.updateQuota(estimatedDatabaseSize);
        } else {
            quotaUpdater.updateQuota(quota);
        }
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "onJsPrompt: %s; defaultValue: %s; url: %s", message, defaultValue, url);
        }
        if (view instanceof IWVWebView) {
            if (WVEventService.getInstance().onEvent(2003, (IWVWebView) view, url, message, defaultValue, result).isSuccess) {
                return true;
            }
        }
        if (defaultValue == null || !defaultValue.equals("wv_hybrid:")) {
            return false;
        }
        WVJsBridge.getInstance().callMethod((IWVWebView) (WVWebView) view, message);
        result.confirm("");
        return true;
    }

    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }
}
