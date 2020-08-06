package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.file.WVFileUtils;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.util.ConfigStorage;
import java.io.File;
import java.util.List;

public class WVPackageAppTool {
    public static String TAG = "WVPackageAppTool";

    public static void uninstallAll() {
        ZipAppFileManager.getInstance().clearAppsDir();
        ZipAppFileManager.getInstance().clearTmpDir((String) null, true);
        WVPackageAppPrefixesConfig.getInstance().resetConfig();
        WVCustomPackageAppConfig.getInstance().resetConfig();
        ConfigManager.getLocGlobalConfig().reset();
        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_PACKAGE, "0");
        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_PREFIXES, "0");
    }

    public static void forceUpdateApp() {
        uninstallAll();
        WVPackageAppManager.getInstance().updatePackageAppConfig((WVConfigUpdateCallback) null, (String) null, "0");
    }

    public static boolean forceOnline() {
        return WVCommonConfig.commonConfig.packageAppStatus == 0;
    }

    public static List<String> getAppsFileList() {
        try {
            return WVFileUtils.getFileListbyDir(new File(ZipAppFileManager.getInstance().getRootPath()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
