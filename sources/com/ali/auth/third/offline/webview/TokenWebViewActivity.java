package com.ali.auth.third.offline.webview;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.login.task.LoginByIVTokenTask;

public class TokenWebViewActivity extends BaseWebViewActivity {
    public static final String CALLBACK = "https://www.alipay.com/webviewbridge";
    public static String mScene;
    public static String mToken;
    private LoginService mLoginService;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = getIntent().getStringExtra("token");
        mScene = getIntent().getStringExtra("scene");
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
                if (TokenWebViewActivity.this.checkWebviewBridge(url)) {
                    return TokenWebViewActivity.this.overrideCallback(Uri.parse(url));
                }
                if (view instanceof AuthWebView) {
                    ((AuthWebView) view).loadUrl(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        };
    }

    /* access modifiers changed from: private */
    public boolean overrideCallback(Uri uri) {
        Bundle bundle = serialBundle(uri.getQuery());
        if (bundle == null) {
            bundle = new Bundle();
        }
        String h5action = bundle.getString("action");
        String string = bundle.getString("token");
        String string2 = bundle.getString("scene");
        if (TextUtils.isEmpty(h5action) || Constants.ACTION_QUIT.equals(h5action)) {
            finish();
            return true;
        } else if (!Constants.ACTION_CONTINUELOGIN.equals(h5action)) {
            return false;
        } else {
            if (CallbackContext.loginCallback != null) {
                new LoginByIVTokenTask(this, (LoginCallback) CallbackContext.loginCallback).execute(new String[]{mToken, mScene, uri.getQuery()});
            }
            return true;
        }
    }

    /* access modifiers changed from: private */
    public boolean checkWebviewBridge(String url) {
        Uri uri = Uri.parse(url);
        if ("https://www.alipay.com/webviewbridge".contains(uri.getAuthority() + uri.getPath())) {
            return true;
        }
        return false;
    }
}
