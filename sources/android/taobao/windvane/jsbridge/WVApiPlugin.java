package android.taobao.windvane.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWebView;

public abstract class WVApiPlugin {
    public static final int REQUEST_MULTI_PICK_PHOTO = 4003;
    public static final int REQUEST_PICK_PHONE = 4003;
    public static final int REQUEST_PICK_PHOTO = 4002;
    public static final int REQUEST_TAKE_PHOTO = 4001;
    /* access modifiers changed from: protected */
    public boolean isAlive = true;
    /* access modifiers changed from: protected */
    public Context mContext;
    /* access modifiers changed from: protected */
    public IWVWebView mWebView;
    protected Object paramObj;

    public abstract boolean execute(String str, String str2, WVCallBackContext wVCallBackContext);

    public void initialize(Context context, IWVWebView webView) {
        initialize(context, webView, (Object) null);
    }

    @Deprecated
    public void initialize(Context context, WVWebView webView) {
        initialize(context, webView, (Object) null);
    }

    public void initialize(Context context, IWVWebView webView, Object paramObj2) {
        this.mContext = context;
        this.mWebView = webView;
        this.paramObj = paramObj2;
    }

    public void onDestroy() {
        this.isAlive = false;
    }

    public void onPause() {
        this.isAlive = false;
    }

    public void onResume() {
        this.isAlive = true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
    }
}
