package android.taobao.windvane.packageapp.zipapp;

import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.TaoLog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ZipAppUpdateManager {
    private static String TAG = "PackageApp-ZipAppUpdateManager";

    public static void startUpdateApps(ZipGlobalConfig onlineConfig) {
        if (onlineConfig == null) {
            try {
                TaoLog.w(TAG, "startUpdateApps: GlobalConfig file parse error or invalid!");
            } catch (Exception e) {
                TaoLog.e(TAG, "startUpdateApps: exception ." + e.getMessage());
                e.printStackTrace();
                GlobalInfoMonitor.error(ZipAppResultCode.ERR_APPS_CONFIG_PARSE, e.getMessage());
            }
        } else {
            ZipGlobalConfig locConfig = ConfigManager.getLocGlobalConfig();
            if ("-1".equals(onlineConfig.i)) {
                if (locConfig == null || !locConfig.isAvailableData() || onlineConfig == null || !onlineConfig.isAvailableData()) {
                    ZipAppFileManager.getInstance().clearAppsDir();
                    ZipAppFileManager.getInstance().clearTmpDir((String) null, true);
                    locConfig = new ZipGlobalConfig();
                } else {
                    for (Map.Entry<String, ZipAppInfo> entry : locConfig.getAppsTable().entrySet()) {
                        ZipAppInfo info = entry.getValue();
                        ZipAppInfo infoOnline = onlineConfig.getAppInfo(info.name);
                        if ((infoOnline == null || infoOnline.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) && info.getAppType() != ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
                            info.isOptional = true;
                        }
                    }
                    ConfigManager.saveGlobalConfigToloc(locConfig);
                }
            }
            locConfig.online_v = onlineConfig.v;
            updateAppsInfo(onlineConfig, locConfig);
        }
    }

    public static boolean preloadZipInstall(String zipFileName) {
        ZipAppFileManager.getInstance().clearTmpDir((String) null, false);
        InputStream in = null;
        try {
            in = ZipAppFileManager.getInstance().getPreloadInputStream(zipFileName);
            if (in == null) {
                TaoLog.w(TAG, "获取预装包失败或者不存在预装包");
                if (in == null) {
                    return false;
                }
                try {
                    in.close();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                ZipGlobalConfig loc = ConfigManager.getLocGlobalConfig();
                String prefixesPath = ZipAppFileManager.getInstance().getRootPathApps() + File.separator + ZipAppConstants.APP_PREFIXES_NAME;
                if (loc == null || !loc.isAvailableData()) {
                    ZipAppFileManager.getInstance().clearAppsDir();
                    if (!FileManager.unzip(in, ZipAppFileManager.getInstance().getRootPathApps())) {
                        TaoLog.w(TAG, "预装解压缩失败");
                        if (in == null) {
                            return false;
                        }
                        try {
                            in.close();
                            return false;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            return false;
                        }
                    } else {
                        ConfigManager.saveGlobalConfigToloc((ZipGlobalConfig) null);
                        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
                        for (Map.Entry<String, ZipAppInfo> entry2 : locGlobalConfig.getAppsTable().entrySet()) {
                            String appName = entry2.getKey();
                            ZipAppInfo appinfo = entry2.getValue();
                            if (!(appName == null || appinfo == null)) {
                                appinfo.status = ZipAppConstants.ZIP_NEWEST;
                                appinfo.installedSeq = appinfo.s;
                                appinfo.installedVersion = appinfo.v;
                            }
                        }
                        locGlobalConfig.setZcacheResConfig(ZipAppUtils.parseZcacheConfig(ZipAppFileManager.getInstance().readZcacheConfig(false)));
                        ConfigManager.saveGlobalConfigToloc(locGlobalConfig);
                        WVPackageAppPrefixesConfig.getInstance().parseConfig(ZipAppFileManager.getInstance().readFile(prefixesPath));
                        if (in == null) {
                            return true;
                        }
                        try {
                            in.close();
                            return true;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return true;
                        }
                    }
                } else if (!FileManager.unzip(in, ZipAppFileManager.getInstance().getRootPathTmp())) {
                    TaoLog.w("ZipAppFileManager", "预装解压缩失败");
                    if (in == null) {
                        return false;
                    }
                    try {
                        in.close();
                        return false;
                    } catch (IOException e4) {
                        e4.printStackTrace();
                        return false;
                    }
                } else {
                    ZipGlobalConfig locpreConfig = ConfigDataUtils.parseGlobalConfig(ZipAppFileManager.getInstance().readGlobalConfig(true));
                    updateFromPreLoadApps(loc, locpreConfig);
                    locpreConfig.v = "0";
                    startUpdateApps(locpreConfig);
                    WVPackageAppPrefixesConfig.getInstance().parseConfig(ZipAppFileManager.getInstance().readFile(prefixesPath));
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    return false;
                }
            }
        } catch (Exception e6) {
            e6.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                }
            }
            throw th;
        }
    }

    private static void updateAppsInfo(ZipGlobalConfig online, ZipGlobalConfig local) {
        if (online == null || !online.isAvailableData()) {
            TaoLog.w(TAG, "updateAppsInfo: onlineConfig is null or appsMap is null");
            return;
        }
        TaoLog.i(TAG, "updateAppsInfo: 开始更新所有应用信息[count:" + online.getAppsTable().size() + "]");
        for (Map.Entry<String, ZipAppInfo> entry : online.getAppsTable().entrySet()) {
            ZipAppInfo val_online = entry.getValue();
            local.putAppInfo2Table(val_online.name, val_online);
        }
        local.v = online.v;
        ConfigManager.saveGlobalConfigToloc(local);
    }

    private static void updateFromPreLoadApps(ZipGlobalConfig loc, ZipGlobalConfig online) {
        if (loc == null || !loc.isAvailableData() || online == null || !online.isAvailableData()) {
            TaoLog.w(TAG, "startUpdateApps:[updateApps]  param error .");
        } else {
            updateFromPreLoad(loc, online);
        }
    }

    private static void updateFromPreLoad(ZipGlobalConfig loc, ZipGlobalConfig online) {
        ZipAppInfo val_loc;
        for (Map.Entry<String, ZipAppInfo> entry : online.getAppsTable().entrySet()) {
            String key_online = entry.getKey();
            ZipAppInfo val_online = entry.getValue();
            if (key_online != null && ((val_loc = loc.getAppsTable().get(key_online)) == null || val_loc.installedSeq < val_online.s)) {
                if (ZipAppManager.getInstance().checkCopyUpdateDel(val_online, true) == ZipAppResultCode.SECCUSS) {
                    ConfigManager.updateGlobalConfig(val_online, (String) null, false);
                } else {
                    TaoLog.w(TAG, "[" + val_online.name + val_online.v + "]:预装出错");
                }
            }
        }
    }
}
