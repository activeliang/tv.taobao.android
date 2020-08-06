package com.ali.auth.third.offline;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.webview.BaseWebViewActivity;
import com.ali.auth.third.offline.webview.BaseWebViewClient;

public class QrLoginConfirmActivity extends BaseWebViewActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        this.authWebView.addBridgeObject("accountBridge", new LoginBridge());
    }

    /* access modifiers changed from: protected */
    public WebViewClient createWebViewClient() {
        return new BaseWebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                if (!((LoginService) MemberSDK.getService(LoginService.class)).isLoginUrl(url)) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                ((LoginService) MemberSDK.getService(LoginService.class)).auth(new LoginCallback() {
                    public void onSuccess(Session session) {
                        view.reload();
                    }

                    public void onFailure(int code, String msg) {
                    }
                });
                return true;
            }
        };
    }
}
