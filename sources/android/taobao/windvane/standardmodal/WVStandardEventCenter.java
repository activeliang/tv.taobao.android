package android.taobao.windvane.standardmodal;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.webview.IWVWebView;

public class WVStandardEventCenter extends WVApiPlugin {
    public static void postNotificationToJS(IWVWebView webView, String eventName, String eventData) {
        WVCallBackContext.fireEvent(webView, eventName, eventData);
    }

    public static void postNotificationToJS(String eventName, String eventData) {
        WVEventService.getInstance().onEvent(WVEventId.NATIVETOH5_EVENT, eventName, eventData);
    }

    public void postNotificationToNative(String params, WVCallBackContext callback) {
        WVEventService.getInstance().onEvent(WVEventId.H5TONATIVE_EVENT, params, callback);
        callback.success();
    }

    public boolean execute(String action, String params, WVCallBackContext callback) {
        if (!"postNotificationToNative".equals(action)) {
            return false;
        }
        postNotificationToNative(params, callback);
        return true;
    }
}
