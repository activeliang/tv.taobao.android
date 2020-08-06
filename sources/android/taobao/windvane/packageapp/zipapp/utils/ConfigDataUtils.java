package android.taobao.windvane.packageapp.zipapp.utils;

import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.monitor.WVConfigMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.TaoLog;
import com.alibaba.analytics.core.device.Constants;

public class ConfigDataUtils {
    private static String ATTACH_ITEM_SPLIT = "|";
    private static String ATTACH_SPLIT = Constants.SEPARATOR;
    private static String TAG = "ConfigDataUtils";

    public static ConfigData parseConfig(String content, boolean valid, boolean isAppRes) {
        if (content == null) {
            return null;
        }
        ConfigDataUtils configDataUtils = new ConfigDataUtils();
        configDataUtils.getClass();
        ConfigData configData = new ConfigData();
        int indexConfig = content.lastIndexOf(ATTACH_SPLIT);
        if (indexConfig > 0) {
            configData.json = content.substring(0, indexConfig);
            String attach = content.substring(indexConfig + 2);
            int indexToken = attach.indexOf(ATTACH_ITEM_SPLIT);
            if (indexToken > 0) {
                configData.systemtime = attach.substring(0, indexToken);
                configData.tk = attach.substring(indexToken + 1);
                if (!valid || ZipAppSecurityUtils.validConfigFile(configData.json, configData.tk)) {
                    return configData;
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.w(TAG, "parseConfig:SecurityUtils validConfigFile fail ");
                }
                if (!isAppRes) {
                    GlobalInfoMonitor.error(ZipAppResultCode.ERR_CHECK_CONFIG_APPS, "");
                }
                return null;
            } else if (valid) {
                return null;
            } else {
                configData.systemtime = attach;
                return configData;
            }
        } else if (valid) {
            return null;
        } else {
            configData.json = content;
            return configData;
        }
    }

    public static ZipGlobalConfig parseGlobalConfig(String content) {
        try {
            ZipGlobalConfig appsConfigOb = ZipAppUtils.parseString2GlobalConfig(content);
            appsConfigOb.setZcacheResConfig(ZipAppUtils.parseZcacheConfig(ZipAppFileManager.getInstance().readZcacheConfig(false)));
            return appsConfigOb;
        } catch (Throwable e) {
            WVConfigMonitorInterface moniter = WVMonitorService.getConfigMonitor();
            if (moniter != null) {
                moniter.didOccurUpdateConfigError(WVConfigManager.CONFIGNAME_PACKAGE, WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal(), e.getMessage());
            }
            TaoLog.e(TAG, "parseGlobalConfig Exception:" + e.getMessage());
            return null;
        }
    }

    public class ConfigData {
        public String json;
        public String systemtime = "0";
        public String tk;

        public ConfigData() {
        }
    }
}
