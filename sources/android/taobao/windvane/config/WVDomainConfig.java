package android.taobao.windvane.config;

import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

public class WVDomainConfig {
    private static final String TAG = "WVDomainConfig";
    private static volatile WVDomainConfig instance = null;
    private String forbiddenDomainRedirectURL = "";

    public static WVDomainConfig getInstance() {
        if (instance == null) {
            synchronized (WVDomainConfig.class) {
                if (instance == null) {
                    instance = new WVDomainConfig();
                }
            }
        }
        return instance;
    }

    public String getForbiddenDomainRedirectURL() {
        return this.forbiddenDomainRedirectURL;
    }

    public void init() {
        parseConfig(ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "domainwv-data"));
    }

    public void updateDomainRule(final WVConfigUpdateCallback callback, String defaultUrl, String snapshortN) {
        String monitorConfigUrl;
        if (TextUtils.isEmpty(defaultUrl)) {
            monitorConfigUrl = WVConfigManager.getInstance().getConfigUrl("2", WVServerConfig.v, WVConfigUtils.getTargetValue(), snapshortN);
        } else {
            monitorConfigUrl = defaultUrl;
        }
        ConnectManager.getInstance().connect(monitorConfigUrl, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse data, int token) {
                if (callback != null) {
                    if (data == null || data.getData() == null) {
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                        return;
                    }
                    try {
                        String content = new String(data.getData(), "utf-8");
                        if (WVDomainConfig.this.parseConfig(content)) {
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "domainwv-data", content);
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 1);
                            return;
                        }
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                    } catch (UnsupportedEncodingException e) {
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        TaoLog.e(WVDomainConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                }
            }

            public void onError(int code, String message) {
                if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVDomainConfig.TAG, "update domain failed! : " + message);
                super.onError(code, message);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean parseConfig(String config) {
        if (TextUtils.isEmpty(config)) {
            return false;
        }
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(config);
        } catch (JSONException e) {
        }
        if (jsonObj == null) {
            return false;
        }
        String v = jsonObj.optString("v", "");
        if (TextUtils.isEmpty(v)) {
            return false;
        }
        String aliDomain = jsonObj.optString("aliDomain", "");
        String thirdPartyDomain = jsonObj.optString("thirdPartyDomain", "");
        String supportDownloadDomain = jsonObj.optString("supportDownloadDomain", "");
        String blackDomain = jsonObj.optString("forbiddenDomain", "");
        this.forbiddenDomainRedirectURL = jsonObj.optString("forbiddenDomainRedirectURL", "");
        if (!TextUtils.isEmpty(aliDomain)) {
            WVServerConfig.DOMAIN_PATTERN = aliDomain;
            WVServerConfig.domainPat = null;
        }
        if (!TextUtils.isEmpty(thirdPartyDomain)) {
            WVServerConfig.THIRD_PARTY_DOMAIN_PATTERN = thirdPartyDomain;
            WVServerConfig.thirdPartyDomain = null;
        }
        if (!TextUtils.isEmpty(supportDownloadDomain)) {
            WVServerConfig.SUPPORT_DOWNLOAD_DOMAIN_PATTERN = supportDownloadDomain;
            WVServerConfig.supportDownloadDomain = null;
        }
        if (!TextUtils.isEmpty(blackDomain)) {
            WVServerConfig.FORBIDDEN_DOMAIN_PATTERN = blackDomain;
            WVServerConfig.forbiddenDomain = null;
            if (!TextUtils.isEmpty(this.forbiddenDomainRedirectURL) && WVServerConfig.isBlackUrl(this.forbiddenDomainRedirectURL)) {
                this.forbiddenDomainRedirectURL = "";
            }
        }
        WVServerConfig.v = v;
        return true;
    }
}
