package com.yunos.tvtaobao.biz.request.bo;

public class RebateBo {
    private String coupon;
    private String couponMessage;
    private String discntPrice;
    private String icon;
    private String itemId;
    private boolean mjf;
    private String outPreferentialId;
    private String picUrl;
    private String tagId;

    public void setCoupon(String coupon2) {
        this.coupon = coupon2;
    }

    public String getCoupon() {
        return this.coupon;
    }

    public void setCouponMessage(String couponMessage2) {
        this.couponMessage = couponMessage2;
    }

    public String getCouponMessage() {
        return this.couponMessage;
    }

    public void setDiscntPrice(String discntPrice2) {
        this.discntPrice = discntPrice2;
    }

    public String getDiscntPrice() {
        return this.discntPrice;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setOutPreferentialId(String outPreferentialId2) {
        this.outPreferentialId = outPreferentialId2;
    }

    public String getOutPreferentialId() {
        return this.outPreferentialId;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setTagId(String tagId2) {
        this.tagId = tagId2;
    }

    public String getTagId() {
        return this.tagId;
    }

    public boolean isMjf() {
        return this.mjf;
    }

    public void setMjf(boolean mjf2) {
        this.mjf = mjf2;
    }
}
