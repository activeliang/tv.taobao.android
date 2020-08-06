package com.yunos.tvtaobao.biz.request.bo;

public class AddFollowResult {
    private String accountName;
    private String followAccount;
    private String subscribe;
    private String toastMsg;

    public String getAccountName() {
        return this.accountName == null ? "" : this.accountName;
    }

    public void setAccountName(String accountName2) {
        this.accountName = accountName2;
    }

    public String getFollowAccount() {
        return this.followAccount == null ? "" : this.followAccount;
    }

    public void setFollowAccount(String followAccount2) {
        this.followAccount = followAccount2;
    }

    public String getSubscribe() {
        return this.subscribe == null ? "" : this.subscribe;
    }

    public void setSubscribe(String subscribe2) {
        this.subscribe = subscribe2;
    }

    public String getToastMsg() {
        return this.toastMsg == null ? "" : this.toastMsg;
    }

    public void setToastMsg(String toastMsg2) {
        this.toastMsg = toastMsg2;
    }
}
