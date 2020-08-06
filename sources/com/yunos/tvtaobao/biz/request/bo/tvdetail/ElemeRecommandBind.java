package com.yunos.tvtaobao.biz.request.bo.tvdetail;

public class ElemeRecommandBind {
    private String appName;
    private String appVersion;
    private String bindUserToken;
    private String locale;
    private String miniAppId;
    private String requestToken;
    private String sdkTraceId;
    private String sdkVersion;
    private String utdid;

    public String getBindUserToken() {
        return this.bindUserToken;
    }

    public void setBindUserToken(String bindUserToken2) {
        this.bindUserToken = bindUserToken2;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion2) {
        this.appVersion = appVersion2;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale2) {
        this.locale = locale2;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String sdkVersion2) {
        this.sdkVersion = sdkVersion2;
    }

    public String getSdkTraceId() {
        return this.sdkTraceId;
    }

    public void setSdkTraceId(String sdkTraceId2) {
        this.sdkTraceId = sdkTraceId2;
    }

    public String getMiniAppId() {
        return this.miniAppId;
    }

    public void setMiniAppId(String miniAppId2) {
        this.miniAppId = miniAppId2;
    }

    public String getRequestToken() {
        return this.requestToken;
    }

    public void setRequestToken(String requestToken2) {
        this.requestToken = requestToken2;
    }

    public String getUtdid() {
        return this.utdid;
    }

    public void setUtdid(String utdid2) {
        this.utdid = utdid2;
    }
}
