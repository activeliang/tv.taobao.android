package android.taobao.windvane.webview;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jspatch.WVJsPatch;
import android.taobao.windvane.monitor.WVErrorMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WVWebViewClient extends WebViewClient {
    private static final String TAG = "WVWebViewClient";
    private String currentUrl = null;
    private boolean isAppcacheEnabled = false;
    protected Context mContext;
    /* access modifiers changed from: private */
    public long mPageFinshTime = 0;
    protected boolean openAllspdytake = false;

    public WVWebViewClient(Context context) {
        this.mContext = context;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (view instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1001, (IWVWebView) view, url, favicon);
        }
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "onPageStarted : " + url);
        }
        this.isAppcacheEnabled = false;
        this.currentUrl = url;
        ((WVWebView) view).onMessage(400, (Object) null);
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didPageStartLoadAtTime(url, System.currentTimeMillis());
        }
        WVJsBridge.getInstance().tryToRunTailBridges();
    }

    public void onPageFinished(WebView view, String url) {
        TaoLog.i(TAG, "onPageFinished : " + url);
        this.mPageFinshTime = System.currentTimeMillis();
        super.onPageFinished(view, url);
        if (view instanceof WVWebView) {
            ((WVWebView) view).setCurrentUrl(url, "onPageFinished");
        }
        if (view instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1002, (IWVWebView) view, url, new Object[0]);
            WVJsPatch.getInstance().execute((IWVWebView) view, url);
        }
        WVWebView webview = (WVWebView) view;
        if (TaoLog.getLogStatus()) {
            TaoLog.v(TAG, "Page finish: " + url);
        }
        webview.onMessage(401, (Object) null);
        webview.fireEvent("WindVaneReady", String.format("{'version':'%s'}", new Object[]{GlobalConfig.VERSION}));
        final String monitorUrl = url;
        webview.evaluateJavascript("(function(p){if(!p||!p.timing)return;var t=p.timing,s=t.navigationStart,sc=t.secureConnectionStart,dc=t.domComplete,lee=t.loadEventEnd;return JSON.stringify({dns:t.domainLookupEnd-t.domainLookupStart,c:t.connectEnd-t.connectStart,scs:sc>0?sc-s:0,req:t.requestStart-s,rps:t.responseStart-s,rpe:t.responseEnd-s,dl:t.domLoading-s,dcl:t.domContentLoadedEventEnd-s,dc:dc>0?dc-s:0,lee:lee>0?lee-s:0})})(window.performance)", new ValueCallback<String>() {
            public void onReceiveValue(String value) {
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    WVMonitorService.getPerformanceMonitor().didPagePerformanceInfo(monitorUrl, value);
                    WVMonitorService.getPerformanceMonitor().didPageFinishLoadAtTime(monitorUrl, WVWebViewClient.this.mPageFinshTime);
                }
            }
        });
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "Receive error, code: " + errorCode + "; desc: " + description + "; url: " + failingUrl);
        }
        if (view instanceof IWVWebView) {
            if (WVEventService.getInstance().onEvent(1005, (IWVWebView) view, failingUrl, Integer.valueOf(errorCode), description, failingUrl).isSuccess) {
                return;
            }
        }
        String curl = view.getUrl();
        if (curl == null || curl.equals(failingUrl)) {
            ((WVWebView) view).onMessage(402, failingUrl);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVErrorMonitorInterface errorMonitor = WVMonitorService.getErrorMonitor();
            if (curl != null) {
                failingUrl = curl;
            }
            errorMonitor.didOccurNativeError(failingUrl, errorCode, description);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!WVUrlUtil.isCommonUrl(url) || !WVServerConfig.isBlackUrl(url)) {
            if (TaoLog.getLogStatus()) {
                TaoLog.v(TAG, "shouldOverrideUrlLoading: " + url);
            }
            if ((view instanceof IWVWebView) && WVEventService.getInstance().onEvent(1003, (IWVWebView) view, url, new Object[0]).isSuccess) {
                return true;
            }
            if (url.startsWith(WVUCWebViewClient.SCHEME_MAILTO) || url.startsWith(WVUCWebViewClient.SCHEME_TEL)) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent.setFlags(268435456);
                    this.mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    TaoLog.e(TAG, "shouldOverrideUrlLoading: ActivityNotFoundException, url=" + url);
                }
                return true;
            }
            try {
                if ((view instanceof IWVWebView) && WVURLInterceptService.getWVURLIntercepter() != null && WVURLInterceptService.getWVURLIntercepter().isOpenURLIntercept()) {
                    if (WVURLInterceptService.getWVURLIntercepter().isNeedupdateURLRule(false)) {
                        WVURLInterceptService.getWVURLIntercepter().updateURLRule();
                    }
                    if (WVURLInterceptService.getWVURLIntercepter().shouldOverrideUrlLoading(this.mContext, (IWVWebView) view, url)) {
                        if (TaoLog.getLogStatus()) {
                            TaoLog.v(TAG, "intercept url: " + url);
                        }
                        return true;
                    }
                }
            } catch (Exception e2) {
                TaoLog.e(TAG, "shouldOverrideUrlLoading: doFilter error, " + e2.getMessage());
            }
            if (view instanceof WVWebView) {
                ((WVWebView) view).setCurrentUrl(url, "shouldOverrideUrlLoading");
            }
            TaoLog.i(TAG, "shouldOverrideUrlLoading : " + url);
            return false;
        }
        String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
        if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
            ((WVWebView) view).onMessage(402, url);
        } else {
            view.loadUrl(forbiddenDomainRedirectURL);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00f4 A[SYNTHETIC, Splitter:B:31:0x00f4] */
    @android.annotation.TargetApi(11)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.webkit.WebResourceResponse shouldInterceptRequest(android.webkit.WebView r17, java.lang.String r18) {
        /*
            r16 = this;
            r0 = r17
            boolean r1 = r0 instanceof android.taobao.windvane.webview.IWVWebView
            if (r1 == 0) goto L_0x0059
            android.taobao.windvane.service.WVEventService r2 = android.taobao.windvane.service.WVEventService.getInstance()
            r3 = 1004(0x3ec, float:1.407E-42)
            r1 = r17
            android.taobao.windvane.webview.IWVWebView r1 = (android.taobao.windvane.webview.IWVWebView) r1
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r0 = r18
            android.taobao.windvane.service.WVEventResult r8 = r2.onEvent(r3, r1, r0, r4)
            boolean r1 = r8.isSuccess
            if (r1 == 0) goto L_0x0059
            java.lang.Object r1 = r8.resultObj
            if (r1 == 0) goto L_0x0059
            java.lang.Object r1 = r8.resultObj
            boolean r1 = r1 instanceof android.taobao.windvane.webview.WVWrapWebResourceResponse
            if (r1 == 0) goto L_0x0059
            java.lang.Object r15 = r8.resultObj
            android.taobao.windvane.webview.WVWrapWebResourceResponse r15 = (android.taobao.windvane.webview.WVWrapWebResourceResponse) r15
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r1 == 0) goto L_0x004d
            java.lang.String r1 = "WVWebViewClient"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "预加载命中 : "
            java.lang.StringBuilder r2 = r2.append(r3)
            r0 = r18
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r1, r2)
        L_0x004d:
            android.webkit.WebResourceResponse r13 = new android.webkit.WebResourceResponse
            java.lang.String r1 = r15.mMimeType
            java.lang.String r2 = r15.mEncoding
            java.io.InputStream r3 = r15.mInputStream
            r13.<init>(r1, r2, r3)
        L_0x0058:
            return r13
        L_0x0059:
            r0 = r16
            boolean r1 = r0.isAppcacheEnabled
            if (r1 != 0) goto L_0x006f
            java.lang.String r1 = ".manifest"
            r0 = r18
            boolean r1 = r0.endsWith(r1)
            if (r1 == 0) goto L_0x006f
            r1 = 1
            r0 = r16
            r0.isAppcacheEnabled = r1
        L_0x006f:
            android.taobao.windvane.cache.WVCacheManager r1 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r0 = r18
            boolean r1 = r1.isCacheEnabled(r0)
            if (r1 == 0) goto L_0x00bc
            java.lang.String r14 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r18)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            android.taobao.windvane.cache.WVCacheManager r2 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r3 = 1
            java.lang.String r2 = r2.getCacheDir(r3)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = java.io.File.separator
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r14)
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r12 = r1.toString()
            r13 = 0
            r10 = 0
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x00f1 }
            r9.<init>(r12)     // Catch:{ Exception -> 0x00f1 }
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00f1 }
            r11.<init>(r9)     // Catch:{ Exception -> 0x00f1 }
            android.webkit.WebResourceResponse r13 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x00fa }
            java.lang.String r1 = "image/png"
            java.lang.String r2 = "UTF-8"
            r13.<init>(r1, r2, r11)     // Catch:{ Exception -> 0x00fa }
            if (r13 != 0) goto L_0x0058
        L_0x00bc:
            java.lang.String r1 = "WVWebViewClient"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "shouldInterceptRequest : "
            java.lang.StringBuilder r2 = r2.append(r3)
            r0 = r18
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.taobao.windvane.util.TaoLog.i(r1, r2)
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r1 == 0) goto L_0x00eb
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            r3 = 0
            r4 = 1
            r5 = 0
            r6 = 0
            r2 = r18
            r1.didGetResourceStatusCode(r2, r3, r4, r5, r6)
        L_0x00eb:
            android.webkit.WebResourceResponse r13 = super.shouldInterceptRequest(r17, r18)
            goto L_0x0058
        L_0x00f1:
            r7 = move-exception
        L_0x00f2:
            if (r10 == 0) goto L_0x00bc
            r10.close()     // Catch:{ IOException -> 0x00f8 }
            goto L_0x00bc
        L_0x00f8:
            r1 = move-exception
            goto L_0x00bc
        L_0x00fa:
            r7 = move-exception
            r10 = r11
            goto L_0x00f2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.webview.WVWebViewClient.shouldInterceptRequest(android.webkit.WebView, java.lang.String):android.webkit.WebResourceResponse");
    }

    @SuppressLint({"NewApi"})
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        String errorInfo = error.toString();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "onReceivedSslError  url: " + error.getUrl() + "errorMsg:" + errorInfo);
        }
        String curl = view.getUrl();
        if (view instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1006, (IWVWebView) view, curl, errorInfo);
        }
        if (WVMonitorService.getErrorMonitor() != null) {
            WVMonitorService.getErrorMonitor().didOccurNativeError(curl, 1006, errorInfo);
        }
        super.onReceivedSslError(view, handler, error);
    }
}
