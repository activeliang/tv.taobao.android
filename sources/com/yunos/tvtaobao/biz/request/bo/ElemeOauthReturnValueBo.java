package com.yunos.tvtaobao.biz.request.bo;

public class ElemeOauthReturnValueBo {
    private String hint;
    private String returnUrl;
    private String scene;
    private String trustLoginToken;

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public void setReturnUrl(String returnUrl2) {
        this.returnUrl = returnUrl2;
    }

    public String getScene() {
        return this.scene;
    }

    public void setScene(String scene2) {
        this.scene = scene2;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint2) {
        this.hint = hint2;
    }

    public String getTrustLoginToken() {
        return this.trustLoginToken;
    }

    public void setTrustLoginToken(String trustLoginToken2) {
        this.trustLoginToken = trustLoginToken2;
    }
}
