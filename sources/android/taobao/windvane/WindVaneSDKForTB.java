package android.taobao.windvane;

import android.app.Activity;
import android.content.Context;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.extra.WVIAdapter;
import android.taobao.windvane.extra.WVSchemeProcessor;
import android.taobao.windvane.extra.config.TBConfigManager;
import android.taobao.windvane.extra.jsbridge.TBJsApiManager;
import android.taobao.windvane.jsbridge.api.WVAPI;
import android.taobao.windvane.monitor.WVLocPerformanceMonitor;
import android.taobao.windvane.monitor.WVMonitor;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.urlintercept.WVURLIntercepterDefault;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.taobao.android.lifecycle.PanguApplication;
import com.taobao.windvane.extra.ut.UTCrashCaughtListner;

public class WindVaneSDKForTB {
    public static final String SPNAME = "browserSP";
    public static String Spyd_demote = "demote";
    /* access modifiers changed from: private */
    public static boolean isForeground = false;
    public static WVIAdapter wvAdapter = null;

    public static void init(Context context, String cacheDir, int mode, WVAppParams params) {
        WindVaneSDK.init(context, cacheDir, mode, params);
        WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
        WVPackageAppManager.getInstance().init(context, true);
        if (WVCommonConfig.commonConfig.urlRuleStatus != 0) {
            WVURLInterceptService.registerWVURLIntercepter(new WVURLIntercepterDefault());
        }
        WVAPI.setup();
        WVMonitor.init();
        TBJsApiManager.initJsApi();
        if (EnvUtil.isDebug()) {
            WVEventService.getInstance().addEventListener(WVLocPerformanceMonitor.getInstance(), WVEventService.WV_BACKWARD_EVENT);
            WVEventService.getInstance().onEvent(WVEventId.APP_ONCREATE);
        }
        WVSchemeInterceptService.registerWVURLintercepter(new WVSchemeProcessor());
        MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new UTCrashCaughtListner());
        try {
            TBConfigManager.getInstance().init(context);
            ((PanguApplication) context).registerCrossActivityLifecycleCallback(new BrowserForgroundObserver());
        } catch (Throwable th) {
        }
    }

    public static class BrowserForgroundObserver implements PanguApplication.CrossActivityLifecycleCallback {
        public void onCreated(Activity activity) {
            if (!WindVaneSDKForTB.isForeground && WVMonitorService.getPackageMonitorInterface() != null) {
                boolean unused = WindVaneSDKForTB.isForeground = true;
                WVMonitorService.getPackageMonitorInterface().uploadStartAppTime(System.currentTimeMillis());
            }
        }

        public void onDestroyed(Activity activity) {
        }

        public void onStarted(Activity activity) {
            if (!WindVaneSDKForTB.isForeground && WVMonitorService.getPackageMonitorInterface() != null) {
                boolean unused = WindVaneSDKForTB.isForeground = true;
                WVMonitorService.getPackageMonitorInterface().uploadStartAppTime(System.currentTimeMillis());
            }
        }

        public void onStopped(Activity activity) {
            boolean unused = WindVaneSDKForTB.isForeground = false;
        }
    }
}
