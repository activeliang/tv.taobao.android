package android.taobao.windvane.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.util.WVConstants;
import android.taobao.windvane.webview.ParamsParcelable;
import android.taobao.windvane.webview.WVViewController;
import android.taobao.windvane.webview.WVWebView;
import android.webkit.WebView;

@Deprecated
public abstract class BaseHybridActivity extends Activity implements Handler.Callback {
    protected Handler mHandler;
    protected WVViewController mViewController;
    protected WebView mWebView;
    protected byte[] postData = null;
    protected String url = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.url = intent.getStringExtra(WVConstants.INTENT_EXTRA_URL);
        this.postData = intent.getByteArrayExtra("DATA");
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.mViewController = new WVViewController(this);
        this.mViewController.init((ParamsParcelable) intent.getParcelableExtra(WVConstants.INTENT_EXTRA_PARAMS));
        this.mWebView = this.mViewController.getWebview();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mViewController.destroy();
        this.mViewController = null;
        super.onDestroy();
    }

    public boolean handleMessage(Message msg) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume();
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.mWebView != null && (this.mWebView instanceof WVWebView)) {
            ((WVWebView) this.mWebView).onActivityResult(requestCode, resultCode, data);
        }
    }
}
