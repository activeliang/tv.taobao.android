package android.taobao.windvane.packageapp;

import android.os.Build;
import android.taobao.windvane.WindvaneException;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpConnector;
import android.taobao.windvane.connect.HttpRequest;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class WVPackageAppConfig implements WVPackageAppConfigInterface {
    /* access modifiers changed from: private */
    public static final String TAG = WVPackageAppConfig.class.getSimpleName();
    private static volatile ZipGlobalConfig config = null;
    private long lastUpdateTime = 0;

    public ZipGlobalConfig getGlobalConfig() {
        ZipGlobalConfig zipGlobalConfig;
        synchronized (TAG) {
            if (config == null) {
                String data = ZipAppFileManager.getInstance().readGlobalConfig(false);
                try {
                    config = ConfigDataUtils.parseGlobalConfig(data);
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "PackageAppforDebug 首次总控解析成功 data:【" + data + "】");
                    }
                } catch (Exception e) {
                    TaoLog.e(TAG, "PackageAppforDebug 总控解析本地总控文件失败 :【 " + e.getMessage() + "】");
                }
                if (config == null) {
                    config = new ZipGlobalConfig();
                }
            }
            zipGlobalConfig = config;
        }
        return zipGlobalConfig;
    }

    public void updateGlobalConfig(boolean forceUpdate, ValueCallback<ZipGlobalConfig> success, ValueCallback<WindvaneException> error, String snapshortN, String defaultUrl) {
        if (Build.VERSION.SDK_INT > 11 && WVCommonConfig.commonConfig.packageAppStatus >= 2) {
            long now = System.currentTimeMillis();
            if (forceUpdate || this.lastUpdateTime == 0 || now - this.lastUpdateTime >= 300000) {
                this.lastUpdateTime = now;
                final String str = defaultUrl;
                final String str2 = snapshortN;
                final ValueCallback<WindvaneException> valueCallback = error;
                final ValueCallback<ZipGlobalConfig> valueCallback2 = success;
                WVThreadPool.getInstance().execute(new Runnable() {
                    public void run() {
                        String v = "0";
                        String configUrl = str;
                        long configUpdateTime = ConfigStorage.getLongVal(WVConfigManager.SPNAME_CONFIG, "package_updateTime", 0);
                        if (TextUtils.isEmpty(str)) {
                            String snapshort = str2;
                            long useTime = System.currentTimeMillis() - configUpdateTime;
                            if (useTime > ((long) WVCommonConfig.commonConfig.recoveryInterval) || useTime < 0) {
                                v = "0";
                                snapshort = "0";
                                ConfigStorage.putLongVal(WVConfigManager.SPNAME_CONFIG, "package_updateTime", System.currentTimeMillis());
                            } else if (WVPackageAppConfig.this.getGlobalConfig() != null) {
                                v = WVPackageAppConfig.this.getGlobalConfig().v;
                            }
                            configUrl = WVConfigManager.getInstance().getConfigUrl("5", v, WVConfigUtils.getTargetValue(), snapshort);
                        }
                        new HttpConnector().syncConnect(new HttpRequest(configUrl), new HttpConnectListener<HttpResponse>() {
                            public void onFinish(HttpResponse data, int token) {
                                ZipGlobalConfig onlineConfig = null;
                                if (data != null) {
                                    try {
                                        if (data.getData() != null) {
                                            if (TaoLog.getLogStatus()) {
                                                TaoLog.d(WVPackageAppConfig.TAG, "PackageAppforDebug 下载总控配置文件成功 data:【" + new String(data.getData()) + "】");
                                            }
                                            if (WVMonitorService.getPackageMonitorInterface() != null) {
                                                long currentTime = System.currentTimeMillis();
                                                Map<String, String> headers = data.getHeaders();
                                                String age = headers.get("age");
                                                String date = headers.get(HttpConnector.DATE);
                                                long diffTime = 0;
                                                if (!TextUtils.isEmpty(age)) {
                                                    diffTime = Long.valueOf(age).longValue() * 1000;
                                                }
                                                if (!TextUtils.isEmpty(date)) {
                                                    diffTime += CommonUtils.parseDate(date);
                                                }
                                                if (currentTime > diffTime) {
                                                    WVMonitorService.getPackageMonitorInterface().uploadDiffTimeTime(currentTime - diffTime);
                                                }
                                            }
                                            onlineConfig = ConfigDataUtils.parseGlobalConfig(new String(data.getData(), ZipAppConstants.DEFAULT_ENCODING));
                                            if (onlineConfig != null && !onlineConfig.isAvailableData()) {
                                                valueCallback.onReceiveValue(new WindvaneException());
                                                return;
                                            }
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        TaoLog.e(WVPackageAppConfig.TAG, "PackageAppforDebug 总控文件编码异常 encoding error:【" + e.getMessage() + "】");
                                    } catch (Exception e2) {
                                        TaoLog.e(WVPackageAppConfig.TAG, "PackageAppforDebug 总控文件解析异常 fail: " + e2.getMessage());
                                        if (e2 instanceof WindvaneException) {
                                            valueCallback.onReceiveValue((WindvaneException) e2);
                                            return;
                                        } else {
                                            valueCallback.onReceiveValue(new WindvaneException((Throwable) e2, ZipAppResultCode.ERR_APPS_CONFIG_PARSE));
                                            return;
                                        }
                                    }
                                }
                                if (onlineConfig == null) {
                                    TaoLog.d(WVPackageAppConfig.TAG, "PackageAppforDebug startUpdateApps: GlobalConfig file parse error or invalid!");
                                    valueCallback.onReceiveValue(new WindvaneException("GlobalConfig file parse error or invalid", ZipAppResultCode.ERR_APPS_CONFIG_PARSE));
                                    return;
                                }
                                valueCallback2.onReceiveValue(onlineConfig);
                            }

                            public void onError(int code, String message) {
                                valueCallback.onReceiveValue(new WindvaneException());
                                TaoLog.d(WVPackageAppConfig.TAG, "update package failed! : " + message);
                                super.onError(code, message);
                            }
                        });
                    }
                });
                return;
            }
            TaoLog.d(TAG, "PackageAppforDebug 总控更新时机未到(非强制更新或间隔不超过5分钟)");
        }
    }

    public boolean saveLocalConfig(ZipGlobalConfig localConfig) {
        config = localConfig;
        if (localConfig == null) {
            return false;
        }
        try {
            return ZipAppFileManager.getInstance().saveGlobalConfig(ZipAppUtils.parseGlobalConfig2String(localConfig).getBytes(ZipAppConstants.DEFAULT_ENCODING), false);
        } catch (UnsupportedEncodingException e) {
            TaoLog.e(TAG, "PackageAppforDebug fail to save global config to disk");
            return false;
        }
    }
}
