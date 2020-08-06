package android.taobao.windvane.packageapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.taobao.windvane.WindvaneException;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppUpdateManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVFixedThreadPool;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import com.taobao.ju.track.constants.Constants;
import com.taobao.windvane.zipdownload.DownLoadListener;
import java.util.List;
import java.util.Map;

public class WVPackageAppManager implements DownLoadListener {
    private static WVPackageAppManager appManager;
    private String TAG = "PackageApp-PackageAppManager";
    private boolean isInit = false;
    private Application mContext;
    public long pkgInitTime = 0;

    public static synchronized WVPackageAppManager getInstance() {
        WVPackageAppManager wVPackageAppManager;
        synchronized (WVPackageAppManager.class) {
            if (appManager == null) {
                appManager = new WVPackageAppManager();
            }
            wVPackageAppManager = appManager;
        }
        return wVPackageAppManager;
    }

    private WVPackageAppManager() {
    }

    public synchronized void init(Context context, boolean checkupdate) {
        if (!this.isInit && Build.VERSION.SDK_INT > 11) {
            this.pkgInitTime = System.currentTimeMillis();
            this.mContext = (Application) context.getApplicationContext();
            ZipAppManager.getInstance().init();
            this.isInit = true;
            WVEventService.getInstance().addEventListener(new WVPackageAppWebViewClientFilter(), WVEventService.WV_FORWARD_EVENT);
            WVPackageAppCleanup.getInstance().init();
            WVPackageAppCleanup.getInstance().registerUninstallListener(new WVPackageAppCleanup.UninstallListener() {
                public void onUninstall(List<String> retainList) {
                    WVPackageAppManager.this.cleanUp(retainList);
                }
            });
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_PACKAGE, new WVConfigHandler() {
                public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                    WVPackageAppManager.this.updatePackageAppConfig(callback, defaultUrl, getSnapshotN());
                }
            });
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_PREFIXES, new WVConfigHandler() {
                public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                    WVPackageAppPrefixesConfig.getInstance().updatePrefixesInfos(defaultUrl, callback, getSnapshotN());
                }
            });
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_CUSTOM, new WVConfigHandler() {
                public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                    WVCustomPackageAppConfig.getInstance().updateCustomConfig(callback, defaultUrl, getSnapshotN());
                }
            });
            if (ZipAppUtils.isNeedPreInstall(this.mContext)) {
                boolean res = ZipAppUpdateManager.preloadZipInstall(WVPackageApp.getPreunzipPackageName());
                WVConfigManager.getInstance().resetConfig();
                TaoLog.i(this.TAG, "PackageAppforDebug 预制包解压:" + res);
            }
        }
    }

    public void updatePackageAppConfig(final WVConfigUpdateCallback callback, String defaultUrl, String snapshortN) {
        if (this.isInit) {
            if (WVCommonConfig.commonConfig.packageAppStatus == 2) {
                ZipAppFileManager.getInstance().clearTmpDir((String) null, false);
                if (WVPackageAppService.getWvPackageAppConfig() != null) {
                    WVPackageAppService.getWvPackageAppConfig().updateGlobalConfig(true, new ValueCallback<ZipGlobalConfig>() {
                        public void onReceiveValue(ZipGlobalConfig value) {
                            ZipAppUpdateManager.startUpdateApps(value);
                            if (callback != null && value != null && value.getAppsTable() != null) {
                                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, value.getAppsTable().size());
                            }
                        }
                    }, new ValueCallback<WindvaneException>() {
                        public void onReceiveValue(WindvaneException value) {
                            GlobalInfoMonitor.error(value.getErrorCode(), value.getMessage());
                            if (callback != null) {
                                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                            }
                        }
                    }, snapshortN, defaultUrl);
                }
            } else if (callback != null) {
                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
            }
        }
    }

    public void callback(String url, String destFile, Map<String, String> map, int token, Object obj) {
        ZipAppInfo appInfo = (ZipAppInfo) obj;
        appInfo.status = ZipAppConstants.ZIP_NEWEST;
        boolean isTaskSuccess = true;
        if (TextUtils.isEmpty(destFile)) {
            isTaskSuccess = false;
            TaoLog.e(this.TAG, "PackageAppforDebug download[" + url + "] fail: destFile is null");
        } else if (appInfo != null) {
            try {
                installOrUpgrade(appInfo, destFile, token == 4);
            } catch (Throwable e) {
                AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_SYSTEM, "ErrorMsg = ERR_SYSTEM : " + e.getMessage());
                TaoLog.e(this.TAG, "PackageAppforDebug call Throwable" + e.getMessage());
            }
        }
        ZipAppDownloaderQueue.getInstance().updateFinshCount(isTaskSuccess);
        ZipAppDownloaderQueue.getInstance().updateState();
    }

    /* access modifiers changed from: private */
    public void cleanUp(final List<String> retainList) {
        if (retainList != null && !retainList.isEmpty()) {
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    ZipGlobalConfig config = ConfigManager.getLocGlobalConfig();
                    for (Map.Entry<String, ZipAppInfo> entry : config.getAppsTable().entrySet()) {
                        ZipAppInfo appInfo = entry.getValue();
                        if (WVCommonConfig.commonConfig.isCheckCleanup) {
                            if (appInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE || retainList.contains(appInfo.name)) {
                                appInfo.status = ZipAppConstants.ZIP_NEWEST;
                            } else {
                                appInfo.status = ZipAppConstants.ZIP_REMOVED;
                            }
                        } else if (!appInfo.isOptional && appInfo.status == ZipAppConstants.ZIP_REMOVED) {
                            appInfo.status = ZipAppConstants.ZIP_NEWEST;
                            appInfo.installedSeq = 0;
                            appInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                        }
                    }
                    ConfigManager.saveGlobalConfigToloc(config);
                }
            });
        }
    }

    private void installOrUpgrade(ZipAppInfo appInfo, String destFile, boolean isInstall) {
        AppInfoMonitor.download(appInfo.getNameandVersion());
        int resultcode = -1;
        if (TaoLog.getLogStatus()) {
            TaoLog.d(this.TAG, "PackageAppforDebug 开始安装【" + appInfo.name + "|" + appInfo.v + "】");
        }
        try {
            resultcode = ZipAppManager.getInstance().install(appInfo, destFile, isInstall);
        } catch (Exception e) {
            AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_SYSTEM, "ErrorMsg = ERR_SYSTEM : " + e.getMessage());
        }
        if (resultcode == ZipAppResultCode.SECCUSS) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "PackageAppforDebug 开始升级/安装【" + appInfo.name + "】成功");
            }
            appInfo.status = ZipAppConstants.ZIP_NEWEST;
            appInfo.installedSeq = appInfo.s;
            appInfo.installedVersion = appInfo.v;
            ConfigManager.updateGlobalConfig(appInfo, (String) null, false);
            AppInfoMonitor.success(appInfo);
            if (ConfigManager.getLocGlobalConfig().isAllAppUpdated()) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(this.TAG, "PackageAppforDebug 所有更新升级/安装 成功+总控配置:【" + ZipAppFileManager.getInstance().readGlobalConfig(false) + "】");
                }
                WVEventService.getInstance().onEvent(WVEventId.PACKAGE_UPLOAD_COMPLETE);
                try {
                    WVFixedThreadPool.getInstance().reSetTempBuffer();
                } catch (Exception e2) {
                }
            }
            WVPackageApp.notifyPackageUpdateFinish(appInfo.name);
        }
        ZipAppFileManager.getInstance().clearTmpDir(appInfo.name, true);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(this.TAG, "PackageAppforDebug 清理临时目录【" + appInfo.name + "】");
        }
    }
}
