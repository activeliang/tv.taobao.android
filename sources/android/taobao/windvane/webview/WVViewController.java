package android.taobao.windvane.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.view.AbstractNaviBar;
import android.taobao.windvane.view.WebNaviBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WVViewController extends LinearLayout {
    protected boolean isInited = false;
    protected Context mContext;
    protected WVWebView mWebView;

    @SuppressLint({"NewApi"})
    public WVViewController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public WVViewController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public WVViewController(Context context) {
        super(context);
        this.mContext = context;
    }

    public void init(ParamsParcelable params) {
        if (!this.isInited) {
            initView(params);
        }
    }

    private void initView(ParamsParcelable params) {
        setOrientation(1);
        if (params == null) {
            params = new ParamsParcelable();
        }
        RelativeLayout webviewParent = new RelativeLayout(this.mContext);
        this.mWebView = new WVWebView(this.mContext);
        webviewParent.addView(this.mWebView, new RelativeLayout.LayoutParams(-1, -1));
        View view = webviewParent;
        LinearLayout.LayoutParams aboveParams = new LinearLayout.LayoutParams(-1, 0);
        aboveParams.weight = 1.0f;
        view.setLayoutParams(aboveParams);
        addView(view);
        initWithParams(params);
        this.isInited = true;
    }

    /* access modifiers changed from: protected */
    public void initWithParams(ParamsParcelable params) {
        if (params.isNavBarEnabled()) {
            WebNaviBar mNaviBar = new WebNaviBar(this.mContext, this.mWebView);
            addView(mNaviBar);
            this.mWebView.getWvUIModel().setNaviBar(mNaviBar);
        }
        if (params.isShowLoading()) {
            this.mWebView.getWvUIModel().enableShowLoading();
        }
        if (!params.isJsbridgeEnabled()) {
            WVJsBridge.getInstance().setEnabled(false);
        }
    }

    public void loadUrl(String url) {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        this.mWebView.loadUrl(url);
    }

    public void loadUrl(String url, byte[] postData) {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        if (postData == null || postData.length == 0) {
            this.mWebView.loadUrl(url);
        } else {
            this.mWebView.postUrl(url, postData);
        }
    }

    public WVWebView getWebview() {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        return this.mWebView;
    }

    public void setErrorView(View view) {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        this.mWebView.getWvUIModel().setErrorView(view);
    }

    public void setLoadingView(View view) {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        this.mWebView.getWvUIModel().setLoadingView(view);
    }

    public void setNaviBar(AbstractNaviBar view) {
        if (!this.isInited) {
            initView((ParamsParcelable) null);
        }
        addView(view);
        this.mWebView.getWvUIModel().setNaviBar(view);
    }

    public void destroy() {
        if (this.isInited) {
            removeAllViews();
            this.mWebView.destroy();
            this.mWebView = null;
        }
        this.mContext = null;
    }
}
