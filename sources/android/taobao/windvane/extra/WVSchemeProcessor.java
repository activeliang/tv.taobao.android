package android.taobao.windvane.extra;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.text.TextUtils;
import anet.channel.strategy.StrategyCenter;

public class WVSchemeProcessor implements WVSchemeIntercepterInterface {
    public String dealUrlScheme(String url) {
        try {
            if (TextUtils.isEmpty(url) || url.startsWith("javascript:") || url.equals("about:blank")) {
                return url;
            }
            String awcnUrl = StrategyCenter.getInstance().getFormalizeUrl(url);
            if (!TextUtils.isEmpty(awcnUrl)) {
                return awcnUrl;
            }
            return url;
        } catch (Throwable th) {
            TaoLog.e("WVSchemeProcessor", "Can not dealUrlScheme : " + url);
            if (url.startsWith(WVUtils.URL_SEPARATOR)) {
                return url.replaceFirst(WVUtils.URL_SEPARATOR, "http://");
            }
            return url;
        }
    }
}
