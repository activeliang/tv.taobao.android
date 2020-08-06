package android.taobao.windvane.config;

import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class WVCommonConfig {
    private static final String TAG = "WVCommonConfig";
    public static final WVCommonConfigData commonConfig = new WVCommonConfigData();
    private static volatile WVCommonConfig instance = null;

    public static WVCommonConfig getInstance() {
        if (instance == null) {
            synchronized (WVCommonConfig.class) {
                if (instance == null) {
                    instance = new WVCommonConfig();
                }
            }
        }
        return instance;
    }

    public void init() {
        parseConfig(ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "commonwv-data"));
    }

    public void updateCommonRule(final WVConfigUpdateCallback callback, String defaultUrl, String snapshotN) {
        String commonConfigUrl;
        if (TextUtils.isEmpty(defaultUrl)) {
            commonConfigUrl = WVConfigManager.getInstance().getConfigUrl("1", commonConfig.v, WVConfigUtils.getTargetValue(), snapshotN);
        } else {
            commonConfigUrl = defaultUrl;
        }
        ConnectManager.getInstance().connect(commonConfigUrl, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse data, int token) {
                if (callback != null) {
                    if (data == null || data.getData() == null) {
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                        return;
                    }
                    try {
                        String content = new String(data.getData(), "utf-8");
                        int updateCount = WVCommonConfig.this.parseConfig(content);
                        if (updateCount > 0) {
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "commonwv-data", content);
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, updateCount);
                            return;
                        }
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                    } catch (UnsupportedEncodingException e) {
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        TaoLog.e(WVCommonConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                }
            }

            public void onError(int code, String message) {
                if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCommonConfig.TAG, "update common failed! : " + message);
                super.onError(code, message);
            }
        });
    }

    /* access modifiers changed from: private */
    public int parseConfig(String config) {
        if (TextUtils.isEmpty(config)) {
            return 0;
        }
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(config);
        } catch (JSONException e) {
        }
        if (jsonObj == null) {
            return 0;
        }
        String v = jsonObj.optString("v", "");
        if (TextUtils.isEmpty(v)) {
            return 0;
        }
        commonConfig.v = v;
        long configUpdateInterval = jsonObj.optLong("configUpdateInterval", 0);
        if (configUpdateInterval >= 0) {
            commonConfig.updateInterval = configUpdateInterval;
            WVConfigManager.getInstance().setUpdateInterval(configUpdateInterval);
        }
        commonConfig.packageAppStatus = jsonObj.optInt("packageAppStatus", 2);
        commonConfig.monitorStatus = jsonObj.optInt("monitorStatus", 2);
        commonConfig.urlRuleStatus = jsonObj.optInt("urlRuleStatus", 2);
        String urlScheme = jsonObj.optString("urlScheme", "http");
        commonConfig.urlScheme = urlScheme.replace(SymbolExpUtil.SYMBOL_COLON, "");
        JSONObject verifySampleRate = jsonObj.optJSONObject("verifySampleRate");
        if (verifySampleRate != null) {
            commonConfig.verifySampleRate = verifySampleRate.toString();
        }
        commonConfig.ucParam = jsonObj.optString("ucParam", "");
        commonConfig.useSystemWebView = jsonObj.optBoolean("useSystemWebView", false);
        commonConfig.ucsdk_alinetwork_rate = jsonObj.optDouble("ucsdk_alinetwork_rate", 1.0d);
        commonConfig.ucsdk_image_strategy_rate = jsonObj.optDouble("ucsdk_image_strategy_rate", 1.0d);
        commonConfig.cookieUrlRule = jsonObj.optString("cookieUrlRule", "");
        commonConfig.ucCoreUrl = jsonObj.optString("ucCoreUrl", "");
        commonConfig.shareBlankList = jsonObj.optString("shareBlankList", "");
        commonConfig.excludeUCVersions = jsonObj.optString("excludeUCVersions", "1.12.11.0, 1.15.15.0, 1.14.13.0, 1.13.12.0");
        commonConfig.isOpenCombo = jsonObj.optBoolean("isOpenCombo", false);
        commonConfig.isCheckCleanup = jsonObj.optBoolean("isCheckCleanup", true);
        commonConfig.isAutoRegisterApp = jsonObj.optBoolean("isAutoRegisterApp", false);
        commonConfig.isUseTBDownloader = jsonObj.optBoolean("isUseTBDownloader", true);
        commonConfig.isUseAliNetworkDelegate = jsonObj.optBoolean("isUseAliNetworkDelegate", true);
        commonConfig.packageDownloadLimit = jsonObj.optInt("packageDownloadLimit", 30);
        commonConfig.packageAccessInterval = jsonObj.optInt("packageAccessInterval", 3000);
        commonConfig.packageRemoveInterval = jsonObj.optInt("packageRemoveInterval", 432000000);
        commonConfig.recoveryInterval = jsonObj.optInt("recoveryInterval", 432000000);
        commonConfig.customsComboLimit = jsonObj.optInt("customsComboLimit", 3);
        commonConfig.customsDirectQueryLimit = jsonObj.optInt("customsDirectQueryLimit", 10);
        commonConfig.packageZipPrefix = jsonObj.optString("packageZipPrefix", "");
        return jsonObj.length();
    }
}
