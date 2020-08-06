package com.yunos.tvtaobao.biz.request.bo;

public class ElemeRecommendAccount {
    private String bindUserToken;
    private boolean canChoose;
    private String imageUrl;
    private String mobile;
    private String nick;
    private boolean upgraded;
    private String userId;
    private boolean vip;

    public String getBindUserToken() {
        return this.bindUserToken;
    }

    public void setBindUserToken(String bindUserToken2) {
        this.bindUserToken = bindUserToken2;
    }

    public boolean isCanChoose() {
        return this.canChoose;
    }

    public void setCanChoose(boolean canChoose2) {
        this.canChoose = canChoose2;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl2) {
        this.imageUrl = imageUrl2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public boolean isUpgraded() {
        return this.upgraded;
    }

    public void setUpgraded(boolean upgraded2) {
        this.upgraded = upgraded2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public boolean isVip() {
        return this.vip;
    }

    public void setVip(boolean vip2) {
        this.vip = vip2;
    }
}
