package android.taobao.windvane.extra.uc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.extra.jsbridge.WVUCBase;
import android.taobao.windvane.filter.WVSecurityFilter;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVAppEvent;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVPluginEntryManager;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jspatch.WVJsPatchListener;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.NetWork;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.view.PopupWindowController;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVRenderPolicy;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.taobao.windvane.webview.WVUIModel;
import android.taobao.windvane.webview.WindVaneError;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.widget.Toast;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.extension.UCSettings;
import com.uc.webview.export.utility.SetupTask;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVUCWebView extends WebView implements Handler.Callback, IWVWebView {
    private static final String TAG = "WVUCWebView";
    private static String UC_CORE_URL;
    private static String UC_CORE_URL_DEBUG = "http://gw.alicdn.com/bao/uploaded/LB1EOD6KpXXXXbiXXXXXXXXXXXX.zip";
    private static String UC_CORE_URL_DEBUG_X86 = "http://gw.alicdn.com/bao/uploaded/LB1sNTKKpXXXXXPXVXXXXXXXXXX.zip";
    private static String UC_PLAYER_URL = "http://gw.alicdn.com/bao/uploaded/LB1NmF0MFXXXXaMXFXXXXXXXXXX.zip";
    private static boolean evaluateJavascriptSupported;
    private static int fromType = 70;
    private static boolean isUCSDKSupport = false;
    private static boolean mUseAliNetwork = true;
    private static boolean mUseSystemWebView = false;
    private static Pattern pattern = null;
    public String bizCode = "";
    protected Context context;
    private String currentUrl = null;
    private String dataOnActive = null;
    float dx;
    float dy;
    protected WVPluginEntryManager entryManager;
    private boolean isLive = false;
    boolean isUser = true;
    private WVJsPatchListener jsPatchListener = null;
    /* access modifiers changed from: private */
    public boolean longPressSaveImage = true;
    SparseArray<MotionEvent> mEventSparseArray = new SparseArray<>();
    private Hashtable<String, Hashtable<String, String>> mH5MonitorCache = null;
    protected Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mImageUrl;
    private View.OnLongClickListener mLongClickListener = null;
    public long mPageStart = 0;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags = {"保存到相册"};
    private int mWvNativeCallbackId = 1000;
    private long onErrorTime = 0;
    /* access modifiers changed from: private */
    public View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (WVUCWebView.this.mPopupMenuTags != null && WVUCWebView.this.mPopupMenuTags.length > 0 && WVUCWebView.this.mPopupMenuTags[0].equals(v.getTag())) {
                try {
                    PermissionProposer.buildPermissionTask(WVUCWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                        public void run() {
                            ImageTool.saveImageToDCIM(WVUCWebView.this.context.getApplicationContext(), WVUCWebView.this.mImageUrl, WVUCWebView.this.mHandler);
                        }
                    }).setTaskOnPermissionDenied(new Runnable() {
                        public void run() {
                            WVUCWebView.this.mHandler.sendEmptyMessage(405);
                        }
                    }).execute();
                } catch (Exception e) {
                }
            }
            WVUCWebView.this.mPopupController.hide();
        }
    };
    protected boolean supportDownload = true;
    private String ucParam = "";
    protected WVUCWebChromeClient webChromeClient;
    protected WVUCWebViewClient webViewClient;
    private WVSecurityFilter wvSecurityFilter = null;
    private WVUIModel wvUIModel = null;

    static {
        boolean z = true;
        UC_CORE_URL = "http://gw.alicdn.com/bao/uploaded/LB1xs2JKpXXXXaLXVXXXXXXXXXX.zip";
        if (Build.VERSION.SDK_INT < 19) {
            z = false;
        }
        evaluateJavascriptSupported = z;
        try {
            if (WVUCUtils.isArchContains("x86")) {
                UC_CORE_URL = UC_CORE_URL_DEBUG_X86;
                TaoLog.i(TAG, "UCCore use x86 core");
            } else if (EnvUtil.isAppDebug()) {
                UC_CORE_URL = UC_CORE_URL_DEBUG;
                TaoLog.i(TAG, "UCCore use debug core");
            }
        } catch (Exception e) {
        }
        try {
            GlobalConfig config = GlobalConfig.getInstance();
            if (config != null) {
                initUCLIb(config.getUcsdkappkeySec(), GlobalConfig.context);
            } else {
                initUCLIb((String[]) null, GlobalConfig.context);
            }
        } catch (Throwable th) {
            initUCLIb(GlobalConfig.context);
        }
        TaoLog.i(TAG, "static UCCore:" + UC_CORE_URL);
    }

    public int getContentHeight() {
        return (int) (((float) super.getContentHeight()) * super.getScale());
    }

    public WVUCWebView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle, mUseSystemWebView);
        this.context = ctx;
        init();
    }

    public WVUCWebView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs, mUseSystemWebView);
        this.context = ctx;
        init();
    }

    public WVUCWebView(Context ctx) {
        super(ctx, mUseSystemWebView);
        this.context = ctx;
        init();
    }

    public boolean isLive() {
        return this.isLive;
    }

    public static void setUcCoreUrl(String ucCoreUrl) {
        if (!TextUtils.isEmpty(ucCoreUrl)) {
            UC_CORE_URL = ucCoreUrl;
        }
    }

    public static int getFromType() {
        fromType = 70;
        if (getUCSDKSupport()) {
            fromType = getUseTaobaoNetwork() ? 6 : 5;
        } else if (!mUseSystemWebView) {
            fromType = 71;
        }
        return fromType;
    }

    public static boolean getUCSDKSupport() {
        return isUCSDKSupport;
    }

    public static void setUseSystemWebView(boolean flag) {
        mUseSystemWebView = flag;
        fromType = 70;
    }

    public static boolean getUseTaobaoNetwork() {
        return mUseAliNetwork;
    }

    public static void setUseTaobaoNetwork(boolean UseTaobaoNetwork) {
        mUseAliNetwork = UseTaobaoNetwork;
    }

    /* access modifiers changed from: private */
    public static void onUCMCoreSwitched(Context ctx) {
        TaoLog.d(TAG, "UCSDK init onUCMCoreSwitched ");
        isUCSDKSupport = true;
        UCCore.setThirdNetwork(new AliNetworkAdapter(ctx.getApplicationContext()), new AliNetworkDecider());
        try {
            UCCore.updateUCPlayer(GlobalConfig.context, UC_PLAYER_URL, new IsWifiCallable());
        } catch (Exception e) {
            TaoLog.e(TAG, "UCCore update UCPlayer failed:" + e.getMessage());
        }
    }

    public static boolean initUCLIb(Context ctx) {
        if (ctx == null) {
            return false;
        }
        return initUCLIb((String[]) null, ctx.getApplicationContext());
    }

    public static boolean initUCLIb(String[] ucsdkappkeySec, Context ctx) {
        TaoLog.d(TAG, "UCSDK initUCLIb begin ");
        WVCommonConfig.getInstance();
        setUseSystemWebView(WVCommonConfig.commonConfig.useSystemWebView);
        TaoLog.d(TAG, "UCSDK initUCLIb UseSystemWebView " + mUseSystemWebView);
        if (isUCSDKSupport) {
            return true;
        }
        try {
            WVCommonConfig.getInstance();
            setUcCoreUrl(WVCommonConfig.commonConfig.ucCoreUrl);
            if (ucsdkappkeySec != null && ucsdkappkeySec.length > 0) {
                UCCore.setup(UCCore.OPTION_PROVIDED_KEYS, ucsdkappkeySec);
            }
            ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) UCCore.setup("CONTEXT", ctx.getApplicationContext()).setup(UCCore.OPTION_HARDWARE_ACCELERATED, true)).setup(UCCore.OPTION_CORE_VERSION_EXCLUDE, WVCommonConfig.commonConfig.excludeUCVersions)).setup(UCCore.OPTION_MULTI_CORE_TYPE, true)).setup(UCCore.OPTION_USE_SYSTEM_WEBVIEW, Boolean.valueOf(mUseSystemWebView))).setup(UCCore.OPTION_WEBVIEW_POLICY, 1)).setup(UCCore.OPTION_LOAD_POLICY, UCCore.LOAD_POLICY_SPECIFIED_ONLY)).setup(UCCore.OPTION_VERIFY_POLICY, Integer.valueOf(UCCore.VERIFY_POLICY_ALL))).setup(UCCore.OPTION_DOWNLOAD_CHECKER, new IsWifiCallable())).setup(UCCore.OPTION_UCM_UPD_URL, UC_CORE_URL)).setup(UCCore.OPTION_DELETE_CORE_POLICY, 7)).onEvent("exception", new ExceptionValueCallback())).onEvent("switch", new SwitchValueCallback())).start();
            TaoLog.i(TAG, "final UCCore:" + UC_CORE_URL);
        } catch (Exception e) {
            TaoLog.e(TAG, "UCCore init fail " + e.getMessage());
        }
        return !mUseSystemWebView;
    }

    public static boolean isNeedCookie(String url) {
        Matcher matcher;
        try {
            if (pattern == null || (matcher = pattern.matcher(url)) == null || !matcher.matches()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            TaoLog.e(TAG, "Pattern complile Exception" + e.getMessage());
            return true;
        }
    }

    public void loadUrl(String url) {
        if (url != null) {
            if (!WVUrlUtil.isCommonUrl(url) || !WVServerConfig.isBlackUrl(url)) {
                WVSchemeIntercepterInterface schemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
                if (schemeIntercepter != null) {
                    url = schemeIntercepter.dealUrlScheme(url);
                }
                try {
                    UCNetworkDelegate.getInstance().onUrlChange(this, url);
                    TaoLog.i(TAG, "loadUrl : " + url);
                    super.loadUrl(url);
                } catch (Exception e) {
                    TaoLog.e(TAG, e.getMessage());
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
    }

    public void refresh() {
        reload();
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public boolean back() {
        if (!canGoBack()) {
            return false;
        }
        goBack();
        return true;
    }

    public void setPageCacheCapacity(int capacity) {
        if (getUCExtension() != null) {
            UCSettings uCSettings = getUCExtension().getUCSettings();
            UCSettings.setPageCacheCapacity(capacity);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void init() {
        TaoLog.i(TAG, "uc webview init ");
        setContentDescription(TAG);
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.isLive = true;
        WVCommonConfig.getInstance();
        setUseTaobaoNetwork(WVCommonConfig.commonConfig.ucsdk_alinetwork_rate > Math.random());
        TaoLog.d(TAG, "Webview init setUseTaobaoNetwork =" + getUseTaobaoNetwork());
        WVCommonConfig.getInstance();
        UCCore.setParam(WVCommonConfig.commonConfig.ucParam);
        try {
            WVCommonConfig.getInstance();
            if (!TextUtils.isEmpty(WVCommonConfig.commonConfig.cookieUrlRule)) {
                pattern = Pattern.compile(WVCommonConfig.commonConfig.cookieUrlRule);
            }
        } catch (Exception e) {
            TaoLog.e(TAG, "Pattern complile Exception" + e.getMessage());
        }
        WVRenderPolicy.disableAccessibility(this.context);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < 18) {
            settings.setSavePassword(false);
        }
        settings.setDatabasePath(this.context.getApplicationInfo().dataDir + "/localstorage");
        settings.setGeolocationEnabled(true);
        String apptag = GlobalConfig.getInstance().getAppTag();
        String appversion = GlobalConfig.getInstance().getAppVersion();
        if (!TextUtils.isEmpty(apptag) && !TextUtils.isEmpty(appversion)) {
            settings.setUserAgentString(settings.getUserAgentString() + " AliApp(" + apptag + WVNativeCallbackUtil.SEPERATER + appversion + ")");
        }
        settings.setUserAgentString(settings.getUserAgentString() + GlobalConfig.DEFAULT_UA);
        settings.setCacheMode(-1);
        if (mUseAliNetwork && getUCExtension() != null) {
            getUCExtension().getUCSettings().setUCCookieType(1);
        }
        if (getCurrentViewCoreType() == 1) {
            TaoLog.d(TAG, "init  CurrentViewCoreType()=CORE_TYPE_U3");
            isUCSDKSupport = true;
            UCCore.setThirdNetwork(new AliNetworkAdapter(this.context.getApplicationContext()), new AliNetworkDecider());
        }
        UCSettings.setEnableAdblock(false);
        UCSettings.setEnableDispatcher(false);
        UCSettings.setGlobalEnableUCProxy(false);
        UCSettings.setEnableMultiThreadParser(true);
        UCSettings.setEnableCustomErrorPage(true);
        UCSettings.setEnableUCVideoViewFullscreen(true);
        UCSettings.setForceUserScalable(2);
        if (TaoLog.getLogStatus()) {
            UCCore.setPrintLog(true);
        }
        setPageCacheCapacity(5);
        UCCore.setStatDataCallback(new WVValueCallback());
        setWebViewClient(new WVUCWebViewClient(this.context));
        setWebChromeClient(new WVUCWebChromeClient());
        UCExtension ucExtension = getUCExtension();
        if (ucExtension != null) {
            ucExtension.setClient(new WVUCClient());
        }
        this.wvUIModel = new WVUIModel(this.context, this);
        WVJsBridge.getInstance().init();
        this.entryManager = new WVPluginEntryManager(this.context, this);
        WVAppEvent event = new WVAppEvent();
        event.initialize(this.context, (IWVWebView) this);
        addJsObject("AppEvent", event);
        WVPluginManager.registerPlugin("WVUCBase", (Class<? extends WVApiPlugin>) WVUCBase.class);
        this.wvSecurityFilter = new WVSecurityFilter();
        WVEventService.getInstance().addEventListener(this.wvSecurityFilter, WVEventService.WV_FORWARD_EVENT);
        this.jsPatchListener = new WVJsPatchListener(this);
        WVEventService.getInstance().addEventListener(this.jsPatchListener, WVEventService.WV_BACKWARD_EVENT);
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable e2) {
                TaoLog.d(TAG, "removeJavascriptInterface " + e2.getMessage());
            }
        }
        this.mLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                try {
                    WebView.HitTestResult result = WVUCWebView.this.getHitTestResult();
                    if (result == null || !WVUCWebView.this.longPressSaveImage) {
                        return false;
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(WVUCWebView.TAG, "Long click on WebView, " + result.getExtra());
                    }
                    if (result.getType() != 8 && result.getType() != 5) {
                        return false;
                    }
                    String unused = WVUCWebView.this.mImageUrl = result.getExtra();
                    PopupWindowController unused2 = WVUCWebView.this.mPopupController = new PopupWindowController(WVUCWebView.this.context, WVUCWebView.this, WVUCWebView.this.mPopupMenuTags, WVUCWebView.this.popupClickListener);
                    WVUCWebView.this.mPopupController.show();
                    return true;
                } catch (Exception e) {
                    TaoLog.e(WVUCWebView.TAG, "getHitTestResult error:" + e.getMessage());
                    return false;
                }
            }
        };
        setOnLongClickListener(this.mLongClickListener);
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
        setAcceptThirdPartyCookies();
    }

    public void evaluateJavascript(String script) {
        evaluateJavascript(script, (ValueCallback<String>) null);
    }

    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        TaoLog.d(TAG, "evaluateJavascript : " + script);
        if (!this.isLive) {
            AppMonitorUtil.commitUCWebviewError("1", script, "not Live");
            return;
        }
        if (script.length() > 10 && "javascript:".equals(script.substring(0, 11).toLowerCase())) {
            script = script.substring(11);
        }
        if (evaluateJavascriptSupported) {
            try {
                super.evaluateJavascript(script, resultCallback);
            } catch (NoSuchMethodError e) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(script, resultCallback);
            } catch (Exception e2) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(script, resultCallback);
                AppMonitorUtil.commitUCWebviewError("2", script, "exception");
            }
        } else if (resultCallback == null) {
            loadUrl("javascript:" + script);
        } else {
            script2NativeCallback(script, resultCallback);
        }
    }

    public void script2NativeCallback(String doScript, ValueCallback<String> callback) {
        int id = this.mWvNativeCallbackId + 1;
        this.mWvNativeCallbackId = id;
        WVNativeCallbackUtil.putNativeCallbak(String.valueOf(id), callback);
        loadUrl("javascript:console.log('wvNativeCallback/" + id + "/'+function(){var s = " + doScript + "; return (typeof s === 'object' ? JSON.stringify(s) : typeof s === 'string' ? '\"' + s + '\"' : s);}())");
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

    public void setWebViewClient(WebViewClient client) {
        if (client instanceof WVUCWebViewClient) {
            this.webViewClient = (WVUCWebViewClient) client;
            super.setWebViewClient(client);
            return;
        }
        throw new WindVaneError("Your WebViewClient must be extended from WVUCWebViewClient");
    }

    public void setWebChromeClient(WebChromeClient client) {
        if (client instanceof WVUCWebChromeClient) {
            this.webChromeClient = (WVUCWebChromeClient) client;
            super.setWebChromeClient(client);
            return;
        }
        throw new WindVaneError("Your WebChromeClient must be extended from WVUCWebChromeClient");
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int arg0) {
        Window w;
        if (arg0 == 0 && Build.VERSION.SDK_INT > 18) {
            Context context2 = getContext();
            if ((context2 instanceof Activity) && (w = ((Activity) context2).getWindow()) != null) {
                final View v = w.getDecorView();
                v.postDelayed(new Runnable() {
                    public void run() {
                        v.requestLayout();
                    }
                }, 100);
            }
        }
        super.onWindowVisibilityChanged(arg0);
    }

    public void onLowMemory() {
    }

    public void coreDestroy() {
        super.setWebViewClient((WebViewClient) null);
        super.setWebChromeClient((WebChromeClient) null);
        this.webViewClient = null;
        this.webChromeClient = null;
        this.context = null;
        WVJsBridge.getInstance().tryToRunTailBridges();
        this.entryManager.onDestroy();
        setOnLongClickListener((View.OnLongClickListener) null);
        this.mLongClickListener = null;
        WVEventService.getInstance().onEvent(WVEventId.PAGE_destroy);
        WVEventService.getInstance().removeEventListener(this.wvSecurityFilter);
        WVEventService.getInstance().removeEventListener(this.jsPatchListener);
        removeAllViews();
        if (JsbridgeHis != null) {
            JsbridgeHis.clear();
        }
        this.isLive = false;
        try {
            super.coreDestroy();
        } catch (Exception e) {
            TaoLog.e(TAG, "WVUCWebView::coreDestroy Exception:" + e.getMessage());
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

    public void OnScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.entryManager != null) {
            this.entryManager.onScrollChanged(l, t, oldl, oldt);
        }
        try {
            super.onScrollChanged(l, t, oldl, oldt);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void coreOnScrollChanged(int arg0, int arg1, int arg2, int arg3) {
        OnScrollChanged(arg0, arg1, arg2, arg3);
        super.coreOnScrollChanged(arg0, arg1, arg2, arg3);
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
        this.isLive = true;
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

    public String getDataOnActive() {
        return this.dataOnActive;
    }

    public void setDataOnActive(String data) {
        this.dataOnActive = data;
    }

    public void fireEvent(String event) {
        getWVCallBackContext().fireEvent(event, "{}");
    }

    public void fireEvent(String event, String data) {
        getWVCallBackContext().fireEvent(event, data);
    }

    public WVCallBackContext getWVCallBackContext() {
        return new WVCallBackContext(this);
    }

    public View getView() {
        return super.getCoreView();
    }

    public WVUIModel getWvUIModel() {
        return this.wvUIModel;
    }

    public void setWvUIModel(WVUIModel wvUIModel2) {
        this.wvUIModel = wvUIModel2;
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

    public void setSupportDownload(boolean support) {
        this.supportDownload = support;
    }

    public String getUserAgentString() {
        return getSettings().getUserAgentString();
    }

    public void setUserAgentString(String ua) {
        getSettings().setUserAgentString(ua);
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
                this.wvUIModel.hideLoadingView();
                return true;
            case 403:
                this.wvUIModel.hideLoadingView();
                return true;
            case 404:
                try {
                    Toast.makeText(this.context, "图片保存到相册成功", 1).show();
                    return true;
                } catch (Exception e) {
                    TaoLog.e(TAG, "NOTIFY_SAVE_IMAGE_SUCCESS fail " + e.getMessage());
                    return true;
                }
            case 405:
                try {
                    Toast.makeText(this.context, "图片保存到相册失败", 1).show();
                    return true;
                } catch (Exception e2) {
                    TaoLog.e(TAG, "NOTIFY_SAVE_IMAGE_FAIL fail " + e2.getMessage());
                    return true;
                }
            default:
                return false;
        }
    }

    public boolean canGoBack() {
        if (WVEventService.getInstance().onEvent(WVEventId.PAGE_back).isSuccess) {
            return false;
        }
        return super.canGoBack();
    }

    private void setAcceptThirdPartyCookies() {
        if (Build.VERSION.SDK_INT >= 21 && getCoreType() != 1) {
            try {
                View view = getView();
                if (view != null && (view instanceof android.webkit.WebView)) {
                    CookieManager.getInstance().setAcceptThirdPartyCookies((android.webkit.WebView) view, true);
                }
            } catch (Throwable th) {
            }
        }
    }

    public boolean coreDispatchTouchEvent(MotionEvent event) {
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
                    super.coreDispatchTouchEvent(down);
                    down.recycle();
                    this.mEventSparseArray.remove(pointerId);
                }
            } else {
                this.isUser = true;
                return true;
            }
        }
        return super.coreDispatchTouchEvent(event);
    }

    private static final class ExceptionValueCallback implements ValueCallback<SetupTask> {
        private ExceptionValueCallback() {
        }

        public void onReceiveValue(SetupTask setupTask) {
            if (setupTask.getException() != null) {
                TaoLog.e(WVUCWebView.TAG, setupTask.getException().getMessage());
            }
        }
    }

    private static final class SwitchValueCallback implements ValueCallback<SetupTask> {
        private SwitchValueCallback() {
        }

        public void onReceiveValue(SetupTask setupTask) {
            if (GlobalConfig.context != null) {
                WVUCWebView.onUCMCoreSwitched(GlobalConfig.context);
                TaoLog.d(WVUCWebView.TAG, "SwitchValueCallback   isUCSDKSupport = true");
            }
        }
    }

    public void insertH5MonitorData(String url, String key, String value) {
        if (!TextUtils.isEmpty(url)) {
            if (this.mH5MonitorCache == null) {
                this.mH5MonitorCache = new Hashtable<>();
            }
            Hashtable<String, String> table = this.mH5MonitorCache.get(url);
            if (table == null) {
                table = new Hashtable<>();
                this.mH5MonitorCache.put(url, table);
            }
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            table.put(key, value);
        }
    }

    public String getH5MonitorData(String url, String key) {
        Hashtable<String, String> table;
        if (this.mH5MonitorCache == null || (table = this.mH5MonitorCache.get(url)) == null) {
            return null;
        }
        return table.get(key);
    }

    public JSONObject getH5MonitorDatas() throws JSONException {
        if (this.mH5MonitorCache == null) {
            return new JSONObject();
        }
        JSONArray array = new JSONArray();
        for (String key : this.mH5MonitorCache.keySet()) {
            Hashtable<String, String> temp = this.mH5MonitorCache.get(key);
            JSONObject jsonObject = new JSONObject();
            Enumeration<String> keys = temp.keys();
            while (keys.hasMoreElements()) {
                String tempKey = keys.nextElement();
                jsonObject.put(tempKey, temp.get(tempKey));
            }
            array.put(jsonObject);
        }
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("resources", array);
        return jsonObject2;
    }

    public void clearH5MonitorData() {
        if (this.mH5MonitorCache != null) {
            this.mH5MonitorCache.clear();
        }
    }

    public boolean containsH5MonitorData(String url) {
        if (this.mH5MonitorCache == null) {
            return false;
        }
        return this.mH5MonitorCache.containsKey(url);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        UCNetworkDelegate.getInstance().removeWebview(this);
    }

    public static class WVValueCallback implements ValueCallback<String> {
        public void onReceiveValue(String s) {
            TaoLog.i(WVUCWebView.TAG, "support : " + WVUCWebView.getUCSDKSupport() + " UC SDK Callback : " + s);
            UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_PA_UCSDK, String.valueOf(WVUCWebView.getUCSDKSupport()), String.valueOf(WVUCWebView.getUseTaobaoNetwork()), s);
        }
    }

    protected static final class IsWifiCallable implements Callable<Boolean> {
        protected IsWifiCallable() {
        }

        public Boolean call() throws Exception {
            return Boolean.valueOf(NetWork.isWiFiActive());
        }
    }

    protected class WVDownLoadListener implements DownloadListener {
        protected WVDownLoadListener() {
        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVUCWebView.TAG, "Download start, url: " + url + " contentDisposition: " + contentDisposition + " mimetype: " + mimetype + " contentLength: " + contentLength);
            }
            if (!WVUCWebView.this.supportDownload) {
                TaoLog.w(WVUCWebView.TAG, "DownloadListener is not support for webview.");
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.setFlags(268435456);
            try {
                WVUCWebView.this.context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(WVUCWebView.this.context, "对不起，您的设备找不到相应的程序", 1).show();
                TaoLog.e(WVUCWebView.TAG, "DownloadListener not found activity to open this url." + e.getMessage());
            }
        }
    }
}
