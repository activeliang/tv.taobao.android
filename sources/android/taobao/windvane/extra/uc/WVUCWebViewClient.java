package android.taobao.windvane.extra.uc;

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
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jspatch.WVJsPatch;
import android.taobao.windvane.monitor.WVErrorMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPerformanceMonitorInterface;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.extension.UCExtension;
import java.lang.ref.WeakReference;
import java.util.Map;

public class WVUCWebViewClient extends WebViewClient {
    public static final String SCHEME_GEO = "geo:0,0?q=";
    public static final String SCHEME_MAILTO = "mailto:";
    public static final String SCHEME_TEL = "tel:";
    private static final String TAG = "WVUCWebViewClient";
    protected WeakReference<Context> mContext;

    public WVUCWebViewClient(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (WVMonitorService.getPerformanceMonitor() != null) {
            WVMonitorService.getPerformanceMonitor().didPageStartLoadAtTime(url, System.currentTimeMillis());
        }
        if (view instanceof WVUCWebView) {
            WVEventService.getInstance().onEvent(1001, (IWVWebView) view, url, favicon);
            ((WVUCWebView) view).onMessage(400, (Object) null);
            ((WVUCWebView) view).mPageStart = System.currentTimeMillis();
        }
        WVJsBridge.getInstance().tryToRunTailBridges();
        TaoLog.i(TAG, "onPageStarted : " + url);
    }

    public void onPageFinished(WebView view, String url) {
        TaoLog.i(TAG, "onPageFinished : " + url);
        final long mPageFinshTime = System.currentTimeMillis();
        super.onPageFinished(view, url);
        if (view instanceof WVUCWebView) {
            ((WVUCWebView) view).setCurrentUrl(url, "onPageFinished");
            ((WVUCWebView) view).onMessage(401, (Object) null);
        }
        if (view instanceof IWVWebView) {
            WVEventService.getInstance().onEvent(1002, (IWVWebView) view, url, new Object[0]);
            WVJsPatch.getInstance().execute((IWVWebView) view, url);
            ((IWVWebView) view).fireEvent("WindVaneReady", String.format("{'version':'%s'}", new Object[]{GlobalConfig.VERSION}));
        }
        if (WVMonitorService.getPerformanceMonitor() != null) {
            UCExtension ucExtension = view.getUCExtension();
            boolean isPageCache = false;
            if (ucExtension != null) {
                isPageCache = ucExtension.isLoadFromCachedPage();
            }
            WVMonitorService.getPerformanceMonitor().didGetPageStatusCode(url, -1, isPageCache ? 72 : WVUCWebView.getFromType(), (String) null, (String) null, (String) null, (Map<String, String>) null, (WVPerformanceMonitorInterface.NetStat) null);
        }
        final String str = url;
        ((WVUCWebView) view).evaluateJavascript("(function(p){if(!p||!p.timing)return;var t=p.timing,s=t.navigationStart,sc=t.secureConnectionStart,dc=t.domComplete,lee=t.loadEventEnd;return JSON.stringify({dns:t.domainLookupEnd-t.domainLookupStart,c:t.connectEnd-t.connectStart,scs:sc>0?sc-s:0,req:t.requestStart-s,rps:t.responseStart-s,rpe:t.responseEnd-s,dl:t.domLoading-s,dcl:t.domContentLoadedEventEnd-s,dc:dc>0?dc-s:0,lee:lee>0?lee-s:0})})(window.performance)", new ValueCallback<String>() {
            public void onReceiveValue(String value) {
                if (WVMonitorService.getPerformanceMonitor() != null) {
                    WVMonitorService.getPerformanceMonitor().didPagePerformanceInfo(str, value);
                    WVMonitorService.getPerformanceMonitor().didPageFinishLoadAtTime(str, mPageFinshTime);
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
        if (WVMonitorService.getErrorMonitor() != null) {
            WVErrorMonitorInterface errorMonitor = WVMonitorService.getErrorMonitor();
            if (curl != null) {
                failingUrl = curl;
            }
            errorMonitor.didOccurNativeError(failingUrl, errorCode, description);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (WVUrlUtil.isCommonUrl(url) && WVServerConfig.isBlackUrl(url)) {
            String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
            if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                ((WVUCWebView) view).onMessage(402, url);
            } else {
                view.loadUrl(forbiddenDomainRedirectURL);
            }
            return true;
        } else if ((view instanceof IWVWebView) && WVEventService.getInstance().onEvent(1003, (IWVWebView) view, url, new Object[0]).isSuccess) {
            return true;
        } else {
            Context cxt = (Context) this.mContext.get();
            if (url.startsWith(SCHEME_MAILTO) || url.startsWith(SCHEME_TEL)) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                    intent.setFlags(268435456);
                    cxt.startActivity(intent);
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
                    if (WVURLInterceptService.getWVURLIntercepter().shouldOverrideUrlLoading(cxt, (IWVWebView) view, url)) {
                        TaoLog.i(TAG, "intercept url : " + url);
                        return true;
                    }
                }
            } catch (Exception e2) {
                TaoLog.e(TAG, "shouldOverrideUrlLoading: doFilter error, " + e2.getMessage());
            }
            if (view instanceof WVUCWebView) {
                UCNetworkDelegate.getInstance().onUrlChange((WVUCWebView) view, url);
            }
            TaoLog.i(TAG, "shouldOverrideUrlLoading : " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00f3 A[SYNTHETIC, Splitter:B:29:0x00f3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.uc.webview.export.WebResourceResponse shouldInterceptRequest(com.uc.webview.export.WebView r20, java.lang.String r21) {
        /*
            r19 = this;
            long r16 = java.lang.System.currentTimeMillis()
            r0 = r20
            boolean r2 = r0 instanceof android.taobao.windvane.webview.IWVWebView
            if (r2 == 0) goto L_0x0065
            android.taobao.windvane.service.WVEventService r3 = android.taobao.windvane.service.WVEventService.getInstance()
            r4 = 1004(0x3ec, float:1.407E-42)
            r2 = r20
            android.taobao.windvane.webview.IWVWebView r2 = (android.taobao.windvane.webview.IWVWebView) r2
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r0 = r21
            android.taobao.windvane.service.WVEventResult r9 = r3.onEvent(r4, r2, r0, r5)
            boolean r2 = r9.isSuccess
            if (r2 == 0) goto L_0x0065
            java.lang.Object r2 = r9.resultObj
            if (r2 == 0) goto L_0x0065
            java.lang.Object r2 = r9.resultObj
            boolean r2 = r2 instanceof android.taobao.windvane.webview.WVWrapWebResourceResponse
            if (r2 == 0) goto L_0x0065
            java.lang.Object r0 = r9.resultObj
            r18 = r0
            android.taobao.windvane.webview.WVWrapWebResourceResponse r18 = (android.taobao.windvane.webview.WVWrapWebResourceResponse) r18
            boolean r2 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r2 == 0) goto L_0x0053
            java.lang.String r2 = "WVUCWebViewClient"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "预加载命中 : "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r21
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            android.taobao.windvane.util.TaoLog.d(r2, r3)
        L_0x0053:
            com.uc.webview.export.WebResourceResponse r14 = new com.uc.webview.export.WebResourceResponse
            r0 = r18
            java.lang.String r2 = r0.mMimeType
            r0 = r18
            java.lang.String r3 = r0.mEncoding
            r0 = r18
            java.io.InputStream r4 = r0.mInputStream
            r14.<init>(r2, r3, r4)
        L_0x0064:
            return r14
        L_0x0065:
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r2 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r2 == 0) goto L_0x007b
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r2 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            r4 = 0
            int r5 = android.taobao.windvane.extra.uc.WVUCWebView.getFromType()
            r6 = 0
            r7 = 0
            r3 = r21
            r2.didGetResourceStatusCode(r3, r4, r5, r6, r7)
        L_0x007b:
            android.taobao.windvane.cache.WVCacheManager r2 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r0 = r21
            boolean r2 = r2.isCacheEnabled(r0)
            if (r2 == 0) goto L_0x00c8
            java.lang.String r15 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r21)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            android.taobao.windvane.cache.WVCacheManager r3 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r4 = 1
            java.lang.String r3 = r3.getCacheDir(r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = java.io.File.separator
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r15)
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r13 = r2.toString()
            r14 = 0
            r11 = 0
            java.io.File r10 = new java.io.File     // Catch:{ Exception -> 0x00f0 }
            r10.<init>(r13)     // Catch:{ Exception -> 0x00f0 }
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00f0 }
            r12.<init>(r10)     // Catch:{ Exception -> 0x00f0 }
            com.uc.webview.export.WebResourceResponse r14 = new com.uc.webview.export.WebResourceResponse     // Catch:{ Exception -> 0x00f9 }
            java.lang.String r2 = "image/png"
            java.lang.String r3 = "UTF-8"
            r14.<init>(r2, r3, r12)     // Catch:{ Exception -> 0x00f9 }
            if (r14 != 0) goto L_0x0064
        L_0x00c8:
            boolean r2 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r2 == 0) goto L_0x00ea
            java.lang.String r2 = "WVUCWebViewClient"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "shouldInterceptRequest : "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r21
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            android.taobao.windvane.util.TaoLog.d(r2, r3)
        L_0x00ea:
            com.uc.webview.export.WebResourceResponse r14 = super.shouldInterceptRequest((com.uc.webview.export.WebView) r20, (java.lang.String) r21)
            goto L_0x0064
        L_0x00f0:
            r8 = move-exception
        L_0x00f1:
            if (r11 == 0) goto L_0x00c8
            r11.close()     // Catch:{ IOException -> 0x00f7 }
            goto L_0x00c8
        L_0x00f7:
            r2 = move-exception
            goto L_0x00c8
        L_0x00f9:
            r8 = move-exception
            r11 = r12
            goto L_0x00f1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebViewClient.shouldInterceptRequest(com.uc.webview.export.WebView, java.lang.String):com.uc.webview.export.WebResourceResponse");
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
