package android.taobao.windvane.extra.uc;

import android.content.Context;
import android.net.Uri;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.webview.IWVWebView;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import com.uc.webview.export.GeolocationPermissions;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebView;

public class WVUCWebChromeClient extends WebChromeClient {
    private static final String TAG = "WVUCWebChromeClient";
    protected Context mContext;

    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        final ValueCallback<Uri> callback = valueCallback;
        try {
            PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    WVUCWebChromeClient.super.openFileChooser(callback);
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                }
            }).execute();
        } catch (Exception e) {
        }
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        final WebView webView1 = webView;
        final ValueCallback<Uri[]> filePathCallback1 = filePathCallback;
        final WebChromeClient.FileChooserParams fileChooserParams1 = fileChooserParams;
        try {
            PermissionProposer.buildPermissionTask(this.mContext, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                public void run() {
                    boolean unused = WVUCWebChromeClient.super.onShowFileChooser(webView1, filePathCallback1, fileChooserParams1);
                }
            }).setTaskOnPermissionDenied(new Runnable() {
                public void run() {
                }
            }).execute();
        } catch (Exception e) {
        }
        return super.onShowFileChooser(webView1, filePathCallback1, fileChooserParams1);
    }

    public WVUCWebChromeClient() {
    }

    public WVUCWebChromeClient(Context context) {
        this.mContext = context;
    }

    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (view instanceof IWVWebView) {
            if (WVEventService.getInstance().onEvent(2003, (IWVWebView) view, url, message, defaultValue, result).isSuccess) {
                return true;
            }
        }
        if (defaultValue == null || !defaultValue.equals("wv_hybrid:")) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
        WVJsBridge.getInstance().callMethod((IWVWebView) view, message);
        result.confirm("");
        return true;
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if (WVEventService.getInstance().onEvent(2001).isSuccess) {
            return true;
        }
        String data = consoleMessage.message();
        if (data == null || !data.startsWith("wvNativeCallback")) {
            if (TaoLog.getLogStatus()) {
                switch (AnonymousClass5.$SwitchMap$android$webkit$ConsoleMessage$MessageLevel[consoleMessage.messageLevel().ordinal()]) {
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

    /* renamed from: android.taobao.windvane.extra.uc.WVUCWebChromeClient$5  reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
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
}
