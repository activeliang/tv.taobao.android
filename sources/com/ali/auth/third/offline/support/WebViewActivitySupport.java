package com.ali.auth.third.offline.support;

import android.webkit.WebView;
import com.ali.auth.third.core.trace.SDKLogger;

public class WebViewActivitySupport {
    private static final String TAG = WebViewActivitySupport.class.getSimpleName();
    public String lastReloadUrl;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final WebViewActivitySupport SINGLETON_INSTANCE = new WebViewActivitySupport();

        private SingletonHolder() {
        }
    }

    public static WebViewActivitySupport getInstance() {
        return SingletonHolder.SINGLETON_INSTANCE;
    }

    private WebViewActivitySupport() {
        this.lastReloadUrl = "";
    }

    public void safeReload(WebView webView) {
        String url = webView.getUrl();
        SDKLogger.d(TAG, "reload url: " + url);
        if (url == null) {
            webView.loadUrl(this.lastReloadUrl);
        } else {
            webView.reload();
        }
    }
}
