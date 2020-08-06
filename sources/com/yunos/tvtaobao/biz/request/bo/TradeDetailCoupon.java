package com.yunos.tvtaobao.biz.request.bo;

public class TradeDetailCoupon {
    private String coupon;
    private String fee;
    private String itemId;
    private String message;
    private String outPreferentialId;
    private String picUrl;
    private String skuId;
    private String tagId;
    private String tvoptions;

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCoupon(String coupon2) {
        this.coupon = coupon2;
    }

    public String getCoupon() {
        return this.coupon;
    }

    public void setFee(String fee2) {
        this.fee = fee2;
    }

    public String getFee() {
        return this.fee;
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

    public void setTagId(String tagId2) {
        this.tagId = tagId2;
    }

    public String getTagId() {
        return this.tagId;
    }

    public void setTvoptions(String tvoptions2) {
        this.tvoptions = tvoptions2;
    }

    public String getTvoptions() {
        return this.tvoptions;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }
}
