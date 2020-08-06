package com.yunos.tvtaobao.biz.request.bo;

public class ValidateLotteryBean {
    private boolean capacity;
    private int usercount;
    private int uuidCount;

    public boolean isCapacity() {
        return this.capacity;
    }

    public void setCapacity(boolean capacity2) {
        this.capacity = capacity2;
    }

    public int getUsercount() {
        return this.usercount;
    }

    public void setUsercount(int usercount2) {
        this.usercount = usercount2;
    }

    public int getUuidCount() {
        return this.uuidCount;
    }

    public void setUuidCount(int uuidCount2) {
        this.uuidCount = uuidCount2;
    }
}
