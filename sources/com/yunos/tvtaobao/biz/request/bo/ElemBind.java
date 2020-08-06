package com.yunos.tvtaobao.biz.request.bo;

public class ElemBind {
    private String avatar;
    private String id;
    private boolean loginMobile;
    private String mobile;
    private String nick;
    private String site;
    private int status;
    private String taobaoId;
    private String uccId;

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar2) {
        this.avatar = avatar2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public boolean isLoginMobile() {
        return this.loginMobile;
    }

    public void setLoginMobile(boolean loginMobile2) {
        this.loginMobile = loginMobile2;
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

    public String getSite() {
        return this.site;
    }

    public void setSite(String site2) {
        this.site = site2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String getTaobaoId() {
        return this.taobaoId;
    }

    public void setTaobaoId(String taobaoId2) {
        this.taobaoId = taobaoId2;
    }

    public String getUccId() {
        return this.uccId;
    }

    public void setUccId(String uccId2) {
        this.uccId = uccId2;
    }
}
