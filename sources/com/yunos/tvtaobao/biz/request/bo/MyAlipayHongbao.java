package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class MyAlipayHongbao implements Serializable {
    private static final long serialVersionUID = 597916177196960233L;
    private String amount;
    private String couponId;
    private String couponName;
    private String currentAmount;
    private String gmtActive;
    private String gmtCreate;
    private String gmtExpired;
    private String publisherName;
    private String templateNid;
    private String useArea;

    public String getAmount() {
        return this.amount;
    }

    public String getCouponId() {
        return this.couponId;
    }

    public String getCouponName() {
        return this.couponName;
    }

    public String getCurrentAmount() {
        return this.currentAmount;
    }

    public String getGmtActive() {
        return this.gmtActive;
    }

    public String getGmtCreate() {
        return this.gmtCreate;
    }

    public String getGmtExpired() {
        return this.gmtExpired;
    }

    public String getPublisherName() {
        return this.publisherName;
    }

    public String getUseArea() {
        return this.useArea;
    }

    public String getTemplateNid() {
        return this.templateNid;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }

    public void setCouponId(String couponId2) {
        this.couponId = couponId2;
    }

    public void setCouponName(String couponName2) {
        this.couponName = couponName2;
    }

    public void setCurrentAmount(String currentAmount2) {
        this.currentAmount = currentAmount2;
    }

    public void setGmtActive(String gmtActive2) {
        this.gmtActive = gmtActive2;
    }

    public void setGmtCreate(String gmtCreate2) {
        this.gmtCreate = gmtCreate2;
    }

    public void setGmtExpired(String gmtExpired2) {
        this.gmtExpired = gmtExpired2;
    }

    public void setPublisherName(String publisherName2) {
        this.publisherName = publisherName2;
    }

    public void setUseArea(String useArea2) {
        this.useArea = useArea2;
    }

    public void setTemplateNid(String templateNid2) {
        this.templateNid = templateNid2;
    }

    public String toString() {
        return "[ amount = " + this.amount + ", couponId = " + this.couponId + ", couponName = " + this.couponName + ", currentAmount = " + this.currentAmount + ", gmtActive = " + this.gmtActive + ", gmtCreate = " + this.gmtCreate + ", gmtExpired = " + this.gmtExpired + ", publisherName = " + this.publisherName + ", useArea = " + this.useArea + ", templateNid = " + this.templateNid;
    }
}
