package android.taobao.windvane.service;

import android.content.Context;
import android.taobao.windvane.webview.IWVWebView;

public class WVEventContext {
    public Context context = null;
    public String url = null;
    public IWVWebView webView = null;

    public WVEventContext() {
    }

    public WVEventContext(IWVWebView webView2, String url2) {
        this.webView = webView2;
        this.url = url2;
    }
}
