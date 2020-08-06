package com.uc.webview.export;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.InputEvent;
import android.view.KeyEvent;
import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: ProGuard */
public class WebViewClient {
    public static final int ERROR_AUTHENTICATION = -4;
    public static final int ERROR_BAD_URL = -12;
    public static final int ERROR_CONNECT = -6;
    public static final int ERROR_FAILED_SSL_HANDSHAKE = -11;
    public static final int ERROR_FILE = -13;
    public static final int ERROR_FILE_NOT_FOUND = -14;
    public static final int ERROR_HOST_LOOKUP = -2;
    public static final int ERROR_IO = -7;
    public static final int ERROR_PROXY_AUTHENTICATION = -5;
    public static final int ERROR_REDIRECT_LOOP = -9;
    public static final int ERROR_TIMEOUT = -8;
    public static final int ERROR_TOO_MANY_REQUESTS = -15;
    public static final int ERROR_UNKNOWN = -1;
    public static final int ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
    public static final int ERROR_UNSUPPORTED_SCHEME = -10;

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return false;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
    }

    public void onPageFinished(WebView webView, String str) {
    }

    public void onRestoreSnapshotFileCompleted() {
    }

    public void onLoadResource(WebView webView, String str) {
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        return null;
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        return null;
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
    }

    public void onFormResubmission(WebView webView, Message message, Message message2) {
        message.sendToTarget();
    }

    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.cancel();
    }

    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        httpAuthHandler.cancel();
    }

    public void onUnhandledInputEvent(WebView webView, InputEvent inputEvent) {
    }

    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return false;
    }

    public void onScaleChanged(WebView webView, float f, float f2) {
    }

    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
    }
}
