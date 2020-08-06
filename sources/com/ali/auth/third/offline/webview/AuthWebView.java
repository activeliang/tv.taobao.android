package com.ali.auth.third.offline.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.ali.auth.third.core.util.CommonUtils;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({"SetJavaScriptEnabled"})
public class AuthWebView extends WebView {
    private static final String TAG = AuthWebView.class.getSimpleName();
    private String appCacheDir;
    private HashMap<String, String> contextParameters = new HashMap<>();
    private Map<String, Object> nameToObj = new HashMap();

    public AuthWebView(Context context) {
        super(context);
        initSettings(context, true);
    }

    public AuthWebView(Context context, boolean updateUAA) {
        super(context);
        initSettings(context, updateUAA);
    }

    public AuthWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSettings(context, true);
    }

    public final void addJavascriptInterface(Object object, String name) {
    }

    @TargetApi(21)
    private void initSettings(Context context, boolean updateUAA) {
        WebSettings webSettings = getSettings();
        try {
            webSettings.setJavaScriptEnabled(true);
        } catch (Exception e) {
        }
        webSettings.setSavePassword(false);
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
    }

    public final void addBridgeObject(String name, Object o) {
        this.nameToObj.put(name, o);
    }

    public final void removeBridgeObject(String name) {
        this.nameToObj.remove(name);
    }

    public Object getBridgeObj(String name) {
        return this.nameToObj.get(name);
    }

    public HashMap<String, String> getContextParameters() {
        return this.contextParameters;
    }
}
