package android.taobao.windvane.packageapp.zipapp;

import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.TaoLog;
import com.taobao.ju.track.constants.Constants;
import java.util.ArrayList;

public class ConfigManager {
    private static String Tag = "PackageApp-ConfigManager";
    private static ZipGlobalConfig locGobalConfig = null;

    public static ZipGlobalConfig getLocGlobalConfig() {
        if (WVPackageAppService.getWvPackageAppConfig() == null) {
            WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
        }
        return WVPackageAppService.getWvPackageAppConfig().getGlobalConfig();
    }

    public static void updateZcacheurlMap(String name, ArrayList<String> resList) {
        getLocGlobalConfig().addZcacheResConfig(name, resList);
    }

    public static boolean updateGlobalConfig(ZipAppInfo appsInfo, String version, boolean isDel) {
        ZipAppInfo oldInfo;
        if (appsInfo == null && version == null) {
            try {
                TaoLog.w(Tag, "UpdateGlobalConfig:param is null");
                return false;
            } catch (Exception e) {
                TaoLog.e(Tag, "updateGlobalConfig:exception  " + e.getMessage());
                return false;
            }
        } else {
            if (!isDel) {
                getLocGlobalConfig().putAppInfo2Table(appsInfo.name, appsInfo);
            } else if (appsInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
                getLocGlobalConfig().removeAppInfoFromTable(appsInfo.name);
            } else if (appsInfo.status == ZipAppConstants.ZIP_REMOVED && (oldInfo = getLocGlobalConfig().getAppInfo(appsInfo.name)) != null) {
                oldInfo.installedSeq = 0;
                oldInfo.installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
            }
            if (!saveGlobalConfigToloc(getLocGlobalConfig())) {
                if (!TaoLog.getLogStatus()) {
                    return false;
                }
                TaoLog.w(Tag, "UpdateGlobalConfig:save to localfile fail  ");
                return false;
            } else if (ZipAppUtils.savaZcacheMapToLoc(getLocGlobalConfig().getZcacheResConfig())) {
                return true;
            } else {
                if (!TaoLog.getLogStatus()) {
                    return false;
                }
                TaoLog.w(Tag, "UpdateZcacheConfig:save to localfile fail  ");
                return false;
            }
        }
    }

    public static boolean saveGlobalConfigToloc(ZipGlobalConfig globalConfig) {
        if (WVPackageAppService.getWvPackageAppConfig() != null) {
            return WVPackageAppService.getWvPackageAppConfig().saveLocalConfig(globalConfig);
        }
        return false;
    }

    public static void updateGlobalConfigAppStatus(ZipAppInfo appInfo, int status) {
        ZipAppInfo locappinfo = getLocGlobalConfig().getAppInfo(appInfo.name);
        if (locappinfo != null) {
            locappinfo.status = status;
        }
        updateGlobalConfig(appInfo, (String) null, false);
    }
}
