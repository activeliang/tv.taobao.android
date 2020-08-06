package android.taobao.windvane.extra.uc;

import android.taobao.windvane.util.TaoLog;
import com.uc.webview.export.internal.interfaces.INetworkDecider;

public class AliNetworkDecider implements INetworkDecider {
    public int chooseNetwork(String url) {
        TaoLog.d("alinetwork", "AliNetworkDecider chooseNetwork :" + WVUCWebView.getUseTaobaoNetwork() + "url:" + url);
        return WVUCWebView.getUseTaobaoNetwork() ? 1 : 0;
    }
}
