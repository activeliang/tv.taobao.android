package com.ali.user.open.core.webview;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;

public class WVUcWebViewProxy implements IWebViewProxy {
    private MemberUCWebView mWebView;

    public WVUcWebViewProxy(Context context) {
        this.mWebView = new MemberUCWebView(context);
    }

    public void loadUrl(String url) {
        try {
            this.mWebView.loadUrl(url);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String getUrl() {
        return this.mWebView.getUrl();
    }

    public boolean canGoBack() {
        return this.mWebView.canGoBack();
    }

    public void goBack() {
        this.mWebView.goBack();
    }

    public void resumeTimers() {
        this.mWebView.resumeTimers();
    }

    public void onResume() {
        this.mWebView.onResume();
    }

    public void removeAllViews() {
        this.mWebView.removeAllViews();
    }

    public void destroy() {
        this.mWebView.destroy();
    }

    public ViewParent getParent() {
        return this.mWebView.getParent();
    }

    public View getWebView() {
        return this.mWebView;
    }

    public void addBridgeObject(String name, Object o) {
    }
}
