package com.ali.user.open.tbauth.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.auth.third.core.model.Constants;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.WebViewOption;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.webview.BaseWebViewActivity;
import com.ali.user.open.tbauth.bridge.SDKBridge;
import com.ali.user.open.tbauth.context.TbAuthContext;

public class TbAuthWebViewActivity extends BaseWebViewActivity {
    public static final String TAG = TbAuthWebViewActivity.class.getSimpleName();
    public static String scene;
    public static String token;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConfigManager.getInstance().getWebViewOption() == WebViewOption.SYSTEM) {
            this.memberWebView.addBridgeObject(TbAuthContext.sdkBridgeName, new SDKBridge());
        }
    }

    private boolean overrideCallback(Uri uri) {
        Bundle bundle = serialBundle(uri.getQuery());
        if (bundle == null) {
            bundle = new Bundle();
        }
        String action = bundle.getString("action");
        if (TextUtils.isEmpty(action) || Constants.ACTION_QUIT.equals(action)) {
            setResult(ResultCode.SUCCESS.code, getIntent().putExtra(Constants.PARAM_IV_TOKEN, bundle.getString(Constants.PARAM_HAVANA_IV_TOKEN)));
            finish();
            return true;
        } else if (Constants.ACTION_RELOGIN.equals(action)) {
            finish();
            return true;
        } else if (Constants.ACTION_CONFIRMLOGIN.equals(action) || Constants.ACTION_TRUSTLOGIN.equals(action)) {
            return true;
        } else {
            if (Constants.ACTION_CONTINUELOGIN.equals(action)) {
                bundle.putString(Constants.QUERY_STRING, uri.getQuery());
                bundle.putString("token", token);
                bundle.putString("scene", scene);
                setResult(ResultCode.CHECK.code, getIntent().putExtras(bundle));
                finish();
                return true;
            } else if ("taobao_auth_token".equals(action)) {
                Intent intent = new Intent();
                intent.putExtra("result", bundle.getString("top_auth_code"));
                setResult(ResultCode.SUCCESS.code, intent);
                finish();
                return true;
            } else if (!"icbu-oauth".equals(action)) {
                return false;
            } else {
                Intent intent2 = new Intent();
                intent2.putExtra("result", bundle.getString("auth_code"));
                setResult(ResultCode.SUCCESS.code, intent2);
                finish();
                return true;
            }
        }
    }

    public void onBackHistory() {
        if (!this.memberWebView.canGoBack() || (!this.memberWebView.getUrl().contains("authorization-notice") && !this.memberWebView.getUrl().contains("agreement"))) {
            setResult(ResultCode.USER_CANCEL.code, new Intent());
            finish();
            return;
        }
        this.memberWebView.goBack();
    }

    public boolean shouldOverrideUrlLoading(String url) {
        SDKLogger.d(TAG, "shouldOverrideUrlLoading url=" + url);
        Uri uri = Uri.parse(url);
        if (checkWebviewBridge(url)) {
            return overrideCallback(uri);
        }
        this.memberWebView.loadUrl(url);
        return true;
    }

    public void onPageStarted(String url) {
        SDKLogger.d(TAG, "onPageStarted url=" + url);
    }

    public void onPageFinished(String url) {
        SDKLogger.d(TAG, "onPageFinished url=" + url);
    }
}
