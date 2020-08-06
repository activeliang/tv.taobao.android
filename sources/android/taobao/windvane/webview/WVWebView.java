package android.taobao.windvane.webview;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.filter.WVSecurityFilter;
import android.taobao.windvane.jsbridge.WVAppEvent;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVPluginEntryManager;
import android.taobao.windvane.jspatch.WVJsPatchListener;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.view.PopupWindowController;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.util.Map;

public class WVWebView extends WebView implements Handler.Callback, IWVWebView {
    private static final String TAG = "WVWebView";
    private static boolean evaluateJavascriptSupported = (Build.VERSION.SDK_INT >= 19);
    private final String WVURL_SUFFIX = "?wvFackUrlState=";
    public String bizCode = "";
    protected Context context;
    private String currentUrl = null;
    private String dataOnActive = null;
    float dx;
    float dy;
    protected WVPluginEntryManager entryManager;
    protected boolean isAlive;
    boolean isUser = true;
    private WVJsPatchListener jsPatchListener = null;
    /* access modifiers changed from: private */
    public boolean longPressSaveImage = true;
    SparseArray<MotionEvent> mEventSparseArray = new SparseArray<>();
    protected Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mImageUrl;
    private View.OnLongClickListener mLongClickListener = null;
    /* access modifiers changed from: private */
    public View.OnClickListener mPopupClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (WVWebView.this.mPopupMenuTags != null && WVWebView.this.mPopupMenuTags.length > 0 && WVWebView.this.mPopupMenuTags[0].equals(v.getTag())) {
                try {
                    PermissionProposer.buildPermissionTask(WVWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                        public void run() {
                            ImageTool.saveImageToDCIM(WVWebView.this.context.getApplicationContext(), WVWebView.this.mImageUrl, WVWebView.this.mHandler);
                        }
                    }).setTaskOnPermissionDenied(new Runnable() {
                        public void run() {
                            WVWebView.this.mHandler.sendEmptyMessage(405);
                        }
                    }).execute();
                } catch (Exception e) {
                }
            }
            WVWebView.this.mPopupController.hide();
        }
    };
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags = {"保存到相册"};
    private int mWvNativeCallbackId = 1000;
    private long onErrorTime = 0;
    protected boolean supportDownload = true;
    protected WVWebChromeClient webChromeClient;
    protected WVWebViewClient webViewClient;
    private WVSecurityFilter wvSecurityFilter = null;
    private boolean wvSupportFileSchema = EnvUtil.isDebug();
    private boolean wvSupportNativeJs = false;
    private WVUIModel wvUIModel = null;

    public WVWebView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        this.context = ctx;
        init();
    }

    public WVWebView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.context = ctx;
        init();
    }

    public WVWebView(Context ctx) {
        super(ctx);
        this.context = ctx;
        init();
    }

    public int getContentHeight() {
        return (int) (((float) super.getContentHeight()) * super.getScale());
    }

    public void loadData(String data, String mimeType, String encoding) {
        if (this.isAlive) {
            super.loadData(data, mimeType, encoding);
        }
    }

    public void reload() {
        super.reload();
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
        if (this.isAlive) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "loadDataWithBaseURL: baseUrl=" + baseUrl);
            }
            super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
        }
    }

    public void loadUrl(String url) {
        if (!WVUrlUtil.isCommonUrl(url) || !WVServerConfig.isBlackUrl(url)) {
            WVEventService.getInstance().onEvent(WVEventId.WEBVIEW_LOADURL);
            if (this.isAlive && url != null) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "loadUrl: url=" + url);
                }
                WVSchemeIntercepterInterface schemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
                if (schemeIntercepter != null) {
                    url = schemeIntercepter.dealUrlScheme(url);
                }
                try {
                    super.loadUrl(url);
                } catch (Exception e) {
                    TaoLog.e(TAG, e.getMessage());
                }
            }
        } else {
            String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
            if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                onMessage(402, url);
                return;
            }
            try {
                super.loadUrl(forbiddenDomainRedirectURL);
            } catch (Exception e2) {
                TaoLog.e(TAG, e2.getMessage());
            }
        }
    }

    public void refresh() {
        reload();
    }

    public void superLoadUrl(String url) {
        if (this.isAlive) {
            super.loadUrl(url);
        }
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (this.isAlive && url != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "loadUrl with headers: url=" + url);
            }
            super.loadUrl(url, additionalHttpHeaders);
        }
    }

    public void postUrl(String url, byte[] postData) {
        if (this.isAlive && url != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "postUrl: url=" + url);
            }
            super.postUrl(url, postData);
        }
    }

    private void init() {
        if (EnvUtil.isDebug()) {
            WVEventService.getInstance().onEvent(WVEventId.WEBVIEW_ONCREATE);
        }
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.webViewClient = new WVWebViewClient(this.context);
        super.setWebViewClient(this.webViewClient);
        this.webChromeClient = new WVWebChromeClient(this.context);
        super.setWebChromeClient(this.webChromeClient);
        setVerticalScrollBarEnabled(false);
        requestFocus();
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        WVRenderPolicy.disableAccessibility(this.context);
        WebSettings setting = getSettings();
        setting.setAllowFileAccess(true);
        setting.setJavaScriptEnabled(true);
        String apptag = GlobalConfig.getInstance().getAppTag();
        String appversion = GlobalConfig.getInstance().getAppVersion();
        if (!TextUtils.isEmpty(apptag) && !TextUtils.isEmpty(appversion)) {
            setting.setUserAgentString(setting.getUserAgentString() + " AliApp(" + apptag + WVNativeCallbackUtil.SEPERATER + appversion + ")");
        }
        setting.setUserAgentString(setting.getUserAgentString() + GlobalConfig.DEFAULT_UA);
        setting.setCacheMode(-1);
        if (Build.VERSION.SDK_INT >= 5) {
            setting.setDatabaseEnabled(true);
            String dbPath = "/data/data/" + this.context.getPackageName() + "/databases";
            setting.setDatabasePath(dbPath);
            setting.setGeolocationEnabled(true);
            setting.setGeolocationDatabasePath(dbPath);
        }
        if (Build.VERSION.SDK_INT >= 7) {
            setting.setDomStorageEnabled(true);
            setting.setAppCacheEnabled(true);
            if (!(this.context == null || this.context.getCacheDir() == null)) {
                setting.setAppCachePath(this.context.getCacheDir().getAbsolutePath());
            }
        }
        if (Build.VERSION.SDK_INT < 18) {
            setting.setSavePassword(false);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            setting.setTextZoom(100);
        } else {
            setting.setTextSize(WebSettings.TextSize.NORMAL);
        }
        if (TaoLog.getLogStatus() && Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WVJsBridge.getInstance().init();
        this.entryManager = new WVPluginEntryManager(this.context, this);
        WVAppEvent event = new WVAppEvent();
        event.initialize(this.context, this);
        addJsObject("AppEvent", event);
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        this.wvSecurityFilter = new WVSecurityFilter();
        WVEventService.getInstance().addEventListener(this.wvSecurityFilter, WVEventService.WV_FORWARD_EVENT);
        this.jsPatchListener = new WVJsPatchListener(this);
        WVEventService.getInstance().addEventListener(this.jsPatchListener, WVEventService.WV_BACKWARD_EVENT);
        if (Build.VERSION.SDK_INT > 15) {
            try {
                ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService("clipboard");
                if (clipboard != null) {
                    ClipData clip = clipboard.getPrimaryClip();
                    if (clip == null) {
                        clipboard.setPrimaryClip(ClipData.newPlainText("初始化", ""));
                    } else if ("intent:#Intent;S.K_1171477665=;end".equals(clip.getItemAt(0).coerceToText(this.context).toString())) {
                        clipboard.setPrimaryClip(ClipData.newPlainText("初始化", ""));
                    }
                }
            } catch (Exception e2) {
            }
        }
        this.wvUIModel = new WVUIModel(this.context, this);
        this.mLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = null;
                try {
                    result = WVWebView.this.getHitTestResult();
                } catch (Exception e) {
                }
                if (result == null || !WVWebView.this.longPressSaveImage) {
                    return false;
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(WVWebView.TAG, "Long click on WebView, " + result.getExtra());
                }
                if (result.getType() != 8 && result.getType() != 5) {
                    return false;
                }
                String unused = WVWebView.this.mImageUrl = result.getExtra();
                PopupWindowController unused2 = WVWebView.this.mPopupController = new PopupWindowController(WVWebView.this.context, WVWebView.this, WVWebView.this.mPopupMenuTags, WVWebView.this.mPopupClickListener);
                WVWebView.this.mPopupController.show();
                return true;
            }
        };
        setOnLongClickListener(this.mLongClickListener);
        setDownloadListener(new WVDownLoadListener());
        WVTweakWebCoreHandler.tryTweakWebCoreHandler();
        this.isAlive = true;
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPerformanceMonitor().didWebViewInitAtTime(System.currentTimeMillis());
        }
        if (Build.VERSION.SDK_INT >= 11 && WVRenderPolicy.shouldDisableHardwareRenderInLayer()) {
            try {
                setLayerType(1, (Paint) null);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    public void setWebViewClient(WebViewClient client) {
        if (client instanceof WVWebViewClient) {
            this.webViewClient = (WVWebViewClient) client;
            super.setWebViewClient(client);
            return;
        }
        throw new WindVaneError("Your WebViewClient must be extended from WVWebViewClient");
    }

    public void setWebChromeClient(WebChromeClient client) {
        if (client instanceof WVWebChromeClient) {
            this.webChromeClient = (WVWebChromeClient) client;
            super.setWebChromeClient(client);
            return;
        }
        throw new WindVaneError("Your WebChromeClient must be extended from WVWebChromeClient");
    }

    public void destroy() {
        if (this.isAlive) {
            this.isAlive = false;
            super.setWebViewClient((WebViewClient) null);
            super.setWebChromeClient((WebChromeClient) null);
            this.webViewClient = null;
            this.webChromeClient = null;
            WVJsBridge.getInstance().tryToRunTailBridges();
            this.entryManager.onDestroy();
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
                this.mHandler = null;
            }
            WVEventService.getInstance().onEvent(WVEventId.PAGE_destroy);
            WVEventService.getInstance().removeEventListener(this.wvSecurityFilter);
            WVEventService.getInstance().removeEventListener(this.jsPatchListener);
            removeAllViews();
            this.mPopupController = null;
            this.mPopupClickListener = null;
            this.mLongClickListener = null;
            setOnLongClickListener((View.OnLongClickListener) null);
            if (JsbridgeHis != null) {
                JsbridgeHis.clear();
            }
            try {
                super.destroy();
            } catch (Exception e) {
            }
        }
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public WVUIModel getWvUIModel() {
        return this.wvUIModel;
    }

    public void onMessage(int id, Object data) {
        if (this.mHandler != null) {
            Message msg = Message.obtain();
            msg.what = id;
            msg.obj = data;
            this.mHandler.sendMessage(msg);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 400:
                this.wvUIModel.showLoadingView();
                this.wvUIModel.switchNaviBar(1);
                return true;
            case 401:
                this.wvUIModel.hideLoadingView();
                this.wvUIModel.resetNaviBar();
                if (this.onErrorTime == 0 || System.currentTimeMillis() - this.onErrorTime <= 3000) {
                    return true;
                }
                this.wvUIModel.hideErrorPage();
                return true;
            case 402:
                this.wvUIModel.loadErrorPage();
                this.onErrorTime = System.currentTimeMillis();
                return true;
            case 403:
                this.wvUIModel.hideLoadingView();
                return true;
            case 404:
                Toast.makeText(this.context, "图片保存到相册成功", 1).show();
                return true;
            case 405:
                Toast.makeText(this.context, "图片保存到相册失败", 1).show();
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.entryManager != null) {
            this.entryManager.onScrollChanged(l, t, oldl, oldt);
        }
        try {
            super.onScrollChanged(l, t, oldl, oldt);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(11)
    public void onPause() {
        if (this.entryManager != null) {
            this.entryManager.onPause();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            super.onPause();
        }
        WVEventService.getInstance().onEvent(WVEventId.PAGE_onPause);
    }

    public void pauseTimers() {
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "You  must be careful  to Call pauseTimers ,It's Global");
        }
    }

    public void resumeTimers() {
        super.resumeTimers();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "You  must be careful  to Call resumeTimers ,It's Global");
        }
    }

    @TargetApi(11)
    public void onResume() {
        if (this.entryManager != null) {
            this.entryManager.onResume();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            super.onResume();
        }
        WVEventService.getInstance().onEvent(WVEventId.PAGE_onResume);
    }

    public Handler getWVHandler() {
        return this.mHandler;
    }

    class WVDownLoadListener implements DownloadListener {
        WVDownLoadListener() {
        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVWebView.TAG, "Download start, url: " + url + " contentDisposition: " + contentDisposition + " mimetype: " + mimetype + " contentLength: " + contentLength);
            }
            if (!WVWebView.this.supportDownload) {
                TaoLog.w(WVWebView.TAG, "DownloadListener is not support for webview.");
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.setFlags(268435456);
            try {
                WVWebView.this.context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(WVWebView.this.context, "对不起，您的设备找不到相应的程序", 1).show();
                TaoLog.e(WVWebView.TAG, "DownloadListener not found activity to open this url.");
            }
        }
    }

    public void setSupportDownload(boolean support) {
        this.supportDownload = support;
    }

    public void setSupportFileSchema(boolean support) {
        this.wvSupportFileSchema = support;
    }

    public boolean isSupportFileSchema() {
        return this.wvSupportFileSchema;
    }

    public void addJavascriptInterface(Object object, String name) {
        if (this.wvSupportNativeJs || Build.VERSION.SDK_INT >= 17) {
            super.addJavascriptInterface(object, name);
        } else {
            TaoLog.e(TAG, "addJavascriptInterface is disabled before API level 17 for security reason.");
        }
    }

    public void supportJavascriptInterface(boolean support) {
        this.wvSupportNativeJs = support;
    }

    public void addJsObject(String name, Object instance) {
        if (this.entryManager != null) {
            this.entryManager.addEntry(name, instance);
        }
    }

    public Object getJsObject(String name) {
        if (this.entryManager == null) {
            return null;
        }
        return this.entryManager.getEntry(name);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (this.entryManager != null) {
            this.entryManager.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void script2NativeCallback(String doScript, ValueCallback<String> callback) {
        int id = this.mWvNativeCallbackId + 1;
        this.mWvNativeCallbackId = id;
        WVNativeCallbackUtil.putNativeCallbak(String.valueOf(id), callback);
        loadUrl("javascript:console.log('wvNativeCallback/" + id + "/'+function(){var s = " + doScript + "; return (typeof s === 'object' ? JSON.stringify(s) : typeof s === 'string' ? '\"' + s + '\"' : s);}())");
    }

    public void evaluateJavascript(String script) {
        evaluateJavascript(script, (ValueCallback<String>) null);
    }

    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        if (script != null && script.length() > 10 && "javascript:".equals(script.substring(0, 11).toLowerCase())) {
            script = script.substring(11);
        }
        if (evaluateJavascriptSupported) {
            try {
                super.evaluateJavascript(script, resultCallback);
            } catch (NoSuchMethodError e) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(script, resultCallback);
            } catch (IllegalStateException e2) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(script, resultCallback);
            }
        } else if (resultCallback == null) {
            loadUrl("javascript:" + script);
        } else {
            script2NativeCallback(script, resultCallback);
        }
    }

    public void openLongPressSaveImage() {
        this.longPressSaveImage = true;
    }

    public void closeLongPressSaveImage() {
        this.longPressSaveImage = false;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public String getCurrentUrl() {
        String url = super.getUrl();
        if (url == null) {
            TaoLog.v(TAG, "getUrl by currentUrl: " + this.currentUrl);
            return this.currentUrl;
        }
        TaoLog.v(TAG, "getUrl by webview: " + url);
        return url;
    }

    public void setCurrentUrl(String url, String state) {
        this.currentUrl = url;
        TaoLog.v(TAG, "setCurrentUrl: " + url + " state : " + state);
    }

    public boolean canGoBack() {
        if (WVEventService.getInstance().onEvent(WVEventId.PAGE_back).isSuccess) {
            return false;
        }
        return super.canGoBack();
    }

    public boolean back() {
        if (!canGoBack()) {
            return false;
        }
        goBack();
        return true;
    }

    public void setDataOnActive(String data) {
        this.dataOnActive = data;
    }

    public String getDataOnActive() {
        return this.dataOnActive;
    }

    public void fireEvent(String event) {
        fireEvent(event, "{}");
    }

    public void fireEvent(String event, String data) {
        getWVCallBackContext().fireEvent(event, data);
    }

    @Deprecated
    public WVCallBackContext getWVCallBackContext() {
        return new WVCallBackContext(this);
    }

    public View getView() {
        return this;
    }

    public void showLoadingView() {
        if (this.wvUIModel != null) {
            this.wvUIModel.showLoadingView();
        }
    }

    public void hideLoadingView() {
        if (this.wvUIModel != null) {
            this.wvUIModel.hideLoadingView();
        }
    }

    public void clearCache() {
        super.clearCache(true);
    }

    public String getUserAgentString() {
        return getSettings().getUserAgentString();
    }

    public void setUserAgentString(String ua) {
        getSettings().setUserAgentString(ua);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int pointerId = event.getPointerId(event.getActionIndex());
        if (action == 0) {
            this.dx = event.getX();
            this.dy = event.getY();
            if (!this.isUser) {
                this.mEventSparseArray.put(pointerId, MotionEvent.obtain(event));
                return true;
            }
        } else if (action == 2) {
            if (!this.isUser && Math.abs(event.getY() - this.dy) > 5.0f) {
                return true;
            }
        } else if (action == 1) {
            if (this.isUser || Math.abs(event.getY() - this.dy) <= 5.0f) {
                MotionEvent down = this.mEventSparseArray.get(pointerId);
                if (down != null) {
                    super.onTouchEvent(down);
                    down.recycle();
                    this.mEventSparseArray.remove(pointerId);
                }
            } else {
                this.isUser = true;
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
