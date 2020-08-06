package android.taobao.windvane.extra.config;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TBConfigListenerV1 implements OrangeConfigListenerV1 {
    public void onConfigUpdate(String s, boolean b) {
        if (!TextUtils.isEmpty(s)) {
            TaoLog.d("TBConfigReceiver", "ConfigName: " + s + " isFromLocal:" + b);
            if (s.equalsIgnoreCase(TBConfigManager.ANDROID_WINDVANE_CONFIG)) {
                getAndroidWindvaneConfigData();
            }
        }
    }

    private void getAndroidWindvaneConfigData() {
        String packageApp = OrangeConfig.getInstance().getConfig(TBConfigManager.ANDROID_WINDVANE_CONFIG, WVConfigManager.CONFIGNAME_PACKAGE, "");
        TaoLog.d("TBConfigReceiver", "receive : packageApp: " + packageApp);
        if (!TextUtils.isEmpty(packageApp)) {
            try {
                JSONArray jSONArray = new JSONArray(packageApp);
                if (jSONArray != null) {
                    try {
                        int size = jSONArray.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject jsonObj = jSONArray.optJSONObject(i);
                            String v = jsonObj.optString("v", "");
                            String v0 = jsonObj.optString("v0", "");
                            String v1 = jsonObj.optString("v1", "");
                            String s = jsonObj.optString("s", "");
                            String e = jsonObj.optString("e", "");
                            if (!TextUtils.isEmpty(v) || !TextUtils.isEmpty(v0) || !TextUtils.isEmpty(v1)) {
                                if (s == null || "".equals(s) || "*".equals(s)) {
                                    s = "0";
                                }
                                if (e == null || "".equals(e) || "*".equals(e)) {
                                    e = String.valueOf(Integer.MAX_VALUE);
                                }
                                String appVersion = GlobalConfig.getInstance().getAppVersion();
                                if (!TextUtils.isEmpty(appVersion)) {
                                    int compareMin = compareVersion(appVersion, s);
                                    int compareMax = compareVersion(e, appVersion);
                                    if ((compareMin >= 0 && compareMax > 0) || (compareMin == compareMax && compareMax == 0)) {
                                        WVMonitorService.getConfigMonitor().didOccurUpdateConfigSuccess("package_push");
                                        WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_PACKAGE, v, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                        WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_CUSTOM, v0, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                        WVConfigManager.getInstance().updateConfig(WVConfigManager.CONFIGNAME_PREFIXES, v1, (String) null, WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypePush);
                                        WVEventService.getInstance().onEvent(WVEventId.CONFIG_UPLOAD_COMPLETE);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    } catch (JSONException e2) {
                        JSONArray jSONArray2 = jSONArray;
                        WVMonitorService.getConfigMonitor().didOccurUpdateConfigError(WVConfigManager.CONFIGNAME_PACKAGE, WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR.ordinal(), "package_push parse failed");
                    }
                }
            } catch (JSONException e3) {
                WVMonitorService.getConfigMonitor().didOccurUpdateConfigError(WVConfigManager.CONFIGNAME_PACKAGE, WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR.ordinal(), "package_push parse failed");
            }
        }
    }

    private int compareVersion(String versionMin, String versionMax) {
        String[] versionArray1 = versionMin.split("\\.");
        String[] versionArray2 = versionMax.split("\\.");
        int minLength = versionArray1.length > versionArray2.length ? versionArray2.length : versionArray1.length;
        if (minLength > 3) {
            minLength = 3;
        }
        int diff = 0;
        for (int idx = 0; idx < minLength; idx++) {
            diff = Integer.valueOf(versionArray1[idx]).intValue() - Integer.valueOf(versionArray2[idx]).intValue();
            if (diff != 0) {
                break;
            }
        }
        return diff;
    }
}
