package android.taobao.windvane.jsbridge.api;

import android.content.Context;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.api.WVCamera;
import android.taobao.windvane.webview.IWVWebView;

public abstract class WVUploadService {
    protected Context mContext;
    protected IWVWebView mWebView;

    public abstract void doUpload(WVCamera.UploadParams uploadParams, WVCallBackContext wVCallBackContext);

    public void initialize(Context context, IWVWebView webView) {
        this.mContext = context;
        this.mWebView = webView;
    }
}
