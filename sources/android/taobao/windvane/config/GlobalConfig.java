package android.taobao.windvane.config;

import android.app.Application;
import android.text.TextUtils;

public class GlobalConfig {
    public static final String DEFAULT_TTID = "hybrid@windvane_android_8.0.0";
    public static final String DEFAULT_UA = " WindVane/8.0.0";
    public static final String VERSION = "8.0.0";
    private static GlobalConfig config;
    public static Application context;
    public static EnvEnum env = EnvEnum.ONLINE;
    private String appKey;
    private String appSecret;
    private String appTag;
    private String appVersion;
    private String deviceId;
    private String groupName;
    private String groupVersion;
    private String imei;
    private String imsi;
    private String ttid;
    private String[] ucsdkappkeySec = null;

    private GlobalConfig() {
    }

    public static synchronized GlobalConfig getInstance() {
        GlobalConfig globalConfig;
        synchronized (GlobalConfig.class) {
            if (config == null) {
                config = new GlobalConfig();
            }
            globalConfig = config;
        }
        return globalConfig;
    }

    public boolean initParams(WVAppParams params) {
        if (params == null) {
            return false;
        }
        if (TextUtils.isEmpty(params.appKey)) {
            throw new NullPointerException("initParams error, appKey is empty");
        }
        if (TextUtils.isEmpty(params.ttid)) {
            this.ttid = DEFAULT_TTID;
        } else {
            this.ttid = params.ttid;
        }
        this.imei = params.imei;
        this.imsi = params.imsi;
        this.deviceId = params.deviceId;
        this.appKey = params.appKey;
        this.appSecret = params.appSecret;
        this.appTag = params.appTag;
        this.appVersion = params.appVersion;
        setUcsdkappkeySec(params.ucsdkappkeySec);
        return true;
    }

    public String getTtid() {
        return this.ttid;
    }

    public String getImei() {
        return this.imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public String getAppTag() {
        return this.appTag;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public String[] getUcsdkappkeySec() {
        return this.ucsdkappkeySec;
    }

    public void setUcsdkappkeySec(String[] key) {
        if (key != null) {
            this.ucsdkappkeySec = key;
        }
    }

    public static String getMtopUrl() {
        return "http://api." + env.getValue() + ".taobao.com/rest/api3.do";
    }

    public static String getCdnConfigUrlPre() {
        return getH5Host() + "/bizcache/";
    }

    public static String getH5Host() {
        return (EnvEnum.ONLINE.equals(env) ? "https://h5." : "http://h5.") + env.getValue() + ".taobao.com";
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName2) {
        this.groupName = groupName2;
    }

    public String getGroupVersion() {
        return this.groupVersion;
    }

    public void setGroupVersion(String groupVersion2) {
        this.groupVersion = groupVersion2;
    }
}
