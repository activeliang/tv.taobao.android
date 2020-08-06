package com.ali.user.open.core.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.util.AttributeSet;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.core.util.DialogHelper;
import com.bftv.fui.constantplugin.Constant;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;

@SuppressLint({"SetJavaScriptEnabled"})
public class MemberUCWebView extends WVUCWebView {
    private static final String TAG = MemberUCWebView.class.getSimpleName();
    private String appCacheDir;
    protected boolean firstAlert = true;
    private Context mContext;
    protected boolean proceed = false;

    public MemberUCWebView(Context context) {
        super(context);
        this.mContext = context;
        initSettings(context);
    }

    public MemberUCWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
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
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(false);
        try {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        final IWebViewClient webViewClient = (IWebViewClient) context;
        setWebViewClient(new WVUCWebViewClient(context) {
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

            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                if (MemberUCWebView.this.firstAlert) {
                    DialogInterface.OnClickListener postiveListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            handler.proceed();
                            MemberUCWebView.this.proceed = true;
                        }
                    };
                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            handler.cancel();
                            MemberUCWebView.this.proceed = false;
                        }
                    };
                    if (view.getContext() instanceof Activity) {
                        DialogHelper.getInstance().alert((Activity) view.getContext(), "SSL证书错误", "证书错误. 是否继续访问?", Constant.OK, postiveListener, "取消", cancelListener);
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setPositiveButton(Constant.OK, postiveListener);
                    builder.setNeutralButton("取消", cancelListener);
                    try {
                        AlertDialog ad = builder.create();
                        ad.setTitle("SSL证书错误");
                        ad.setMessage("证书错误. 是否继续访问?");
                        ad.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (MemberUCWebView.this.proceed) {
                    handler.proceed();
                } else {
                    super.onReceivedSslError(view, handler, error);
                }
            }
        });
        setWebChromeClient(new WVUCWebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webViewClient.onReceivedTitle(title);
            }
        });
    }
}
