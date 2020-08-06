package com.uc.webview.export.internal.a;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.uc.webview.export.GeolocationPermissions;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebView;
import com.uc.webview.export.internal.android.WebChromeClientCompatibility;
import com.uc.webview.export.internal.interfaces.CommonDef;
import com.uc.webview.export.internal.interfaces.IOpenFileChooser;

/* compiled from: ProGuard */
final class j extends WebChromeClientCompatibility implements IOpenFileChooser {

    /* compiled from: ProGuard */
    private class a implements WebChromeClient.CustomViewCallback {
        private WebChromeClient.CustomViewCallback b;

        public a(WebChromeClient.CustomViewCallback customViewCallback) {
            this.b = customViewCallback;
        }

        public final void onCustomViewHidden() {
            if (this.b != null) {
                this.b.onCustomViewHidden();
            }
        }
    }

    /* compiled from: ProGuard */
    private class b implements GeolocationPermissions.Callback {
        private GeolocationPermissions.Callback b;

        public b(GeolocationPermissions.Callback callback) {
            this.b = callback;
        }

        public final void invoke(String str, boolean z, boolean z2) {
            if (this.b != null) {
                this.b.invoke(str, z, z2);
            }
        }
    }

    public j(WebView webView, com.uc.webview.export.WebChromeClient webChromeClient) {
        this.mWebView = webView;
        this.mClient = webChromeClient;
    }

    public final void onProgressChanged(android.webkit.WebView webView, int i) {
        this.mClient.onProgressChanged(this.mWebView, i);
    }

    public final void onReceivedTitle(android.webkit.WebView webView, String str) {
        this.mClient.onReceivedTitle(this.mWebView, str);
    }

    public final void onReceivedIcon(android.webkit.WebView webView, Bitmap bitmap) {
        this.mClient.onReceivedIcon(this.mWebView, bitmap);
    }

    public final void onReceivedTouchIconUrl(android.webkit.WebView webView, String str, boolean z) {
        this.mClient.onReceivedTouchIconUrl(this.mWebView, str, z);
    }

    public final void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.mClient.onShowCustomView(view, new a(customViewCallback));
    }

    public final void onHideCustomView() {
        this.mClient.onHideCustomView();
    }

    public final boolean onCreateWindow(android.webkit.WebView webView, boolean z, boolean z2, Message message) {
        WebView.WebViewTransport webViewTransport = (WebView.WebViewTransport) message.obj;
        com.uc.webview.export.WebView webView2 = this.mWebView;
        webView2.getClass();
        WebView.WebViewTransport webViewTransport2 = new WebView.WebViewTransport();
        Message obtain = Message.obtain(new k(this, Looper.getMainLooper()));
        obtain.obj = webViewTransport2;
        CommonDef.sOnCreateWindowType = 1;
        boolean onCreateWindow = this.mClient.onCreateWindow(this.mWebView, z, z2, obtain);
        CommonDef.sOnCreateWindowType = 0;
        if (webViewTransport2.getWebView() == null) {
            webViewTransport.setWebView((android.webkit.WebView) null);
        } else {
            webViewTransport.setWebView((android.webkit.WebView) webViewTransport2.getWebView().getCoreView());
        }
        if (webViewTransport.getWebView() != null) {
            message.sendToTarget();
        }
        return onCreateWindow;
    }

    public final void onRequestFocus(android.webkit.WebView webView) {
        this.mClient.onRequestFocus(this.mWebView);
    }

    public final void onCloseWindow(android.webkit.WebView webView) {
        this.mClient.onCloseWindow(this.mWebView);
    }

    public final boolean onJsAlert(android.webkit.WebView webView, String str, String str2, JsResult jsResult) {
        return this.mClient.onJsAlert(this.mWebView, str, str2, new f(jsResult));
    }

    public final boolean onJsConfirm(android.webkit.WebView webView, String str, String str2, JsResult jsResult) {
        return this.mClient.onJsConfirm(this.mWebView, str, str2, new f(jsResult));
    }

    public final boolean onJsPrompt(android.webkit.WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        return this.mClient.onJsPrompt(this.mWebView, str, str2, str3, new e(jsPromptResult));
    }

    public final boolean onJsBeforeUnload(android.webkit.WebView webView, String str, String str2, JsResult jsResult) {
        return this.mClient.onJsBeforeUnload(this.mWebView, str, str2, new f(jsResult));
    }

    public final void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        this.mClient.onGeolocationPermissionsShowPrompt(str, new b(callback));
    }

    public final void onGeolocationPermissionsHidePrompt() {
        this.mClient.onGeolocationPermissionsHidePrompt();
    }

    public final boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return this.mClient.onConsoleMessage(consoleMessage);
    }

    public final Bitmap getDefaultVideoPoster() {
        return this.mClient.getDefaultVideoPoster();
    }

    public final View getVideoLoadingProgressView() {
        return this.mClient.getVideoLoadingProgressView();
    }

    public final void getVisitedHistory(ValueCallback<String[]> valueCallback) {
        this.mClient.getVisitedHistory(valueCallback);
    }

    public final void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
        if (!this.mClient.onShowFileChooser(this.mWebView, new l(this, valueCallback), new m(this))) {
            this.mClient.openFileChooser(valueCallback);
        }
    }

    public final void openFileChooser(ValueCallback<Uri> valueCallback, String str) {
        if (!this.mClient.onShowFileChooser(this.mWebView, new l(this, valueCallback), new m(this))) {
            this.mClient.openFileChooser(valueCallback);
        }
    }

    public final void openFileChooser(ValueCallback<Uri> valueCallback) {
        if (!this.mClient.onShowFileChooser(this.mWebView, new l(this, valueCallback), new m(this))) {
            this.mClient.openFileChooser(valueCallback);
        }
    }
}
