package com.ali.user.open.core.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.R;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.WebViewOption;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;

public class BaseWebViewActivity extends AppCompatActivity implements IWebViewClient {
    public static final String TAG = BaseWebViewActivity.class.getSimpleName();
    protected final String CALLBACK = "https://www.alipay.com/webviewbridge";
    protected IWebViewProxy memberWebView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams(getIntent());
        if (KernelContext.IS_HAVE_WEBVIEW) {
            initViews(savedInstanceState);
            if (this.memberWebView == null) {
                finish();
                return;
            }
            if (KernelContext.applicationContext == null) {
                KernelContext.applicationContext = getApplicationContext();
            }
            if (ConfigManager.getInstance().getWebViewOption() == WebViewOption.SYSTEM) {
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).skipPage(this);
                this.memberWebView.addBridgeObject("ALBBUserTrackJSBridge", new UserTrackBridge());
                this.memberWebView.addBridgeObject("aluWVJSBridge", new UserInfoBridge());
                return;
            }
            return;
        }
        loadUrl(getIntent().getStringExtra("url"));
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        SDKLogger.d(TAG, "onCreate url=" + url);
        if (!KernelContext.checkServiceValid()) {
            finish();
        } else if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.toast("member_sdk_network_not_available_message");
        } else {
            try {
                this.memberWebView.resumeTimers();
                this.memberWebView.onResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadUrl(url);
        }
    }

    /* access modifiers changed from: protected */
    public void initParams(Intent intent) {
        checkWindVaneExist();
    }

    private void initViews(Bundle savedInstanceState) {
        setContentView(getLayout());
        Toolbar toolbar = (AliUccCustomToolbar) findViewById(R.id.ali_user_webview_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BaseWebViewActivity.this.onBackHistory();
            }
        });
        this.memberWebView = createWebView();
        ((FrameLayout) findViewById(R.id.ali_user_webview_container)).addView(this.memberWebView.getWebView(), new FrameLayout.LayoutParams(-1, -1));
        String url = getIntent().getStringExtra("url");
        SDKLogger.d(TAG, "onCreate url=" + url);
        if (!KernelContext.checkServiceValid()) {
            finish();
        } else if (!CommonUtils.isNetworkAvailable()) {
            CommonUtils.toast("member_sdk_network_not_available_message");
        } else {
            try {
                this.memberWebView.resumeTimers();
                this.memberWebView.onResume();
            } catch (Exception e) {
            }
            loadUrl(url);
        }
    }

    /* access modifiers changed from: protected */
    public void loadUrl(String url) {
        this.memberWebView.loadUrl(url);
    }

    /* access modifiers changed from: protected */
    public int getLayout() {
        return R.layout.ali_user_activity_webview;
    }

    public boolean checkWebviewBridge(String url) {
        Uri uri = Uri.parse(url);
        if ("https://www.alipay.com/webviewbridge".contains(uri.getAuthority() + uri.getPath())) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public IWebViewProxy createWebView() {
        if (ConfigManager.getInstance().getWebViewOption() != WebViewOption.UC) {
            return new SystemWebViewProxy(this);
        }
        if (checkWindVaneExist()) {
            return new WVUcWebViewProxy(this);
        }
        return new SystemWebViewProxy(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.memberWebView != null) {
            ViewGroup parent = (ViewGroup) this.memberWebView.getParent();
            if (parent != null) {
                parent.removeView(this.memberWebView.getWebView());
            }
            this.memberWebView.removeAllViews();
            this.memberWebView.destroy();
        }
        super.onDestroy();
    }

    public void setResult(ResultCode resultCode) {
        onFailure(resultCode);
    }

    /* access modifiers changed from: protected */
    public void onFailure(ResultCode resultCode) {
        finish();
    }

    public void onBackPressed() {
        onBackHistory();
    }

    /* access modifiers changed from: protected */
    public void onBackHistory() {
        setResult(ResultCode.USER_CANCEL.code, new Intent());
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.memberWebView != null) {
            try {
                this.memberWebView.resumeTimers();
                this.memberWebView.onResume();
            } catch (Exception e) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).pageDisAppear(this);
    }

    /* access modifiers changed from: protected */
    public boolean checkWindVaneExist() {
        try {
            Class.forName("android.taobao.windvane.WindVaneSDK");
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public Bundle serialBundle(String params) {
        Bundle bundle = null;
        if (params != null && params.length() > 0) {
            String[] strs = params.split("&");
            bundle = new Bundle();
            for (String str : strs) {
                int pos = str.indexOf("=");
                if (pos > 0 && pos < str.length() - 1) {
                    bundle.putString(str.substring(0, pos), str.substring(pos + 1));
                }
            }
        }
        return bundle;
    }

    public boolean shouldOverrideUrlLoading(String url) {
        loadUrl(url);
        return true;
    }

    public void onPageStarted(String url) {
    }

    public void onPageFinished(String url) {
    }

    public void onReceivedTitle(String title) {
        getSupportActionBar().setTitle((CharSequence) title);
    }
}
