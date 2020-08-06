package com.yunos.tvtaobao.biz.request.bo;

public class LiveFollowResult {
    private String accountName;
    private String followAccount;
    private String subscribe;
    private String toastMsg;

    public void setAccountName(String accountName2) {
        this.accountName = accountName2;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setFollowAccount(String followAccount2) {
        this.followAccount = followAccount2;
    }

    public String getFollowAccount() {
        return this.followAccount;
    }

    public void setSubscribe(String subscribe2) {
        this.subscribe = subscribe2;
    }

    public String getSubscribe() {
        return this.subscribe;
    }

    public void setToastMsg(String toastMsg2) {
        this.toastMsg = toastMsg2;
    }

    public String getToastMsg() {
        return this.toastMsg;
    }
}
