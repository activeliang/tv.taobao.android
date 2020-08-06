package android.taobao.windvane.extra.uc;

import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UCNetworkDelegate implements Handler.Callback {
    public static final int CHANGE_WEBVIEW_URL = 276;
    private static final int RECEIVE_RESPONSE_CODE = 274;
    private static final int REMOVE_WEBVIEW_CODE = 275;
    private static final int SEND_REQUEST_CODE = 273;
    private static final String TAG = "UCNetworkDelegate";
    private static UCNetworkDelegate instance = new UCNetworkDelegate();
    private Handler mHandler = new WVThread("Windvane", (Handler.Callback) this).getHandler();
    private ConcurrentHashMap<WeakReference<WVUCWebView>, String> mWebViewsInfoMap = new ConcurrentHashMap<>();

    public static synchronized UCNetworkDelegate getInstance() {
        UCNetworkDelegate uCNetworkDelegate;
        synchronized (UCNetworkDelegate.class) {
            uCNetworkDelegate = instance;
        }
        return uCNetworkDelegate;
    }

    private UCNetworkDelegate() {
    }

    public ConcurrentHashMap<WeakReference<WVUCWebView>, String> getWebViews() {
        return this.mWebViewsInfoMap;
    }

    public void removeWebview(WVUCWebView webView) {
        Message message = this.mHandler.obtainMessage();
        message.what = REMOVE_WEBVIEW_CODE;
        message.obj = webView;
        this.mHandler.sendMessage(message);
    }

    public void onUrlChange(WVUCWebView webView, String url) {
        Message message = this.mHandler.obtainMessage();
        message.what = CHANGE_WEBVIEW_URL;
        ArrayList objects = new ArrayList();
        objects.add(webView);
        objects.add(url);
        message.obj = objects;
        this.mHandler.sendMessage(message);
    }

    public void onSendRequest(Map<String, String> header, String url) {
        String str;
        if (this.mWebViewsInfoMap != null && header != null && url != null) {
            Message message = this.mHandler.obtainMessage();
            message.what = SEND_REQUEST_CODE;
            Hashtable<String, String> requestTable = new Hashtable<>();
            requestTable.put("url", url);
            String referrer = header.get("Referer");
            if (TextUtils.isEmpty(referrer)) {
                str = "";
            } else {
                str = referrer;
            }
            requestTable.put("referrer", str);
            requestTable.put("start", String.valueOf(System.currentTimeMillis()));
            message.obj = requestTable;
            TaoLog.d(TAG, "onSendRequest : " + url + " Referer: " + referrer);
            this.mHandler.sendMessage(message);
        }
    }

    public void onFinish(int code, String url) {
        if (this.mWebViewsInfoMap != null && url != null) {
            Message message = this.mHandler.obtainMessage();
            message.what = RECEIVE_RESPONSE_CODE;
            Hashtable<String, String> requestTable = new Hashtable<>();
            requestTable.put("url", url);
            String statusCode = String.valueOf(code);
            requestTable.put("statusCode", statusCode);
            requestTable.put("end", String.valueOf(System.currentTimeMillis()));
            message.obj = requestTable;
            TaoLog.d(TAG, "onFinish : " + url + " statusCode: " + statusCode);
            this.mHandler.sendMessage(message);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SEND_REQUEST_CODE /*273*/:
                dealSendRequest(msg.obj);
                return false;
            case RECEIVE_RESPONSE_CODE /*274*/:
                dealReceiveResponse(msg.obj);
                return false;
            case REMOVE_WEBVIEW_CODE /*275*/:
                dealRemoveWebView(msg.obj);
                break;
            case CHANGE_WEBVIEW_URL /*276*/:
                break;
            default:
                return false;
        }
        dealUrlChange(msg.obj);
        return false;
    }

    private void dealRemoveWebView(Object obj) {
        if ((obj instanceof WVUCWebView) && this.mWebViewsInfoMap != null) {
            Iterator<WeakReference<WVUCWebView>> iterator = this.mWebViewsInfoMap.keySet().iterator();
            while (iterator.hasNext()) {
                WVUCWebView webView = (WVUCWebView) iterator.next().get();
                if (webView != null && obj.equals(webView)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private void dealUrlChange(Object object) {
        if (object instanceof ArrayList) {
            ArrayList objectList = (ArrayList) object;
            if ((objectList.get(0) instanceof WVUCWebView) && (objectList.get(1) instanceof String)) {
                WVUCWebView view = (WVUCWebView) objectList.get(0);
                String tempUrl = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme((String) objectList.get(1)));
                for (WeakReference<WVUCWebView> reference : this.mWebViewsInfoMap.keySet()) {
                    WVUCWebView webView = (WVUCWebView) reference.get();
                    if (webView != null && view.equals(webView)) {
                        webView.clearH5MonitorData();
                        this.mWebViewsInfoMap.put(reference, tempUrl);
                        return;
                    }
                }
                this.mWebViewsInfoMap.put(new WeakReference(view), tempUrl);
            }
        }
    }

    private void dealSendRequest(Object obj) {
        if (obj instanceof Hashtable) {
            Hashtable<String, String> table = (Hashtable) obj;
            String url = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme(table.get("url")));
            String referrer = WVUrlUtil.removeScheme(table.get("referrer"));
            if (!TextUtils.isEmpty(referrer)) {
                if (this.mWebViewsInfoMap.containsValue(referrer)) {
                    for (WeakReference<WVUCWebView> reference : this.mWebViewsInfoMap.keySet()) {
                        if (reference.get() != null && this.mWebViewsInfoMap.get(reference).equals(referrer)) {
                            assembleRequestData(table, url, referrer, reference);
                            return;
                        }
                    }
                    return;
                }
                for (WeakReference<WVUCWebView> reference2 : this.mWebViewsInfoMap.keySet()) {
                    if (reference2.get() != null && ((WVUCWebView) reference2.get()).containsH5MonitorData(referrer)) {
                        assembleRequestData(table, url, referrer, reference2);
                        return;
                    }
                }
            } else if (this.mWebViewsInfoMap.containsValue(url)) {
                for (WeakReference<WVUCWebView> reference3 : this.mWebViewsInfoMap.keySet()) {
                    if (reference3.get() != null && this.mWebViewsInfoMap.get(reference3).equals(url)) {
                        assembleRequestData(table, url, referrer, reference3);
                        return;
                    }
                }
            }
        }
    }

    private void assembleRequestData(Hashtable<String, String> table, String url, String referrer, WeakReference<WVUCWebView> reference) {
        String str;
        WVUCWebView webView = (WVUCWebView) reference.get();
        if (webView != null) {
            if (TextUtils.isEmpty(url)) {
                str = "";
            } else {
                str = url;
            }
            webView.insertH5MonitorData(url, "url", str);
            if (TextUtils.isEmpty(referrer)) {
                referrer = "";
            }
            webView.insertH5MonitorData(url, "referrer", referrer);
            webView.insertH5MonitorData(url, "start", String.valueOf(Long.parseLong(table.get("start")) - webView.mPageStart));
        }
    }

    private void dealReceiveResponse(Object obj) {
        if (obj instanceof Hashtable) {
            Hashtable<String, String> table = (Hashtable) obj;
            String url = WVUrlUtil.removeHashCode(WVUrlUtil.removeScheme(table.get("url")));
            for (WeakReference<WVUCWebView> key : this.mWebViewsInfoMap.keySet()) {
                String currentUrl = this.mWebViewsInfoMap.get(key);
                if (currentUrl != null && currentUrl.contains(url)) {
                    assembleResponseData(table, url, key);
                    return;
                }
            }
            for (WeakReference<WVUCWebView> key2 : this.mWebViewsInfoMap.keySet()) {
                WVUCWebView webView = (WVUCWebView) key2.get();
                if (webView != null && webView.containsH5MonitorData(url)) {
                    assembleResponseData(table, url, key2);
                    return;
                }
            }
        }
    }

    private void assembleResponseData(Hashtable<String, String> table, String url, WeakReference<WVUCWebView> reference) {
        WVUCWebView webView = (WVUCWebView) reference.get();
        if (webView != null) {
            webView.insertH5MonitorData(url, "statusCode", table.get("statusCode"));
            webView.insertH5MonitorData(url, "end", String.valueOf(Long.parseLong(table.get("end")) - webView.mPageStart));
        }
    }
}
