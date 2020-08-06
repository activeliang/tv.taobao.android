package android.taobao.windvane.packageapp.zipapp;

import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.AppResConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZipAppManager {
    private static String TAG = "PackageApp-ZipAppManager";
    private static ZipAppManager zipAppManager;
    private boolean isInit = false;
    private ZipAppFileManager zipAppFile;

    public static synchronized ZipAppManager getInstance() {
        ZipAppManager zipAppManager2;
        synchronized (ZipAppManager.class) {
            if (zipAppManager == null) {
                zipAppManager = new ZipAppManager();
            }
            zipAppManager2 = zipAppManager;
        }
        return zipAppManager2;
    }

    public synchronized boolean init() {
        boolean z;
        if (!this.isInit) {
            TaoLog.d(TAG, "init: zipapp init start .");
            this.zipAppFile = ZipAppFileManager.getInstance();
            boolean isSuccess = this.zipAppFile.createZipAppInitDir();
            TaoLog.i(TAG, "init: zipapp init finished .isSuccess=" + isSuccess);
            this.isInit = isSuccess;
            z = this.isInit;
        } else {
            z = true;
        }
        return z;
    }

    public int install(ZipAppInfo appInfo, String destFile, boolean isInstall) {
        if (appInfo == null || TextUtils.isEmpty(destFile)) {
            TaoLog.w(TAG, "install: check fail :appInfo is null or destFile is null");
            AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_PARAM, "ErrorMsg = ERR_PARAM");
            return ZipAppResultCode.ERR_PARAM;
        }
        boolean res = this.zipAppFile.unZipToTmp(appInfo, destFile);
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "install: unZipToTmp :[" + appInfo.name + SymbolExpUtil.SYMBOL_COLON + res + "]");
        }
        if (appInfo.isPreViewApp) {
            WVEventService.getInstance().onEvent(WVEventId.ZIPAPP_UNZIP, Boolean.valueOf(res));
        }
        if (res) {
            return checkCopyUpdateDel(appInfo, isInstall);
        }
        AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_FILE_UNZIP, "ErrorMsg = ERR_FILE_UNZIP");
        return ZipAppResultCode.ERR_FILE_UNZIP;
    }

    public int checkCopyUpdateDel(ZipAppInfo appInfo, boolean isInstall) {
        String typeName = isInstall ? AtlasMonitor.INSTALL : "upgrade";
        try {
            boolean res = validInstallZipPackage(appInfo, isInstall);
            if (appInfo.isPreViewApp) {
                appInfo.isPreViewApp = false;
                WVEventService.getInstance().onEvent(WVEventId.ZIPAPP_VALID, Boolean.valueOf(res));
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, typeName + ": validZipPackage :[" + appInfo.name + SymbolExpUtil.SYMBOL_COLON + res + "]");
            }
            if (!res) {
                AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_CHECK_ZIP, "ErrorMsg = ERR_CHECK_ZIP");
                return ZipAppResultCode.ERR_CHECK_ZIP;
            } else if (!parseUrlMappingInfo(appInfo, true)) {
                AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_FILE_READ, "ErrorMsg = ERR_FILE_READ_MAPPINGINFO");
                return ZipAppResultCode.ERR_FILE_READ;
            } else {
                dealAppResFileName(appInfo, isInstall);
                boolean res2 = this.zipAppFile.copyZipApp(appInfo);
                if (!res2) {
                    AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_FILE_COPY, "ErrorMsg = ERR_FILE_COPY");
                    return ZipAppResultCode.ERR_FILE_COPY;
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, typeName + ": copyZipApp :[" + appInfo.name + SymbolExpUtil.SYMBOL_COLON + res2 + "]");
                }
                appInfo.status = ZipAppConstants.ZIP_NEWEST;
                boolean res3 = ConfigManager.updateGlobalConfig(appInfo, (String) null, false);
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, typeName + ": UpdateGlobalConfig :[" + appInfo.name + SymbolExpUtil.SYMBOL_COLON + res3 + "]");
                }
                if (!res3) {
                    AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_FILE_SAVE, "ErrorMsg = ERR_FILE_SAVE");
                    return ZipAppResultCode.ERR_FILE_SAVE;
                }
                boolean del = this.zipAppFile.deleteHisZipApp(appInfo);
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, typeName + ": deleteHisZipApp :" + del);
                }
                return ZipAppResultCode.SECCUSS;
            }
        } catch (Exception e) {
            AppInfoMonitor.error(appInfo, ZipAppResultCode.ERR_SYSTEM, "ErrorMsg = ERR_SYSTEM : " + e.getMessage());
            TaoLog.e(TAG, "checkCopyUpdateDel Exception:" + e.getMessage());
            return ZipAppResultCode.ERR_SYSTEM;
        }
    }

    private void dealAppResFileName(ZipAppInfo appInfo, boolean isInstall) {
        if (!isInstall) {
            TaoLog.d(TAG, appInfo.name + " : appResFile changeName : " + (new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, ZipAppConstants.APP_RES_NAME, true)).renameTo(new File(ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, ZipAppConstants.APP_RES_INC_NAME, true))) ? "sussess!" : "failed!"));
        }
    }

    public int unInstall(ZipAppInfo appInfo) {
        try {
            if (!this.zipAppFile.deleteZipApp(appInfo, false)) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(TAG, "unInstall: deleteZipApp :fail [" + appInfo.name + "]");
                }
                return ZipAppResultCode.ERR_FILE_DEL;
            }
            boolean res = ConfigManager.updateGlobalConfig(appInfo, (String) null, true);
            if (!res) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(TAG, "unInstall: updateGlobalConfig :fail [" + appInfo.name + res + "]");
                }
                return ZipAppResultCode.ERR_FILE_SAVE;
            }
            ConfigManager.getLocGlobalConfig().removeZcacheRes(appInfo.name);
            return ZipAppResultCode.SECCUSS;
        } catch (Exception e) {
            TaoLog.e(TAG, "unInstall Exception:" + e.getMessage());
            return ZipAppResultCode.ERR_SYSTEM;
        }
    }

    public static boolean parseUrlMappingInfo(ZipAppInfo zipAppInfo, boolean isTemp) {
        String str;
        if (zipAppInfo == null) {
            return false;
        }
        if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE) {
            TaoLog.d(TAG, "zcache not need parse appinfo.wvc");
            return true;
        }
        String appinfo = ZipAppFileManager.getInstance().readZipAppRes(zipAppInfo, ZipAppConstants.APP_INFO_NAME, isTemp);
        if (TextUtils.isEmpty(appinfo)) {
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP) {
                zipAppInfo.mappingUrl = "//h5." + GlobalConfig.env.getValue() + ".taobao.com/app/" + zipAppInfo.name + WVNativeCallbackUtil.SEPERATER;
            }
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE2) {
                return false;
            }
            TaoLog.w(TAG, "parseUrlMappingInfo fail. appinfo.wvc is empty.");
            return true;
        }
        try {
            JSONObject appInfoResObj = new JSONObject(appinfo);
            String optString = appInfoResObj.optString("appMonitor");
            String mappingUrl = appInfoResObj.optString("mappingUrl");
            if (!TextUtils.isEmpty(mappingUrl)) {
                zipAppInfo.mappingUrl = mappingUrl;
                TaoLog.i(TAG, zipAppInfo.name + " : mappingUrl : " + mappingUrl);
            } else {
                TaoLog.w(TAG, zipAppInfo.name + " mappingUrl is empty!");
            }
            if (zipAppInfo.folders == null) {
                zipAppInfo.folders = new ArrayList<>();
            }
            JSONArray removedFolders = appInfoResObj.optJSONArray("removedFolders");
            if (removedFolders != null) {
                for (int i = 0; i < removedFolders.length(); i++) {
                    String rmFolder = removedFolders.get(i).toString();
                    if (zipAppInfo.folders.contains(rmFolder)) {
                        zipAppInfo.folders.remove(rmFolder);
                        TaoLog.i(TAG, zipAppInfo.name + " : remvoe folder : " + rmFolder);
                    }
                }
            }
            JSONArray addFolders = appInfoResObj.optJSONArray("addFolders");
            if (addFolders != null) {
                for (int i2 = 0; i2 < addFolders.length(); i2++) {
                    String addFolder = addFolders.get(i2).toString();
                    zipAppInfo.folders.add(addFolder);
                    TaoLog.i(TAG, zipAppInfo.name + " : add folder : " + addFolder);
                }
            }
            JSONArray removedRes = appInfoResObj.optJSONArray("removedRes");
            if (removedRes != null && mappingUrl != null) {
                for (int i3 = 0; i3 < removedRes.length(); i3++) {
                    String rmRes = removedRes.get(i3).toString();
                    if (appinfo != null) {
                        String rmFilePath = ZipAppFileManager.getInstance().getZipResAbsolutePath(zipAppInfo, rmRes, false);
                        if (TextUtils.isEmpty(rmFilePath)) {
                            break;
                        }
                        File file = new File(rmFilePath);
                        if (file.exists()) {
                            boolean ok = FileAccesser.deleteFile(file);
                            String str2 = TAG;
                            StringBuilder append = new StringBuilder().append(zipAppInfo.name).append(" : delete res:").append(rmFilePath).append(" : ");
                            if (ok) {
                                str = "sussess!";
                            } else {
                                str = "failed!";
                            }
                            TaoLog.i(str2, append.append(str).toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (zipAppInfo.getAppType() == ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE2) {
                return false;
            }
        }
        return true;
    }

    public static boolean validInstallZipPackage(ZipAppInfo appInfo, boolean isInstall) {
        byte[] data;
        try {
            String appres = ZipAppFileManager.getInstance().readZipAppRes(appInfo, ZipAppConstants.APP_RES_NAME, true);
            if (TextUtils.isEmpty(appres)) {
                TaoLog.w(TAG, "validZipPackage fail. appres is empty.");
                return false;
            }
            AppResConfig AppResInfo = ZipAppUtils.parseAppResConfig(appres, true);
            if (AppResInfo == null) {
                TaoLog.w(TAG, "validZipPackage fail. AppResInfo valid fail.");
                return false;
            }
            ArrayList<String> list = new ArrayList<>();
            for (Map.Entry<String, AppResConfig.FileInfo> fileInfo : AppResInfo.mResfileMap.entrySet()) {
                String hash = fileInfo.getValue().v;
                String path = fileInfo.getKey();
                if (!(appInfo == null || ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP == appInfo.getAppType())) {
                    list.add(path);
                }
                if (TextUtils.isEmpty(hash) || ((data = ZipAppFileManager.getInstance().readZipAppResByte(appInfo, path, true)) != null && data.length >= 1 && !hash.equals(DigestUtils.md5ToHex(data)))) {
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, path + "[invalid]" + hash);
                    }
                    return false;
                }
            }
            if (appInfo != null && ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE == appInfo.getAppType()) {
                if (!isInstall) {
                    ArrayList<String> list_org = ConfigManager.getLocGlobalConfig().getZcacheResConfig().get(appInfo.name);
                    int i = 0;
                    while (list_org != null && i < list_org.size()) {
                        list.add(list_org.get(i));
                        i++;
                    }
                }
                ConfigManager.updateZcacheurlMap(appInfo.name, list);
            }
            return true;
        } catch (Exception e) {
            TaoLog.e(TAG, "validZipPackage fail. parse config fail: " + e.getMessage());
            return false;
        }
    }

    public int validRunningZipPackage(String patch) {
        try {
            String appres = ZipAppFileManager.getInstance().readFile(patch);
            if (TextUtils.isEmpty(appres)) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "validZipPackage fail. appres is empty.patch=" + patch);
                }
                return ZipAppResultCode.ERR_NOTFOUND_APPRES;
            }
            AppResConfig AppResInfo = ZipAppUtils.parseAppResConfig(appres, true);
            if (AppResInfo == null) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "validZipPackage fail. AppResInfo valid fail.");
                }
                return ZipAppResultCode.ERR_VERIFY_APPRES;
            }
            for (Map.Entry<String, AppResConfig.FileInfo> fileInfo : AppResInfo.mResfileMap.entrySet()) {
                String hash = fileInfo.getValue().v;
                String key = fileInfo.getKey();
                WVZipSecurityManager.getInstance().put(fileInfo.getValue().url, hash);
            }
            return ZipAppResultCode.SECCUSS;
        } catch (Exception e) {
            return ZipAppResultCode.ERR_VERIFY_APPRES;
        }
    }
}
