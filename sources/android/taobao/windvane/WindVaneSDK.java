package android.taobao.windvane;

import android.app.Application;
import android.content.Context;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

public class WindVaneSDK {
    private static final String SPNAME_ENV = "wv_evn";
    private static final String VALUE_NAME = "evn_value";
    private static boolean initialized = false;

    public static void init(Context context, WVAppParams params) {
        init(context, (String) null, 0, params);
    }

    @Deprecated
    public static void init(Context context, String cacheDir, int mode, WVAppParams params) {
        init(context, cacheDir, params);
    }

    public static void init(Context context, String cacheDir, WVAppParams params) {
        if (context == null) {
            throw new NullPointerException("init error, context is null");
        }
        GlobalConfig.context = (Application) (context instanceof Application ? context : context.getApplicationContext());
        if (GlobalConfig.context == null) {
            throw new IllegalArgumentException("init error, context should be Application or its subclass");
        }
        if (TextUtils.isEmpty(cacheDir)) {
            cacheDir = "caches";
        }
        WVCacheManager.getInstance().init(context, cacheDir, 0);
        WVCookieManager.onCreate(context);
        GlobalConfig.getInstance().initParams(params);
        ConfigStorage.initDirs();
        UserTrackUtil.init();
        initConfig();
        initialized = true;
    }

    public static void initConfig() {
        WVDomainConfig.getInstance().init();
        WVCommonConfig.getInstance().init();
        WVConfigManager.getInstance().registerHandler("domain", new WVConfigHandler() {
            public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                WVDomainConfig.getInstance().updateDomainRule(callback, defaultUrl, getSnapshotN());
            }
        });
        WVConfigManager.getInstance().registerHandler("common", new WVConfigHandler() {
            public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                WVCommonConfig.getInstance().updateCommonRule(callback, defaultUrl, getSnapshotN());
            }
        });
    }

    public static void initURLCache(Context context, String cacheDir, int mode) {
        WVCacheManager.getInstance().init(context, cacheDir, mode);
    }

    public static void openLog(boolean open) {
        TaoLog.setLogSwitcher(open);
    }

    public static boolean isTrustedUrl(String url) {
        return WVServerConfig.isTrustedUrl(url);
    }

    public static void setEnvMode(EnvEnum env) {
        if (env != null) {
            try {
                TaoLog.i(SPNAME_ENV, "setEnvMode : " + env.getValue());
                GlobalConfig.env = env;
                if (ConfigStorage.getLongVal(SPNAME_ENV, VALUE_NAME) != ((long) env.getKey())) {
                    WVConfigManager.getInstance().resetConfig();
                    if (WVPackageAppService.getWvPackageAppConfig() != null) {
                        WVPackageAppService.getWvPackageAppConfig().getGlobalConfig().reset();
                    }
                    ConfigStorage.putLongVal(SPNAME_ENV, VALUE_NAME, (long) env.getKey());
                    WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive);
                }
            } catch (Throwable th) {
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
