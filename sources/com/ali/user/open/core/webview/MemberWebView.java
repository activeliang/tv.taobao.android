package com.ali.user.open.core.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SetJavaScriptEnabled"})
public class MemberWebView extends WebView {
    private static final String TAG = MemberWebView.class.getSimpleName();
    private String appCacheDir;
    private Map<String, Object> nameToObj = new HashMap();

    public MemberWebView(Context context) {
        super(context);
        initSettings(context);
    }

    public MemberWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSettings(context);
    }

    public final void addJavascriptInterface(Object object, String name) {
    }

    @TargetApi(21)
    private void initSettings(Context context) {
        WebSettings webSettings = getSettings();
        try {
            webSettings.setJavaScriptEnabled(true);
        } catch (Exception e) {
        }
        webSettings.setSavePassword(false);
        webSettings.setSupportZoom(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setDomStorageEnabled(true);
        this.appCacheDir = context.getApplicationContext().getDir("cache", 0).getPath();
        webSettings.setAppCachePath(this.appCacheDir);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        if (CommonUtils.isNetworkAvailable(context)) {
            webSettings.setCacheMode(-1);
        } else {
            webSettings.setCacheMode(1);
        }
        webSettings.setBuiltInZoomControls(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= 19) {
            setWebContentsDebuggingEnabled(SDKLogger.isDebugEnabled());
        }
        try {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
                webSettings.setMixedContentMode(0);
            } catch (Throwable th) {
            }
        }
        final IWebViewClient webViewClient = (IWebViewClient) context;
        setWebViewClient(new BaseWebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return webViewClient.shouldOverrideUrlLoading(url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewClient.onPageStarted(url);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webViewClient.onPageFinished(url);
            }
        });
        setWebChromeClient(new BridgeWebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webViewClient.onReceivedTitle(title);
            }
        });
    }

    public final void addBridgeObject(String name, Object o) {
        this.nameToObj.put(name, o);
    }

    public Object getBridgeObj(String name) {
        return this.nameToObj.get(name);
    }
}
