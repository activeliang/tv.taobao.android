package android.taobao.windvane.webview;

import android.content.Context;
import android.view.View;
import java.util.concurrent.ConcurrentHashMap;

public interface IWVWebView {
    public static final ConcurrentHashMap<String, Integer> JsbridgeHis = new ConcurrentHashMap<>();

    void addJsObject(String str, Object obj);

    boolean back();

    void clearCache();

    void evaluateJavascript(String str);

    void fireEvent(String str, String str2);

    Context getContext();

    String getDataOnActive();

    Object getJsObject(String str);

    String getUrl();

    String getUserAgentString();

    View getView();

    void hideLoadingView();

    void loadUrl(String str);

    void refresh();

    void setDataOnActive(String str);

    void setUserAgentString(String str);

    void showLoadingView();
}
