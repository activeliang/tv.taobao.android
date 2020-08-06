package com.uc.webview.export.internal.a;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.uc.webview.export.DownloadListener;
import com.uc.webview.export.WebBackForwardList;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.internal.interfaces.ICommonExtension;
import com.uc.webview.export.internal.interfaces.IUCExtension;
import com.uc.webview.export.internal.interfaces.IWebView;
import com.uc.webview.export.internal.interfaces.IWebViewOverride;

/* compiled from: ProGuard */
public class p extends WebView implements IWebView {
    private com.uc.webview.export.WebView a;
    private IWebViewOverride b;

    /* compiled from: ProGuard */
    private class a implements IWebView.IHitTestResult {
        private WebView.HitTestResult b;

        /* synthetic */ a(p pVar, WebView.HitTestResult hitTestResult, byte b2) {
            this(hitTestResult);
        }

        private a(WebView.HitTestResult hitTestResult) {
            this.b = hitTestResult;
        }

        public final int getType() {
            return this.b.getType();
        }

        public final String getExtra() {
            return this.b.getExtra();
        }
    }

    public p(Context context, AttributeSet attributeSet, com.uc.webview.export.WebView webView) {
        super(context, attributeSet);
        this.a = webView;
        setWebViewClient(new q(webView, new WebViewClient()));
        getSettings().setSavePassword(false);
    }

    public void computeScroll() {
        if (this.b != null) {
            this.b.coreComputeScroll();
        } else {
            super.computeScroll();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.b != null) {
            this.b.coreOnConfigurationChanged(configuration);
        } else {
            super.onConfigurationChanged(configuration);
        }
    }

    public void onVisibilityChanged(View view, int i) {
        if (this.b != null) {
            this.b.coreOnVisibilityChanged(view, i);
        } else {
            super.onVisibilityChanged(view, i);
        }
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        if (this.b != null) {
            this.b.coreOnScrollChanged(i, i2, i3, i4);
        } else {
            super.onScrollChanged(i, i2, i3, i4);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.b != null) {
            return this.b.coreDispatchTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void draw(Canvas canvas) {
        if (this.b != null) {
            this.b.coreDraw(canvas);
        } else {
            super.draw(canvas);
        }
    }

    public void destroy() {
        if (this.b != null) {
            this.b.coreDestroy();
        } else {
            super.destroy();
        }
    }

    public void setVisibility(int i) {
        if (this.b != null) {
            this.b.coreSetVisibility(i);
        } else {
            super.setVisibility(i);
        }
    }

    public void requestLayout() {
        if (this.b != null) {
            this.b.coreRequestLayout();
        } else {
            super.requestLayout();
        }
    }

    @TargetApi(9)
    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        if (Build.VERSION.SDK_INT < 9) {
            return false;
        }
        if (this.b != null) {
            return this.b.coreOverScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
        }
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public void superComputeScroll() {
        super.computeScroll();
    }

    public void superOnConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public void superOnVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
    }

    public void superOnScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    public boolean superDispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public void superDraw(Canvas canvas) {
        super.draw(canvas);
    }

    public void superDestroy() {
        super.destroy();
    }

    public void superSetVisibility(int i) {
        super.setVisibility(i);
    }

    public void superRequestLayout() {
        super.requestLayout();
    }

    public boolean superOverScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public ICommonExtension getCommonExtension() {
        return this;
    }

    public IUCExtension getUCExtension() {
        return null;
    }

    public void notifyForegroundChanged(boolean z) {
    }

    public boolean isInOverScrollMoving() {
        return false;
    }

    public void setDropDownOverScrollEnabled(boolean z) {
    }

    public View getView() {
        return this;
    }

    public void setOverrideObject(IWebViewOverride iWebViewOverride) {
        this.b = iWebViewOverride;
    }

    public IWebViewOverride getOverrideObject() {
        return this.b;
    }

    public void clearClientCertPreferences(Runnable runnable) {
    }

    public void findAllAsync(String str) {
        if (Build.VERSION.SDK_INT >= 16) {
            super.findAllAsync(str);
        }
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        if (downloadListener == null) {
            super.setDownloadListener((android.webkit.DownloadListener) null);
        } else {
            super.setDownloadListener(new b(downloadListener));
        }
    }

    public void setFindListener(WebView.FindListener findListener) {
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        if (webViewClient == null) {
            webViewClient = new WebViewClient();
        }
        setWebViewClient(new q(this.a, webViewClient));
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        if (webChromeClient == null) {
            super.setWebChromeClient((android.webkit.WebChromeClient) null);
        } else {
            super.setWebChromeClient(new j(this.a, webChromeClient));
        }
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        if (Build.VERSION.SDK_INT >= 19) {
            super.evaluateJavascript(str, valueCallback);
        }
    }

    public WebBackForwardList copyBackForwardListInner() {
        android.webkit.WebBackForwardList copyBackForwardList = super.copyBackForwardList();
        if (copyBackForwardList != null) {
            return new i(copyBackForwardList);
        }
        return null;
    }

    public IWebView.IHitTestResult getHitTestResultInner() {
        WebView.HitTestResult hitTestResult = super.getHitTestResult();
        if (hitTestResult != null) {
            return new a(this, hitTestResult, (byte) 0);
        }
        return null;
    }

    public WebSettings getSettingsInner() {
        return new n(super.getSettings());
    }

    public WebBackForwardList restoreStateInner(Bundle bundle) {
        android.webkit.WebBackForwardList restoreState = super.restoreState(bundle);
        if (restoreState == null) {
            return null;
        }
        return new i(restoreState);
    }

    public WebBackForwardList saveStateInner(Bundle bundle) {
        android.webkit.WebBackForwardList saveState = super.saveState(bundle);
        if (saveState == null) {
            return null;
        }
        return new i(saveState);
    }
}
