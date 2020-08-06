package android.taobao.windvane.packageapp;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class WVCustomPackageAppConfig {
    private static final String TAG = "WVCustomPackageAppConfig";
    private static volatile WVCustomPackageAppConfig instance = null;
    /* access modifiers changed from: private */
    public int mComboRqCount = 0;
    /* access modifiers changed from: private */
    public int updateCount = 0;
    private String v = "0";

    static /* synthetic */ int access$006(WVCustomPackageAppConfig x0) {
        int i = x0.mComboRqCount - 1;
        x0.mComboRqCount = i;
        return i;
    }

    public static WVCustomPackageAppConfig getInstance() {
        if (instance == null) {
            synchronized (WVCustomPackageAppConfig.class) {
                if (instance == null) {
                    instance = new WVCustomPackageAppConfig();
                }
            }
        }
        return instance;
    }

    public synchronized void resetConfig() {
        this.v = "0";
        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_CUSTOM, "0");
    }

    public void updateCustomConfig(WVConfigUpdateCallback callback, String defaultUrl, String snapshotN) {
        String configUrl;
        if (WVCommonConfig.commonConfig.packageAppStatus == 2) {
            ZipGlobalConfig config = ConfigManager.getLocGlobalConfig();
            List<String> customList = new ArrayList<>();
            this.updateCount = 0;
            for (Map.Entry<String, ZipAppInfo> entry : config.getAppsTable().entrySet()) {
                ZipAppInfo appInfo = entry.getValue();
                if (appInfo.isOptional) {
                    customList.add(appInfo.name);
                }
            }
            if (customList != null && !customList.isEmpty()) {
                this.updateCount = customList.size();
                Collections.sort(customList, Collator.getInstance(Locale.ENGLISH));
                if (WVCommonConfig.commonConfig.customsDirectQueryLimit <= customList.size()) {
                    if (!TextUtils.isEmpty(defaultUrl)) {
                        configUrl = defaultUrl;
                    } else {
                        configUrl = WVConfigManager.getInstance().getConfigUrl(Constants.LogTransferLevel.L6, "0", WVConfigUtils.getTargetValue(), snapshotN);
                    }
                    updateCustomInfos(configUrl, callback, customList, snapshotN);
                    return;
                }
                updateByCombo(customList, callback, snapshotN);
            } else if (callback != null) {
                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
            }
        } else if (callback != null) {
            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
        }
    }

    /* access modifiers changed from: private */
    public void updateByCombo(List<String> customList, WVConfigUpdateCallback callback, String snapshotN) {
        Iterator<String> iterator = customList.iterator();
        this.mComboRqCount = 0;
        while (iterator.hasNext()) {
            this.mComboRqCount++;
            List<String> appNameList = new ArrayList<>();
            for (int i = 0; i < WVCommonConfig.commonConfig.customsComboLimit && iterator.hasNext(); i++) {
                appNameList.add(iterator.next());
            }
            updateCustomComboInfos(getConfigUrl(appNameList, snapshotN), callback, appNameList, snapshotN);
        }
    }

    private void updateCustomComboInfos(String configUrl, final WVConfigUpdateCallback callback, final List<String> list, String snapshotN) {
        ConnectManager.getInstance().connect(configUrl, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse data, int token) {
                WVCustomPackageAppConfig.access$006(WVCustomPackageAppConfig.this);
                if (data != null && data.getData() != null) {
                    try {
                        if (WVCustomPackageAppConfig.this.parseComboConfig(new String(data.getData(), "utf-8"), list)) {
                            if (callback != null && WVCustomPackageAppConfig.this.mComboRqCount == 0) {
                                callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, WVCustomPackageAppConfig.this.updateCount);
                            }
                        } else if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                        }
                    } catch (Exception e) {
                        if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVCustomPackageAppConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }

            public void onError(int code, String message) {
                WVCustomPackageAppConfig.access$006(WVCustomPackageAppConfig.this);
                if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCustomPackageAppConfig.TAG, "update custom package failed! : " + message);
                super.onError(code, message);
            }
        });
    }

    private void updateCustomInfos(String configUrl, final WVConfigUpdateCallback callback, final List list, final String snapshotN) {
        ConnectManager.getInstance().connect(configUrl, (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
            public void onFinish(HttpResponse data, int token) {
                if (data != null && data.getData() != null) {
                    try {
                        if (!WVCustomPackageAppConfig.this.parseConfig(new String(data.getData(), "utf-8"), list) && callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                        }
                        if (list != null && list.size() > 0) {
                            WVCustomPackageAppConfig.this.updateByCombo(list, callback, snapshotN);
                        } else if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                        }
                    } catch (UnsupportedEncodingException e) {
                        if (callback != null) {
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                        }
                        TaoLog.e(WVCustomPackageAppConfig.TAG, "config encoding error. " + e.getMessage());
                    }
                } else if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NULL_DATA, 0);
                }
            }

            public void onError(int code, String message) {
                if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVCustomPackageAppConfig.TAG, "update custom package failed! : " + message);
                super.onError(code, message);
            }
        });
    }

    public String getConfigUrl(List<String> appList, String snapshotN) {
        Iterator<String> iterator = appList.iterator();
        StringBuilder url = new StringBuilder();
        url.append(GlobalConfig.getH5Host());
        url.append("/appconfig/");
        url.append(snapshotN);
        url.append(WVNativeCallbackUtil.SEPERATER);
        String target = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "abt", "a");
        char abt = target.charAt(0);
        if ('a' > abt || abt > 'c') {
            target = "a";
        }
        url.append(target);
        url.append(WVNativeCallbackUtil.SEPERATER);
        if (appList.size() > 1) {
            url.append("??");
        }
        for (int i = 0; i < WVCommonConfig.commonConfig.customsComboLimit && iterator.hasNext(); i++) {
            if (i != 0) {
                url.append(",");
            }
            url.append(iterator.next());
            url.append("/app.json");
        }
        return url.toString();
    }

    /* access modifiers changed from: private */
    public boolean parseComboConfig(String config, List<String> appInfoList) {
        String[] array;
        if (TextUtils.isEmpty(config) || (array = config.split("\\\n\\\n")) == null || array.length == 0 || array.length != appInfoList.size()) {
            return false;
        }
        ZipGlobalConfig zipGlobalConfig = ConfigManager.getLocGlobalConfig();
        Iterator<String> appInfoNamesIt = appInfoList.iterator();
        for (int i = 0; i < array.length; i++) {
            try {
                JSONObject jsonObj = new JSONObject(array[i]);
                if (jsonObj != null) {
                    try {
                        Iterator<String> iterator = jsonObj.keys();
                        if (!iterator.hasNext()) {
                            JSONObject jSONObject = jsonObj;
                        } else {
                            String appName = iterator.next();
                            String appInfoName = appInfoNamesIt.next();
                            if (!appName.equals(UploadQueueMgr.MSGTYPE_INTERVAL) || jsonObj.optInt(appName, -1) != -1) {
                                JSONObject appInfoObj = jsonObj.optJSONObject(appName);
                                if (appInfoObj == null) {
                                    JSONObject jSONObject2 = jsonObj;
                                } else {
                                    String v2 = appInfoObj.optString("v", "");
                                    if (TextUtils.isEmpty(v2)) {
                                        JSONObject jSONObject3 = jsonObj;
                                    } else {
                                        ZipAppInfo appInfo = zipGlobalConfig.getAppInfo(appName);
                                        if (appInfo == null) {
                                            appInfo = new ZipAppInfo();
                                        }
                                        appInfo.v = v2;
                                        appInfo.name = appName;
                                        appInfo.s = appInfoObj.optLong("s", 0);
                                        appInfo.f = appInfoObj.optLong("f", 5);
                                        appInfo.t = appInfoObj.optLong("t", 0);
                                        appInfo.z = appInfoObj.optString("z", "");
                                        appInfo.isOptional = true;
                                    }
                                }
                            } else {
                                ZipAppInfo appInfo2 = zipGlobalConfig.getAppInfo(appInfoName);
                                if (appInfo2 == null) {
                                    appInfo2 = new ZipAppInfo();
                                }
                                appInfo2.name = appInfoName;
                                appInfo2.isOptional = true;
                                appInfo2.status = ZipAppConstants.ZIP_REMOVED;
                                appInfo2.f |= PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
                                JSONObject jSONObject4 = jsonObj;
                            }
                        }
                    } catch (JSONException e) {
                        JSONObject jSONObject5 = jsonObj;
                    }
                }
                JSONObject jSONObject6 = jsonObj;
            } catch (JSONException e2) {
            }
        }
        TaoLog.v(TAG, "解析成功 combo 一次");
        ConfigManager.saveGlobalConfigToloc(zipGlobalConfig);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean parseConfig(String config, List appInfoList) {
        if (TextUtils.isEmpty(config)) {
            return false;
        }
        ZipGlobalConfig zipGlobalConfig = ConfigManager.getLocGlobalConfig();
        try {
            JSONObject jsonObj = new JSONObject(config);
            if (jsonObj != null) {
                try {
                    this.v = jsonObj.optString("v", "0");
                    if ("0".equals(this.v)) {
                        return false;
                    }
                    JSONObject apps = jsonObj.optJSONObject("apps");
                    Iterator<String> appsIt = apps.keys();
                    while (appsIt.hasNext()) {
                        String appName = appsIt.next();
                        long s = apps.optLong(appName, 0);
                        if (s != 0) {
                            new ZipAppInfo();
                            ZipAppInfo oldAppInfo = zipGlobalConfig.getAppInfo(appName);
                            if (oldAppInfo != null) {
                                if (s <= oldAppInfo.s) {
                                    appInfoList.remove(appName);
                                }
                                oldAppInfo.s = s;
                            }
                        }
                    }
                } catch (JSONException e) {
                    JSONObject jSONObject = jsonObj;
                }
            }
        } catch (JSONException e2) {
        }
        ConfigManager.saveGlobalConfigToloc(zipGlobalConfig);
        return true;
    }
}
