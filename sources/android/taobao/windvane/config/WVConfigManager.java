package android.taobao.windvane.config;

import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.monitor.WVConfigMonitorInterface;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONObject;

public class WVConfigManager {
    public static final String CONFIGNAME_COMMON = "common";
    public static final String CONFIGNAME_CUSTOM = "customs";
    public static final String CONFIGNAME_DOMAIN = "domain";
    public static final String CONFIGNAME_MONITOR = "monitor";
    public static final String CONFIGNAME_PACKAGE = "package";
    public static final String CONFIGNAME_PREFIXES = "prefixes";
    public static final String CONFIG_UPDATETIME = "_updateTime";
    public static final String SPNAME_CONFIG = "wv_main_config";
    private static final String TAG = "WVConfigManager";
    /* access modifiers changed from: private */
    public static volatile WVConfigManager instance = null;
    private static long updateInterval = 300000;
    private static long updateTime = 0;
    private boolean enableUpdateConfig = true;
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, WVConfigHandler> mConfigMap = null;
    /* access modifiers changed from: private */
    public int updateConfigCount = 0;

    public enum WVConfigUpdateFromType {
        WVConfigUpdateFromTypeCustom,
        WVConfigUpdateFromTypeActive,
        WVConfigUpdateFromTypeFinish,
        WVConfigUpdateFromTypePush,
        WVConfigUpdateFromTypeLaunch
    }

    static /* synthetic */ int access$304(WVConfigManager x0) {
        int i = x0.updateConfigCount + 1;
        x0.updateConfigCount = i;
        return i;
    }

    public static class WVPageEventListener implements WVEventListener {
        public WVEventResult onEvent(int id, WVEventContext ctx, Object... obj) {
            switch (id) {
                case 1002:
                    WVConfigManager.instance.updateConfig(WVConfigUpdateFromType.WVConfigUpdateFromTypeFinish);
                    return null;
                case WVEventId.PAGE_onResume /*3002*/:
                    WVConfigManager.instance.updateConfig(WVConfigUpdateFromType.WVConfigUpdateFromTypeActive);
                    return null;
                default:
                    return null;
            }
        }
    }

    public static WVConfigManager getInstance() {
        if (instance == null) {
            synchronized (WVConfigManager.class) {
                if (instance == null) {
                    instance = new WVConfigManager();
                    instance.mConfigMap = new ConcurrentHashMap<>();
                    WVEventService.getInstance().addEventListener(new WVPageEventListener());
                }
            }
        }
        return instance;
    }

    public void registerHandler(String key, WVConfigHandler handler) {
        this.mConfigMap.put(key, handler);
    }

    public void setUpdateConfigEnable(boolean enable) {
        this.enableUpdateConfig = enable;
    }

    private void updateImmediately(final WVConfigUpdateFromType fromType) {
        if (this.enableUpdateConfig && WVConfigUtils.checkAppKeyAvailable()) {
            final long startTime = System.currentTimeMillis();
            ConnectManager.getInstance().connect(getConfigUrl("0", "0", WVConfigUtils.getTargetValue(), "0"), (HttpConnectListener<HttpResponse>) new HttpConnectListener<HttpResponse>() {
                public void onFinish(HttpResponse data, int token) {
                    int i;
                    long updateTime = System.currentTimeMillis() - startTime;
                    boolean isSuccess = true;
                    if (data != null) {
                        try {
                            String content = new String(data.getData(), "utf-8");
                            JSONObject jsObj = null;
                            ApiResponse response = new ApiResponse();
                            if (response.parseJsonResult(content).success) {
                                jsObj = response.data;
                            }
                            if (!(jsObj == null || WVConfigManager.this.mConfigMap == null)) {
                                for (String key : WVConfigManager.this.mConfigMap.keySet()) {
                                    WVConfigManager.this.doUpdateByKey(key, jsObj.optString(key, "0"), (String) null, fromType);
                                }
                                WVMonitorService.getConfigMonitor().didOccurUpdateConfigSuccess("entry");
                            }
                        } catch (Exception e) {
                            isSuccess = false;
                            WVMonitorService.getConfigMonitor().didOccurUpdateConfigError("entry", WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal(), "update entry error : " + e.getMessage());
                            TaoLog.d(WVConfigManager.TAG, "updateImmediately failed!");
                        }
                        WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
                        int ordinal = fromType.ordinal();
                        if (isSuccess) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        configMonitor.didUpdateConfig("entry", ordinal, updateTime, i, WVConfigManager.this.mConfigMap.size());
                    }
                }

                public void onError(int code, String message) {
                    TaoLog.d(WVConfigManager.TAG, "update entry failed! : " + message);
                    WVMonitorService.getConfigMonitor().didOccurUpdateConfigError("entry-NoNetwork", WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR.ordinal(), message);
                    super.onError(code, message);
                }
            });
        }
    }

    public void updateConfig(WVConfigUpdateFromType fromType) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - updateTime > updateInterval && WVConfigUtils.checkAppKeyAvailable()) {
            updateTime = currentTime;
            updateImmediately(fromType);
        }
    }

    public void updateConfig(String configName, String version, String defaultUrl, WVConfigUpdateFromType fromType) {
        if (!TextUtils.isEmpty(configName) && !TextUtils.isEmpty(version)) {
            doUpdateByKey(configName, version, defaultUrl, fromType);
        }
    }

    /* access modifiers changed from: private */
    public void doUpdateByKey(String key, String newValue, String defaultUrl, WVConfigUpdateFromType fromType) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(newValue)) {
            boolean needUpdate = true;
            if (TextUtils.isEmpty(defaultUrl)) {
                try {
                    needUpdate = WVConfigUtils.isNeedUpdate(newValue, key);
                } catch (Exception e) {
                    needUpdate = false;
                }
            }
            if (needUpdate) {
                final WVConfigHandler handler = this.mConfigMap.get(key);
                if (handler != null) {
                    if (!handler.getUpdateStatus() || System.currentTimeMillis() - updateTime >= updateInterval) {
                        handler.setUpdateStatus(true);
                        handler.setSnapshotN(newValue);
                        final long startTime = System.currentTimeMillis();
                        final String str = key;
                        final String str2 = newValue;
                        final WVConfigUpdateFromType wVConfigUpdateFromType = fromType;
                        handler.update(defaultUrl, new WVConfigUpdateCallback() {
                            public void updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS updateInfo, int updateCount) {
                                int i = 0;
                                handler.setUpdateStatus(false);
                                WVConfigManager.access$304(WVConfigManager.this);
                                if (WVConfigManager.this.updateConfigCount >= WVConfigManager.this.mConfigMap.size()) {
                                    int unused = WVConfigManager.this.updateConfigCount = 0;
                                    WVEventService.getInstance().onEvent(WVEventId.CONFIG_UPLOAD_COMPLETE);
                                }
                                boolean isSuccess = WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS.equals(updateInfo);
                                WVConfigMonitorInterface moniter = WVMonitorService.getConfigMonitor();
                                if (isSuccess) {
                                    ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, str, str2);
                                    if (moniter != null) {
                                        moniter.didOccurUpdateConfigSuccess(str);
                                    }
                                } else if (moniter != null) {
                                    moniter.didOccurUpdateConfigError(str, updateInfo.ordinal(), str + SymbolExpUtil.SYMBOL_COLON + str2 + SymbolExpUtil.SYMBOL_COLON + updateInfo);
                                }
                                if (moniter != null) {
                                    long updateTime = System.currentTimeMillis() - startTime;
                                    WVConfigMonitorInterface configMonitor = WVMonitorService.getConfigMonitor();
                                    String str = str;
                                    int ordinal = wVConfigUpdateFromType.ordinal();
                                    if (isSuccess) {
                                        i = 1;
                                    }
                                    configMonitor.didUpdateConfig(str, ordinal, updateTime, i, updateCount);
                                }
                                TaoLog.d(WVConfigManager.TAG, "isUpdateSuccess " + str + " : " + updateInfo);
                            }
                        });
                    } else {
                        return;
                    }
                }
            } else {
                this.updateConfigCount++;
            }
            if (this.updateConfigCount >= this.mConfigMap.size()) {
                this.updateConfigCount = 0;
                WVEventService.getInstance().onEvent(WVEventId.CONFIG_UPLOAD_COMPLETE);
            }
        }
    }

    public WVConfigHandler registedHandler(String key) {
        if (this.mConfigMap == null) {
            return null;
        }
        return this.mConfigMap.get(key);
    }

    public void removeHandler(String key) {
        if (this.mConfigMap != null) {
            this.mConfigMap.remove(key);
        }
    }

    public void resetConfig() {
        if (this.mConfigMap != null) {
            for (String key : this.mConfigMap.keySet()) {
                ConfigStorage.putStringVal(SPNAME_CONFIG, key, "0");
            }
        }
        updateTime = 0;
    }

    public HashMap getConfigVersions() {
        HashMap versionHashMap = new HashMap();
        if (this.mConfigMap != null) {
            for (String key : this.mConfigMap.keySet()) {
                String version = ConfigStorage.getStringVal(SPNAME_CONFIG, key, "0");
                if (!version.contains(".")) {
                    Long vLong = Long.valueOf(Long.parseLong(version));
                    if (vLong.longValue() == 0) {
                        version = "NO VERSION";
                    } else if (vLong.longValue() == Long.MAX_VALUE) {
                        version = "CUSTOM VERION";
                    }
                }
                versionHashMap.put(key, version);
            }
        }
        return versionHashMap;
    }

    public void setUpdateInterval(long interval) {
        updateInterval = interval;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x004d, code lost:
        r8 = android.taobao.windvane.util.ConfigStorage.getStringVal(SPNAME_CONFIG, "abt", "a");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConfigUrl(java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String r9) {
        /*
            r5 = this;
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = android.taobao.windvane.config.GlobalConfig.getCdnConfigUrlPre()
            r1.append(r2)
            java.lang.String r2 = "5/windvane/"
            r1.append(r2)
            r1.append(r6)
            java.lang.String r2 = "/"
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = "-"
            r1.append(r2)
            r1.append(r9)
            java.lang.String r2 = "/"
            r1.append(r2)
            android.taobao.windvane.config.GlobalConfig r2 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r2 = r2.getAppKey()
            r1.append(r2)
            java.lang.String r2 = "-"
            r1.append(r2)
            java.lang.String r2 = android.taobao.windvane.config.WVConfigUtils.dealAppVersion()
            r1.append(r2)
            java.lang.String r2 = "/"
            r1.append(r2)
            if (r8 != 0) goto L_0x006a
            java.lang.String r2 = "wv_main_config"
            java.lang.String r3 = "abt"
            java.lang.String r4 = "a"
            java.lang.String r8 = android.taobao.windvane.util.ConfigStorage.getStringVal(r2, r3, r4)
            r2 = 0
            char r0 = r8.charAt(r2)
            r2 = 97
            if (r2 > r0) goto L_0x0067
            r2 = 99
            if (r0 <= r2) goto L_0x006a
        L_0x0067:
            java.lang.String r8 = "a"
        L_0x006a:
            r1.append(r8)
            java.lang.String r2 = "/settings.json"
            r1.append(r2)
            java.lang.String r2 = r1.toString()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.config.WVConfigManager.getConfigUrl(java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }
}
