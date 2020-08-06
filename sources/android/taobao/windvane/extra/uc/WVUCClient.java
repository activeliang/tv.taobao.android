package android.taobao.windvane.extra.uc;

import android.taobao.windvane.monitor.AppMonitorUtil;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.UCClient;

public class WVUCClient extends UCClient {
    public void onWebViewEvent(WebView webView, int i, Object o) {
        if (i == 9) {
            try {
                AppMonitorUtil.commitEmptyPage(webView.getUrl(), ((Integer) o).toString());
            } catch (Throwable th) {
            }
        }
        super.onWebViewEvent(webView, i, o);
    }
}
