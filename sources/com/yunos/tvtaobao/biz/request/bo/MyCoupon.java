package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class MyCoupon implements Serializable {
    private static final long serialVersionUID = 4511096011130599716L;
    private String activtyDay;
    private String amount;
    private String bizType;
    private String canDelete;
    private String couponId;
    private String couponType;
    private String endDay;
    private String endTime;
    private String hasRead;
    private String limitedPrompt;
    private String shopLogo;
    private String spreadType;
    private String startTime;
    private String status;
    private String supplierId;
    private String title;
    private String useCondition;

    public String getCouponId() {
        return this.couponId;
    }

    public String getSupplierId() {
        return this.supplierId;
    }

    public String getCouponType() {
        return this.couponType;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getTitle() {
        return this.title;
    }

    public String getEndDay() {
        return this.endDay;
    }

    public String getStatus() {
        return this.status;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getUseCondition() {
        return this.useCondition;
    }

    public String getHasRead() {
        return this.hasRead;
    }

    public String getCanDelete() {
        return this.canDelete;
    }

    public String getShopLogo() {
        return this.shopLogo;
    }

    public String getLimitedPrompt() {
        return this.limitedPrompt;
    }

    public String getBizType() {
        return this.bizType;
    }

    public String getSpreadType() {
        return this.spreadType;
    }

    public String getActivtyDay() {
        return this.activtyDay;
    }

    public void setCouponId(String couponId2) {
        this.couponId = couponId2;
    }

    public void setSupplierId(String supplierId2) {
        this.supplierId = supplierId2;
    }

    public void setCouponType(String couponType2) {
        this.couponType = couponType2;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public void setEndDay(String endDay2) {
        this.endDay = endDay2;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }

    public void setUseCondition(String useCondition2) {
        this.useCondition = useCondition2;
    }

    public void setHasRead(String hasRead2) {
        this.hasRead = hasRead2;
    }

    public void setCanDelete(String canDelete2) {
        this.canDelete = canDelete2;
    }

    public void setShopLogo(String shopLogo2) {
        this.shopLogo = shopLogo2;
    }

    public void setLimitedPrompt(String limitedPrompt2) {
        this.limitedPrompt = limitedPrompt2;
    }

    public void setBizType(String bizType2) {
        this.bizType = bizType2;
    }

    public void setSpreadType(String spreadType2) {
        this.spreadType = spreadType2;
    }

    public void setActivtyDay(String activtyDay2) {
        this.activtyDay = activtyDay2;
    }

    public String toString() {
        return "[couponId = " + this.couponId + ", supplierId = " + this.supplierId + ",couponType = " + this.couponType + ", startTime = " + this.startTime + ", endTime = " + this.endTime + ", title = " + this.title + ", status = " + this.status + ", amount = " + this.amount + ", endDay = " + this.endDay + ", useCondition = " + this.useCondition + ", limitedPrompt = " + this.limitedPrompt + ", bizType = " + this.bizType;
    }
}
