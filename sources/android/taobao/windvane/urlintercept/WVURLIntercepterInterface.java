package android.taobao.windvane.urlintercept;

import android.content.Context;
import android.taobao.windvane.webview.IWVWebView;

public interface WVURLIntercepterInterface {
    boolean isNeedupdateURLRule(boolean z);

    boolean isOpenURLIntercept();

    boolean shouldOverrideUrlLoading(Context context, IWVWebView iWVWebView, String str);

    void updateURLRule();
}
