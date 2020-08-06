package android.taobao.windvane.packageapp;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import org.json.JSONObject;

public class WVPackageAppPrefixesConfig {
    private static final String TAG = "WVPackageAppPrefixesConfig";
    private static final String VERION_KEY = "WVZipPrefixesVersion";
    private static volatile WVPackageAppPrefixesConfig instance = null;
    public int updateCount = 0;
    private String v = "0";

    public static WVPackageAppPrefixesConfig getInstance() {
        if (instance == null) {
            synchronized (WVPackageAppPrefixesConfig.class) {
                if (instance == null) {
                    instance = new WVPackageAppPrefixesConfig();
                    instance.v = ConfigStorage.getStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, "0");
                }
            }
        }
        return instance;
    }

    public synchronized void resetConfig() {
        this.v = "0";
        ConfigStorage.putStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, this.v);
        ZipPrefixesManager.getInstance().clear();
    }

    public void updatePrefixesInfos(String defaultUrl, final WVConfigUpdateCallback callback, String snapshotN) {
        String configUrl;
        this.updateCount = 0;
        String snapshotId = snapshotN;
        long useTime = System.currentTimeMillis() - ConfigStorage.getLongVal(WVConfigManager.SPNAME_CONFIG, "prefixes_updateTime", 0);
        if (useTime > ((long) WVCommonConfig.commonConfig.recoveryInterval) || useTime < 0) {
            this.v = "0";
            snapshotId = "0";
            ConfigStorage.putLongVal(WVConfigManager.SPNAME_CONFIG, "prefixes_updateTime", System.currentTimeMillis());
        }
        if (TextUtils.isEmpty(defaultUrl)) {
            configUrl = WVConfigManager.getInstance().getConfigUrl(Constants.LogTransferLevel.L7, this.v, WVConfigUtils.getTargetValue(), snapshotId);
        } else {
            configUrl = defaultUrl;
        }
        ConnectManager.getInstance().connect(configUrl, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse data, int token) {
                if (data != null && data.getData() != null) {
                    try {
                        String content = new String(data.getData(), "utf-8");
                        if (!WVPackageAppPrefixesConfig.this.parseConfig(content)) {
                            if (callback != null) {
                                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                            }
                        } else if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, WVPackageAppPrefixesConfig.this.updateCount);
                        }
                        TaoLog.i(WVPackageAppPrefixesConfig.TAG, content);
                    } catch (UnsupportedEncodingException e) {
                        if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVPackageAppPrefixesConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }
        });
    }

    public boolean parseConfig(String config) {
        if (TextUtils.isEmpty(config)) {
            return false;
        }
        ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
        try {
            JSONObject jsonObj = new JSONObject(config);
            if (jsonObj != null) {
                try {
                    this.v = jsonObj.optString("v");
                    String i = jsonObj.optString(UploadQueueMgr.MSGTYPE_INTERVAL);
                    if (this.v == null) {
                        return false;
                    }
                    String rules = jsonObj.optString("rules");
                    if (rules != null) {
                        Hashtable<String, Hashtable<String, String>> prefixes = ZipAppUtils.parsePrefixes(rules);
                        if (i != null && "-1".equals(i)) {
                            ZipPrefixesManager.getInstance().clear();
                        }
                        if (ZipPrefixesManager.getInstance().mergePrefixes(prefixes)) {
                            ConfigStorage.putStringVal(ZipPrefixesManager.SPNAME, VERION_KEY, this.v);
                        }
                    }
                } catch (Exception e) {
                    JSONObject jSONObject = jsonObj;
                    TaoLog.e(TAG, "parse PrefixesInfos error!");
                    return false;
                }
            }
            return true;
        } catch (Exception e2) {
            TaoLog.e(TAG, "parse PrefixesInfos error!");
            return false;
        }
    }
}
