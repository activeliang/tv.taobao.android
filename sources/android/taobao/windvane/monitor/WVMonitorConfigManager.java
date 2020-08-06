package android.taobao.windvane.monitor;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.ConnectManager;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.connect.api.ApiResponse;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WVMonitorConfigManager {
    /* access modifiers changed from: private */
    public static final String TAG = WVPackageMonitorImpl.class.getSimpleName();
    private static volatile WVMonitorConfigManager instance = null;
    public WVMonitorConfig config = new WVMonitorConfig();

    public static WVMonitorConfigManager getInstance() {
        if (instance == null) {
            synchronized (WVMonitorConfigManager.class) {
                if (instance == null) {
                    instance = new WVMonitorConfigManager();
                }
            }
        }
        return instance;
    }

    private static class PageFinshWVEventListener implements WVEventListener {
        private PageFinshWVEventListener() {
        }

        public WVEventResult onEvent(int eventId, WVEventContext context, Object... objs) {
            switch (eventId) {
                case 1002:
                    try {
                        double rate = WVMonitorConfigManager.getInstance().config.perfCheckSampleRate;
                        String scriptUrl = WVMonitorConfigManager.getInstance().config.perfCheckURL;
                        if (TextUtils.isEmpty("scriptUrl") || rate <= Math.random()) {
                            return null;
                        }
                        context.webView.evaluateJavascript(String.format("(function(d){var s = d.createElement('script');s.src='%s';d.head.appendChild(s);})(document)", new Object[]{scriptUrl}));
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                default:
                    return null;
            }
        }
    }

    public void init() {
        try {
            String data = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, "monitorwv-data", "");
            if (!TextUtils.isEmpty(data)) {
                this.config = parseRule(data);
            }
        } catch (Exception e) {
        }
        WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_MONITOR, new WVConfigHandler() {
            public void update(String defaultUrl, WVConfigUpdateCallback callback) {
                WVMonitorConfigManager.this.updateMonitorConfig(callback, defaultUrl, getSnapshotN());
            }
        });
        WVEventService.getInstance().addEventListener(new PageFinshWVEventListener());
    }

    public WVMonitorConfig parseRule(String content) {
        try {
            JSONObject config2 = new JSONObject(content);
            WVMonitorConfig res = new WVMonitorConfig();
            res.v = config2.optString("v", "");
            if (TextUtils.isEmpty(res.v)) {
                return null;
            }
            res.stat.onLoad = config2.optLong("minLoadTime", 0);
            res.stat.onDomLoad = config2.optLong("minDomLoadTime", 0);
            res.stat.resTime = config2.optLong("minResTime", 0);
            res.stat.netstat = config2.optBoolean("reportNetStat", false);
            res.stat.resSample = config2.optInt("resSample", 100);
            res.isErrorBlacklist = config2.optString("errorType", "b").equals("b");
            JSONArray errorRules = config2.optJSONArray("errorRule");
            if (errorRules != null) {
                for (int i = 0; i < errorRules.length(); i++) {
                    JSONObject errorRule = errorRules.optJSONObject(i);
                    if (errorRule != null) {
                        res.errorRule.add(res.newErrorRuleInstance(errorRule.optString("url", ""), errorRule.optString("msg", ""), errorRule.optString("code", "")));
                    }
                }
            }
            res.perfCheckSampleRate = config2.optDouble("perfCheckSampleRate", ClientTraceData.b.f47a);
            res.perfCheckURL = config2.optString("perfCheckURL", "");
            return res;
        } catch (JSONException e) {
            TaoLog.e(TAG, "parseRule error. content=" + content);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public boolean needSaveConfig(String config2) {
        WVMonitorConfig newConfig;
        if (TextUtils.isEmpty(config2)) {
            return false;
        }
        JSONObject jsonObj = null;
        ApiResponse response = new ApiResponse();
        if (response.parseJsonResult(config2).success) {
            jsonObj = response.data;
        }
        if (jsonObj == null || (newConfig = parseRule(jsonObj.toString())) == null) {
            return false;
        }
        this.config = newConfig;
        return true;
    }

    /* access modifiers changed from: private */
    public void updateMonitorConfig(final WVConfigUpdateCallback callback, String defaultUrl, String snapshortN) {
        String monitorConfigUrl;
        if (WVCommonConfig.commonConfig.monitorStatus != 2) {
            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
            return;
        }
        if (TextUtils.isEmpty(defaultUrl)) {
            monitorConfigUrl = WVConfigManager.getInstance().getConfigUrl("3", this.config.v, WVConfigUtils.getTargetValue(), snapshortN);
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
                        if (WVMonitorConfigManager.this.needSaveConfig(content)) {
                            ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, "monitorwv-data", content);
                            callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 1);
                            return;
                        }
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.NO_VERSION, 0);
                    } catch (UnsupportedEncodingException e) {
                        callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.ENCODING_ERROR, 0);
                    }
                }
            }

            public void onError(int code, String message) {
                if (callback != null) {
                    callback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UNKNOWN_ERROR, 0);
                }
                TaoLog.d(WVMonitorConfigManager.TAG, "update moniter failed! : " + message);
                super.onError(code, message);
            }
        });
    }
}
