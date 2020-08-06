package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class BuildOrderRebateBo {
    private String coupon;
    private String message;
    private String orderId;
    private String payment;
    private String picUrl;
    private List<TradeDetailCoupon> tradeDetailCoupon;

    public void setCoupon(String coupon2) {
        this.coupon = coupon2;
    }

    public String getCoupon() {
        return this.coupon;
    }

    public void setOrderId(String orderId2) {
        this.orderId = orderId2;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setPayment(String payment2) {
        this.payment = payment2;
    }

    public String getPayment() {
        return this.payment;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public void setTradeDetailCoupon(List<TradeDetailCoupon> tradeDetailCoupon2) {
        this.tradeDetailCoupon = tradeDetailCoupon2;
    }

    public List<TradeDetailCoupon> getTradeDetailCoupon() {
        return this.tradeDetailCoupon;
    }
}
