package com.uc.webview.export;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import com.uc.webview.export.GeolocationPermissions;
import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: ProGuard */
public class WebChromeClient {

    @Api
    /* compiled from: ProGuard */
    public interface CustomViewCallback {
        void onCustomViewHidden();
    }

    public void onProgressChanged(WebView webView, int i) {
    }

    public void onReceivedTitle(WebView webView, String str) {
    }

    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
    }

    public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
    }

    public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
    }

    public void onHideCustomView() {
    }

    public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
        return false;
    }

    public void onRequestFocus(WebView webView) {
    }

    public void onCloseWindow(WebView webView) {
    }

    public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        return false;
    }

    public boolean onJsBeforeUnload(WebView webView, String str, String str2, JsResult jsResult) {
        return false;
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
    }

    public void onGeolocationPermissionsHidePrompt() {
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return false;
    }

    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    public View getVideoLoadingProgressView() {
        return null;
    }

    public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        valueCallback.onReceiveValue((Object) null);
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return false;
    }

    @Api
    /* compiled from: ProGuard */
    public static abstract class FileChooserParams {
        public static final int MODE_OPEN = 0;
        public static final int MODE_OPEN_FOLDER = 2;
        public static final int MODE_OPEN_MULTIPLE = 1;
        public static final int MODE_SAVE = 3;

        public abstract Intent createIntent();

        public abstract String[] getAcceptTypes();

        public abstract String getFilenameHint();

        public abstract int getMode();

        public abstract CharSequence getTitle();

        public abstract boolean isCaptureEnabled();

        public static Uri[] parseResult(int i, Intent intent) {
            if (i == 0) {
                return null;
            }
            Uri data = (intent == null || i != -1) ? null : intent.getData();
            if (data == null) {
                return null;
            }
            return new Uri[]{data};
        }
    }
}
