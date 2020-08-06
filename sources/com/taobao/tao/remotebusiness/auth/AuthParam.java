package com.taobao.tao.remotebusiness.auth;

import mtopsdk.common.util.StringUtils;

public class AuthParam {
    public String apiInfo;
    public String bizParam;
    public String failInfo;
    public String openAppKey = "DEFAULT_AUTH";
    public boolean showAuthUI;

    public AuthParam(String openAppKey2, String bizParam2, boolean showAuthUI2) {
        if (StringUtils.isNotBlank(openAppKey2)) {
            this.openAppKey = openAppKey2;
        }
        this.bizParam = bizParam2;
        this.showAuthUI = showAuthUI2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("AuthParam{ openAppKey=").append(this.openAppKey).append(", bizParam=").append(this.bizParam).append(", showAuthUI=").append(this.showAuthUI).append(", apiInfo=").append(this.apiInfo).append(", failInfo=").append(this.failInfo).append("}");
        return builder.toString();
    }
}
