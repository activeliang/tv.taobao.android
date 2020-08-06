package com.ali.auth.third.offline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.bridge.LoginBridge;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.webview.AuthWebView;
import com.ali.auth.third.offline.webview.BridgeWebChromeClient;

public class QrLoginActivity extends Activity {
    protected ImageView backButton;
    protected ImageView closeButton;
    private String mCacheQrCodeLoginUrl;
    private AuthWebView mWebView;
    protected RelativeLayout titleBar;
    protected TextView titleText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        initViews();
        this.mCacheQrCodeLoginUrl = getIntent().getExtras().getString("qrCodeLoginUrl");
        this.mWebView.addBridgeObject("loginBridge", new LoginBridge());
        this.mWebView.loadUrl(this.mCacheQrCodeLoginUrl);
    }

    public void setOrientation() {
        if (ConfigManager.getInstance().getOrientation() == 0) {
            if (getResources().getConfiguration().orientation == 1) {
                setRequestedOrientation(0);
            }
        } else if (getResources().getConfiguration().orientation == 2) {
            setRequestedOrientation(1);
        }
    }

    private void initViews() {
        LinearLayout mainView = new LinearLayout(this);
        mainView.setLayoutParams(new FrameLayout.LayoutParams((int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_300"), (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_300")));
        mainView.setOrientation(1);
        LinearLayout.LayoutParams layoutParamsTitleBar = new LinearLayout.LayoutParams(-1, (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_titlebar_height"));
        this.titleBar = new RelativeLayout(this);
        this.titleBar.setBackgroundColor(Color.parseColor("#F8F8F8"));
        this.backButton = new ImageView(this);
        RelativeLayout.LayoutParams backButtonParams = new RelativeLayout.LayoutParams(-2, -1);
        backButtonParams.addRule(9);
        backButtonParams.addRule(15);
        backButtonParams.leftMargin = (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_10");
        this.backButton.setScaleType(ImageView.ScaleType.CENTER);
        this.backButton.setImageResource(ResourceUtils.getIdentifier(getApplicationContext(), "drawable", "com_taobao_tae_sdk_web_view_title_bar_back"));
        this.backButton.setPadding(0, 0, (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_20"), 0);
        this.titleText = new TextView(this);
        RelativeLayout.LayoutParams titleTextParams = new RelativeLayout.LayoutParams(-2, -2);
        titleTextParams.addRule(13);
        this.titleText.setMaxWidth((int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_160"));
        this.titleText.setMaxLines(1);
        this.titleText.setTextColor(Color.parseColor("#3d4245"));
        this.titleText.setTextSize(2, 18.0f);
        this.closeButton = new ImageView(this);
        RelativeLayout.LayoutParams closeButtonParams = new RelativeLayout.LayoutParams(-2, -1);
        closeButtonParams.addRule(11);
        closeButtonParams.addRule(15);
        closeButtonParams.rightMargin = (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_10");
        this.closeButton.setScaleType(ImageView.ScaleType.CENTER);
        this.closeButton.setImageResource(ResourceUtils.getIdentifier(getApplicationContext(), "drawable", "com_taobao_tae_sdk_web_view_title_bar_close"));
        this.closeButton.setPadding((int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_20"), 0, 0, 0);
        this.titleBar.addView(this.titleText, titleTextParams);
        this.titleBar.addView(this.closeButton, closeButtonParams);
        mainView.addView(this.titleBar, layoutParamsTitleBar);
        LinearLayout bodyRL = new LinearLayout(this);
        bodyRL.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_space_300")));
        bodyRL.setOrientation(0);
        this.mWebView = createTaeWebView();
        if (this.mWebView == null) {
            LoginStatus.resetLoginFlag();
            finish();
            return;
        }
        this.mWebView.addBridgeObject("accountBridge", new LoginBridge());
        this.mWebView.setWebChromeClient(new BridgeWebChromeClient());
        this.mWebView.setWebViewClient(new WebViewClient() {
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
        });
        this.mWebView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        bodyRL.addView(this.mWebView);
        mainView.addView(bodyRL);
        setContentView(mainView);
    }

    /* access modifiers changed from: protected */
    public AuthWebView createTaeWebView() {
        try {
            return new AuthWebView(this);
        } catch (Throwable th) {
            return null;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        setResult(ResultCode.USER_CANCEL.code, new Intent());
        LoginStatus.resetLoginFlag();
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.mWebView != null) {
            ViewGroup parent = (ViewGroup) this.mWebView.getParent();
            if (parent != null) {
                parent.removeView(this.mWebView);
            }
            this.mWebView.removeAllViews();
            this.mWebView.destroy();
        }
        LoginStatus.resetLoginFlag();
        super.onDestroy();
    }
}
