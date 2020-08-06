package com.ali.auth.third.offline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.login.task.RefreshSidTask;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.ali.auth.third.offline.webview.BaseWebViewActivity;
import com.ali.auth.third.offline.webview.BaseWebViewClient;
import com.ali.auth.third.offline.webview.BridgeWebChromeClient;

public class LoginWebViewActivity extends BaseWebViewActivity {
    public static final String CALLBACK = "https://www.alipay.com/webviewbridge";
    public static final String TAG = BaseWebViewActivity.class.getSimpleName();
    public static Activity originActivity;
    public static String scene;
    public static String token;
    /* access modifiers changed from: private */
    public LoginService mLoginService;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("token");
        scene = getIntent().getStringExtra("scene");
        this.authWebView.addBridgeObject("accountBridge", new LoginBridge());
        this.authWebView.addBridgeObject("loginBridge", new LoginBridge());
        this.mLoginService = (LoginService) MemberSDK.getService(LoginService.class);
        if (KernelContext.context == null) {
            KernelContext.context = getApplicationContext();
        }
    }

    /* access modifiers changed from: protected */
    public WebViewClient createWebViewClient() {
        return new BaseWebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                SDKLogger.d(LoginWebViewActivity.TAG, "shouldOverrideUrlLoading url=" + url);
                Uri uri = Uri.parse(url);
                if (LoginWebViewActivity.this.mLoginService.isLoginUrl(url)) {
                    new RefreshSidTask(LoginWebViewActivity.this.authWebView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                    return true;
                } else if (LoginWebViewActivity.this.checkWebviewBridge(url)) {
                    return LoginWebViewActivity.this.overrideCallback(uri);
                } else {
                    if (view instanceof AuthWebView) {
                        ((AuthWebView) view).loadUrl(url);
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                SDKLogger.d(LoginWebViewActivity.TAG, "onPageStarted url=" + url);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                SDKLogger.d(LoginWebViewActivity.TAG, "onPageFinished url=" + url);
            }

            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                SDKLogger.d(LoginWebViewActivity.TAG, "onLoadResource url=" + url);
            }
        };
    }

    public boolean checkWebviewBridge(String url) {
        Uri uri = Uri.parse(url);
        if ("https://www.alipay.com/webviewbridge".contains(uri.getAuthority() + uri.getPath())) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean overrideCallback(Uri uri) {
        Bundle bundle = serialBundle(uri.getQuery());
        if (bundle == null) {
            bundle = new Bundle();
        }
        String action = bundle.getString("action");
        String h5token = bundle.getString("token");
        String h5scene = bundle.getString("scene");
        if (TextUtils.isEmpty(action) || Constants.ACTION_QUIT.equals(action)) {
            bundle.putString(Constants.PARAM_IV_TOKEN, bundle.getString(Constants.PARAM_HAVANA_IV_TOKEN));
            bundle.putString(Constants.NATIVE_TOKEN, token);
            bundle.putString(Constants.NATIVE_SCENE, scene);
            setResult(ResultCode.SUCCESS.code, getIntent().putExtras(bundle));
            finish();
            return true;
        } else if (Constants.ACTION_RELOGIN.equals(action)) {
            bundle.putString(Constants.QUERY_STRING, uri.getQuery());
            setResult(ResultCode.CAPTCHA_RELOGIN.code, getIntent().putExtras(bundle));
            finish();
            return true;
        } else if (Constants.ACTION_CONFIRMLOGIN.equals(action)) {
            return true;
        } else {
            if (Constants.ACTION_TRUSTLOGIN.equals(action)) {
                if (TextUtils.isEmpty(h5scene)) {
                    h5scene = scene;
                }
                bundle.putString(Constants.H5_TOKEN, h5token);
                bundle.putString(Constants.H5_SCENE, h5scene);
                setResult(ResultCode.TRUST_LOGIN.code, getIntent().putExtras(bundle));
                finish();
                return true;
            } else if (!Constants.ACTION_CONTINUELOGIN.equals(action)) {
                return false;
            } else {
                bundle.putString(Constants.QUERY_STRING, uri.getQuery());
                bundle.putString(Constants.NATIVE_TOKEN, token);
                bundle.putString(Constants.NATIVE_SCENE, scene);
                setResult(ResultCode.SUCCESS.code, getIntent().putExtras(bundle));
                finish();
                return true;
            }
        }
    }

    /* access modifiers changed from: protected */
    public WebChromeClient createWebChromeClient() {
        return new BridgeWebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                if (!LoginWebViewActivity.this.canReceiveTitle) {
                    return;
                }
                if ((title == null || !title.contains("我喜欢")) && title != null) {
                    LoginWebViewActivity.this.titleText.setText(title);
                }
            }
        };
    }

    public void onBackHistory() {
        if (!this.authWebView.canGoBack() || (!this.authWebView.getUrl().contains("authorization-notice") && !this.authWebView.getUrl().contains("agreement"))) {
            setResult(ResultCode.USER_CANCEL.code, new Intent());
            LoginStatus.resetLoginFlag();
            finish();
            return;
        }
        this.authWebView.goBack();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        LoginStatus.resetLoginFlag();
    }
}
