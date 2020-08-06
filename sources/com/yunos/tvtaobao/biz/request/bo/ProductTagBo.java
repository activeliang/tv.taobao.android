package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class ProductTagBo implements Serializable {
    private String cart;
    private String coupon;
    private String couponFullMessage;
    private String couponMessage;
    private String couponType;
    private String icon;
    private boolean isPre;
    private String isVip;
    private String itemId;
    private String lastTraceKeyword;
    private String outPreferentialId;
    private String picUrl;
    private String picUrl2;
    private String pointBlacklisted;
    private double pointRate = 1.0d;
    private String pointSchemeId;
    private int position;
    private String tagId;

    public String getCouponType() {
        return this.couponType;
    }

    public void setCouponType(String couponType2) {
        this.couponType = couponType2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position2) {
        this.position = position2;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
    }

    public String getOutPreferentialId() {
        return this.outPreferentialId;
    }

    public void setOutPreferentialId(String outPreferentialId2) {
        this.outPreferentialId = outPreferentialId2;
    }

    public String getLastTraceKeyword() {
        return this.lastTraceKeyword;
    }

    public void setLastTraceKeyword(String lastTraceKeyword2) {
        this.lastTraceKeyword = lastTraceKeyword2;
    }

    public double getPointRate() {
        return this.pointRate;
    }

    public void setPointRate(double pointRate2) {
        this.pointRate = pointRate2;
    }

    public String getPointSchemeId() {
        return this.pointSchemeId;
    }

    public void setPointSchemeId(String pointSchemeId2) {
        this.pointSchemeId = pointSchemeId2;
    }

    public String getPointBlacklisted() {
        return this.pointBlacklisted;
    }

    public void setPointBlacklisted(String pointBlacklisted2) {
        this.pointBlacklisted = pointBlacklisted2;
    }

    public String getIsVip() {
        return this.isVip;
    }

    public void setIsVip(String isVip2) {
        this.isVip = isVip2;
    }

    public String getCart() {
        return this.cart;
    }

    public void setCart(String cart2) {
        this.cart = cart2;
    }

    public String getCoupon() {
        return this.coupon;
    }

    public void setCoupon(String coupon2) {
        this.coupon = coupon2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl3) {
        this.picUrl = picUrl3;
    }

    public String getCouponMessage() {
        return this.couponMessage;
    }

    public void setCouponMessage(String couponMessage2) {
        this.couponMessage = couponMessage2;
    }

    public boolean isPre() {
        return this.isPre;
    }

    public void setPre(boolean pre) {
        this.isPre = pre;
    }

    public String getTagId() {
        return this.tagId;
    }

    public void setTagId(String tagId2) {
        this.tagId = tagId2;
    }

    public String getPicUrl2() {
        return this.picUrl2;
    }

    public void setPicUrl2(String picUrl22) {
        this.picUrl2 = picUrl22;
    }

    public String getCouponFullMessage() {
        return this.couponFullMessage;
    }

    public void setCouponFullMessage(String couponFullMessage2) {
        this.couponFullMessage = couponFullMessage2;
    }
}
