package com.uc.webview.export.internal.android;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebView;
import com.uc.webview.export.annotations.Jni;

public class WebChromeClientCompatibility extends WebChromeClient {
    protected com.uc.webview.export.WebChromeClient mClient;
    protected WebView mWebView;

    private class FileChooserParamsAdapter extends WebChromeClient.FileChooserParams {
        private WebChromeClient.FileChooserParams mFileChooserParams;

        FileChooserParamsAdapter(WebChromeClient.FileChooserParams fileChooserParams) {
            this.mFileChooserParams = fileChooserParams;
        }

        public int getMode() {
            return this.mFileChooserParams.getMode();
        }

        public String[] getAcceptTypes() {
            return this.mFileChooserParams.getAcceptTypes();
        }

        public boolean isCaptureEnabled() {
            return this.mFileChooserParams.isCaptureEnabled();
        }

        public CharSequence getTitle() {
            return this.mFileChooserParams.getTitle();
        }

        public String getFilenameHint() {
            return this.mFileChooserParams.getFilenameHint();
        }

        public Intent createIntent() {
            return this.mFileChooserParams.createIntent();
        }
    }

    @Jni
    public boolean onShowFileChooser(android.webkit.WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return this.mClient.onShowFileChooser(this.mWebView, filePathCallback, fileChooserParams == null ? null : new FileChooserParamsAdapter(fileChooserParams));
    }
}
