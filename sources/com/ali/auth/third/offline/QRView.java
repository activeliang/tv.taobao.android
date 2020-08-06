package com.ali.auth.third.offline;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.ali.auth.third.offline.webview.BridgeWebChromeClient;

public class QRView extends LinearLayout {
    private static final String TAG = "QRView";
    public static LoginCallback mLoginCallback;
    /* access modifiers changed from: private */
    public AuthWebView mAuthWebView;
    /* access modifiers changed from: private */
    public int mQRViewWidth;

    public QRView(Context context) {
        super(context);
    }

    public QRView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ali_auth_qrview, this, true);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mAuthWebView = (AuthWebView) findViewById(R.id.ali_auth_webview);
        this.mAuthWebView.setPadding(0, 0, 0, 0);
        this.mAuthWebView.addBridgeObject("loginBridge", new LoginBridge());
        this.mAuthWebView.setWebChromeClient(new BridgeWebChromeClient());
        this.mAuthWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        this.mAuthWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(16)
            public void onGlobalLayout() {
                QRView.this.mAuthWebView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int unused = QRView.this.mQRViewWidth = QRView.this.mAuthWebView.getWidth();
            }
        });
    }

    public void showQR(LoginCallback loginCallback) {
        mLoginCallback = loginCallback;
        if (this.mAuthWebView != null) {
            if (this.mQRViewWidth == 0) {
                this.mQRViewWidth = this.mAuthWebView.getWidth();
            }
            float screenDensity = getResources().getDisplayMetrics().density;
            SDKLogger.d(TAG, "qr width = " + this.mAuthWebView.getPaddingLeft() + "   " + this.mAuthWebView.getPaddingTop());
            this.mAuthWebView.loadUrl(String.format(ConfigManager.qrCodeLoginUrl, new Object[]{KernelContext.getAppKey()}) + "&qrwidth=" + ((int) (((double) (((float) this.mQRViewWidth) / screenDensity)) * 0.8d)));
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        mLoginCallback = null;
        super.onDetachedFromWindow();
    }
}
