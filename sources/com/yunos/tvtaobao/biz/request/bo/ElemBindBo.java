package com.yunos.tvtaobao.biz.request.bo;

public class ElemBindBo {
    private boolean success;
    private ElemBind user;

    public ElemBind getUser() {
        return this.user;
    }

    public void setUser(ElemBind user2) {
        this.user = user2;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success2) {
        this.success = success2;
    }
}
