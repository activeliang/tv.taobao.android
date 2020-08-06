package android.taobao.windvane.filter;

import android.annotation.SuppressLint;
import android.taobao.windvane.service.WVWebViewClientFilter;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.text.TextUtils;
import java.io.InputStream;

public class WVSecurityFilter extends WVWebViewClientFilter {
    @SuppressLint({"NewApi", "DefaultLocale"})
    public WVWrapWebResourceResponse shouldInterceptRequest(IWVWebView view, String url) {
        if (TaoLog.getLogStatus()) {
            TaoLog.d("WVSecurityFilter", "WVSecurityFilter shouldInterceptRequest url =" + url);
        }
        if (TextUtils.isEmpty(url) || url.length() <= 6 || !url.substring(0, 7).toLowerCase().startsWith("file://")) {
            return super.shouldInterceptRequest(view, url);
        }
        return new WVWrapWebResourceResponse("", "utf-8", (InputStream) null);
    }
}
