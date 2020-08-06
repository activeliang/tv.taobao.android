package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class Coupon implements Serializable {
    private static final long serialVersionUID = -8085336548214928590L;
    private String activityFlag;
    private String bgColor;
    private String condition;
    private String couponId;
    private String couponName;
    private int couponType;
    private String discount;
    private String endDay;
    private String endTime;
    private String logoUrl;
    private int modelType = 0;
    private long sellerId;
    private String source = "";
    private String startDay;
    private String startTime;
    private int status;
    private String supplier;

    public String getCouponName() {
        return this.couponName;
    }

    public void setCouponName(String couponName2) {
        this.couponName = couponName2;
    }

    public String getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String bgColor2) {
        this.bgColor = bgColor2;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier2) {
        this.supplier = supplier2;
    }

    public String getActivityFlag() {
        return this.activityFlag;
    }

    public void setActivityFlag(String activityFlag2) {
        this.activityFlag = activityFlag2;
    }

    public String getDiscount() {
        return this.discount;
    }

    public void setDiscount(String discount2) {
        this.discount = discount2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition2) {
        this.condition = condition2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public long getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(long sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getStartDay() {
        return this.startDay;
    }

    public void setStartDay(String startDay2) {
        this.startDay = startDay2;
    }

    public String getEndDay() {
        return this.endDay;
    }

    public void setEndDay(String endDay2) {
        this.endDay = endDay2;
    }

    public int getModelType() {
        return this.modelType;
    }

    public void setModelType(int modelType2) {
        this.modelType = modelType2;
    }

    public String getCouponId() {
        return this.couponId;
    }

    public void setCouponId(String couponId2) {
        this.couponId = couponId2;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public void setLogoUrl(String logoUrl2) {
        this.logoUrl = logoUrl2;
    }

    public int getCouponType() {
        return this.couponType;
    }

    public void setCouponType(int couponType2) {
        this.couponType = couponType2;
    }
}
