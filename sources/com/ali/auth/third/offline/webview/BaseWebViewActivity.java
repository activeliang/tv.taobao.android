package com.ali.auth.third.offline.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.cookies.CookieManagerWrapper;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.support.ActivityResultHandler;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class BaseWebViewActivity extends Activity {
    public static final String TAG = BaseWebViewActivity.class.getSimpleName();
    /* access modifiers changed from: protected */
    public AuthWebView authWebView;
    protected ImageView backButton;
    public boolean canReceiveTitle;
    protected ImageView closeButton;
    protected RelativeLayout titleBar;
    /* access modifiers changed from: protected */
    public TextView titleText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        initViews();
        if (this.backButton != null) {
            this.backButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BaseWebViewActivity.this.onBackHistory();
                }
            });
        }
        if (this.closeButton != null) {
            this.closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BaseWebViewActivity.this.finish();
                }
            });
            this.closeButton.setVisibility(8);
        }
        Serializable contextParams = null;
        if (getIntent() != null) {
            contextParams = getIntent().getSerializableExtra("ui_contextParams");
        } else if (savedInstanceState != null) {
            contextParams = savedInstanceState.getSerializable("ui_contextParams");
        }
        if (contextParams instanceof Map) {
            this.authWebView.getContextParameters().putAll((Map) contextParams);
        }
        String title = null;
        String url = null;
        if (savedInstanceState != null) {
            title = savedInstanceState.getString("title");
            url = savedInstanceState.getString("url");
        }
        if (TextUtils.isEmpty(title)) {
            title = getIntent().getStringExtra("title");
        }
        if (TextUtils.isEmpty(title)) {
            this.canReceiveTitle = true;
        } else {
            this.canReceiveTitle = false;
            this.titleText.setText(title);
        }
        if (TextUtils.isEmpty(url)) {
            url = getIntent().getStringExtra("url");
        }
        SDKLogger.d(TAG, "onCreate url=" + url);
        if (KernelContext.checkServiceValid()) {
            new LoadUrlTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{url});
            return;
        }
        LoginStatus.resetLoginFlag();
        finish();
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
        mainView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        mainView.setOrientation(1);
        LinearLayout.LayoutParams layoutParamsTitleBar = new LinearLayout.LayoutParams(-1, (int) ResourceUtils.getDimen(getApplicationContext(), "ali_auth_titlebar_height"));
        this.titleBar = new RelativeLayout(this);
        this.titleBar.setBackgroundColor(Color.parseColor("#F8F8F8"));
        mainView.addView(this.titleBar, layoutParamsTitleBar);
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
        this.titleBar.addView(this.backButton, backButtonParams);
        this.titleBar.addView(this.titleText, titleTextParams);
        this.titleBar.addView(this.closeButton, closeButtonParams);
        this.authWebView = createTaeWebView();
        if (this.authWebView == null) {
            LoginStatus.resetLoginFlag();
            finish();
            return;
        }
        this.authWebView.setWebViewClient(createWebViewClient());
        this.authWebView.setWebChromeClient(createWebChromeClient());
        mainView.addView(this.authWebView, new LinearLayout.LayoutParams(-1, -1));
        setContentView(mainView);
    }

    class LoadUrlTask extends AsyncTask<String, Void, Void> {
        private String url;

        LoadUrlTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... params) {
            this.url = params[0];
            CookieManagerWrapper.INSTANCE.refreshCookie();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!CommonUtils.isNetworkAvailable()) {
                CommonUtils.toast("com_taobao_tae_sdk_network_not_available_message");
                return;
            }
            try {
                BaseWebViewActivity.this.authWebView.resumeTimers();
                if (Build.VERSION.SDK_INT >= 11) {
                    BaseWebViewActivity.this.authWebView.onResume();
                }
            } catch (Exception e) {
            }
            BaseWebViewActivity.this.authWebView.loadUrl(this.url);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", this.authWebView.getUrl());
        outState.putString("title", this.titleText.getText().toString());
        outState.putSerializable("ui_contextParams", this.authWebView.getContextParameters());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.authWebView != null) {
            ViewGroup parent = (ViewGroup) this.authWebView.getParent();
            if (parent != null) {
                parent.removeView(this.authWebView);
            }
            this.authWebView.removeAllViews();
            this.authWebView.destroy();
        }
        super.onDestroy();
    }

    public void setResult(ResultCode resultCode) {
        onFailure(resultCode);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultHandler activityResultHandler = (ActivityResultHandler) KernelContext.getService(ActivityResultHandler.class, Collections.singletonMap(ActivityResultHandler.REQUEST_CODE_KEY, String.valueOf(requestCode)));
        if (activityResultHandler != null) {
            activityResultHandler.onActivityResult(2, requestCode, resultCode, data, this, (Map<Class<?>, Object>) null, this.authWebView);
        }
    }

    /* access modifiers changed from: protected */
    public WebViewClient createWebViewClient() {
        return new BaseWebViewClient();
    }

    /* access modifiers changed from: protected */
    public WebChromeClient createWebChromeClient() {
        return new BridgeWebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                if (BaseWebViewActivity.this.canReceiveTitle) {
                    BaseWebViewActivity.this.titleText.setText(title);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public AuthWebView createTaeWebView() {
        try {
            return new AuthWebView(this);
        } catch (Throwable th) {
            return null;
        }
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
        LoginStatus.resetLoginFlag();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.authWebView != null) {
            try {
                this.authWebView.resumeTimers();
                if (Build.VERSION.SDK_INT >= 11) {
                    this.authWebView.onResume();
                }
            } catch (Exception e) {
            }
        }
    }

    public static Bundle serialBundle(String params) {
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
}
