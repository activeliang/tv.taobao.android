package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class BuildOrderRequestBo implements Serializable {
    private static final long serialVersionUID = -5731049163432947160L;
    private String activityId;
    private boolean buyNow;
    private String buyParam;
    private String cartIds;
    private String deliveryId;
    private String extParams;
    private boolean isPreSell;
    private boolean isSettlementAlone;
    private String itemId;
    private String mFrom;
    private int quantity;
    private String serviceId;
    private String skuId;
    private String tagId;
    private String tgKey;

    public String getTagId() {
        return this.tagId;
    }

    public void setTagId(String tagId2) {
        this.tagId = tagId2;
    }

    public String getFrom() {
        return this.mFrom;
    }

    public void setFrom(String mFrom2) {
        this.mFrom = mFrom2;
    }

    public String getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(String deliveryId2) {
        this.deliveryId = deliveryId2;
    }

    public String getCartIds() {
        return this.cartIds;
    }

    public void setCartIds(String cartIds2) {
        this.cartIds = cartIds2;
    }

    public boolean isBuyNow() {
        return this.buyNow;
    }

    public void setBuyNow(boolean buyNow2) {
        this.buyNow = buyNow2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId2) {
        this.serviceId = serviceId2;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId2) {
        this.activityId = activityId2;
    }

    public String getTgKey() {
        return this.tgKey;
    }

    public void setTgKey(String tgKey2) {
        this.tgKey = tgKey2;
    }

    public boolean isSettlementAlone() {
        return this.isSettlementAlone;
    }

    public void setSettlementAlone(boolean isSettlementAlone2) {
        this.isSettlementAlone = isSettlementAlone2;
    }

    public String getBuyParam() {
        return this.buyParam;
    }

    public void setBuyParam(String buyParam2) {
        this.buyParam = buyParam2;
    }

    public String getExtParams() {
        return this.extParams;
    }

    public void setExtParams(String extParams2) {
        this.extParams = extParams2;
    }

    public boolean isPreSell() {
        return this.isPreSell;
    }

    public void setPreSell(boolean preSell) {
        this.isPreSell = preSell;
    }

    public String toString() {
        return "BuildOrderRequestBo{deliveryId='" + this.deliveryId + '\'' + ", cartIds='" + this.cartIds + '\'' + ", buyNow=" + this.buyNow + ", itemId='" + this.itemId + '\'' + ", quantity=" + this.quantity + ", skuId='" + this.skuId + '\'' + ", serviceId='" + this.serviceId + '\'' + ", activityId='" + this.activityId + '\'' + ", tgKey='" + this.tgKey + '\'' + ", isSettlementAlone=" + this.isSettlementAlone + ", buyParam='" + this.buyParam + '\'' + ", mFrom='" + this.mFrom + '\'' + ", extParams='" + this.extParams + '\'' + ", isPreSell=" + this.isPreSell + '}';
    }
}
