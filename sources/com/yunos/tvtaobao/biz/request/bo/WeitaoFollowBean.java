package com.yunos.tvtaobao.biz.request.bo;

public class WeitaoFollowBean {
    private String accountId;
    private boolean dynamic;
    private boolean follow;
    private boolean quiet;

    public boolean isFollow() {
        return this.follow;
    }

    public void setFollow(boolean follow2) {
        this.follow = follow2;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    public void setDynamic(boolean dynamic2) {
        this.dynamic = dynamic2;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId2) {
        this.accountId = accountId2;
    }

    public boolean isQuiet() {
        return this.quiet;
    }

    public void setQuiet(boolean quiet2) {
        this.quiet = quiet2;
    }
}
