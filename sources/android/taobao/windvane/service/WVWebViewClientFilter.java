package android.taobao.windvane.service;

import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;

public abstract class WVWebViewClientFilter implements WVEventListener {
    public boolean shouldOverrideUrlLoading(IWVWebView view, String url) {
        return false;
    }

    public void onPageStarted(IWVWebView view, String url) {
    }

    public void onPageFinished(IWVWebView view, String url) {
    }

    public WVWrapWebResourceResponse shouldInterceptRequest(IWVWebView view, String url) {
        return null;
    }

    public void onReceivedError(IWVWebView view, int errorCode, String description, String failingUrl) {
    }

    public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
        WVEventResult eventResult = new WVEventResult(false);
        switch (id) {
            case 1001:
                onPageStarted(ctx.webView, ctx.url);
                return eventResult;
            case 1002:
                onPageFinished(ctx.webView, ctx.url);
                return eventResult;
            case 1003:
                eventResult.isSuccess = shouldOverrideUrlLoading(ctx.webView, ctx.url);
                return eventResult;
            case 1004:
                WVWrapWebResourceResponse response = shouldInterceptRequest(ctx.webView, ctx.url);
                if (response == null) {
                    return eventResult;
                }
                eventResult.isSuccess = true;
                eventResult.resultObj = response;
                return eventResult;
            case 1005:
                onReceivedError(ctx.webView, obj[0].intValue(), obj[1], obj[2]);
                return eventResult;
            default:
                return null;
        }
    }
}
