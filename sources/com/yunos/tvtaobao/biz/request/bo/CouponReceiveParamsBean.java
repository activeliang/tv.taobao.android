package com.yunos.tvtaobao.biz.request.bo;

import java.util.ArrayList;
import java.util.List;

public class CouponReceiveParamsBean {
    private String behaviorType;
    private String city;
    private String couponAmount;
    private String couponId;
    private String couponRule;
    private String couponValidityEnd;
    private String couponValidityStart;
    private String itemId;
    private String itemName;
    private int itemNum = -1;
    private String itemSku;
    private String page;
    private String province;
    private List<String> referer;
    private String sellerId;
    private String shopId;
    private String shopName;

    public String getPage() {
        return this.page;
    }

    public void setPage(String page2) {
        this.page = page2;
    }

    public List<String> getReferer() {
        if (this.referer == null) {
            return new ArrayList();
        }
        return this.referer;
    }

    public void setReferer(List<String> referer2) {
        this.referer = referer2;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province2) {
        this.province = province2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName2) {
        this.shopName = shopName2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName2) {
        this.itemName = itemName2;
    }

    public String getCouponId() {
        return this.couponId;
    }

    public void setCouponId(String couponId2) {
        this.couponId = couponId2;
    }

    public String getCouponRule() {
        return this.couponRule;
    }

    public void setCouponRule(String couponRule2) {
        this.couponRule = couponRule2;
    }

    public String getCouponAmount() {
        return this.couponAmount;
    }

    public void setCouponAmount(String couponAmount2) {
        this.couponAmount = couponAmount2;
    }

    public String getCouponValidityStart() {
        return this.couponValidityStart;
    }

    public void setCouponValidityStart(String couponValidityStart2) {
        this.couponValidityStart = couponValidityStart2;
    }

    public String getCouponValidityEnd() {
        return this.couponValidityEnd;
    }

    public void setCouponValidityEnd(String couponValidityEnd2) {
        this.couponValidityEnd = couponValidityEnd2;
    }

    public int getItemNum() {
        return this.itemNum;
    }

    public void setItemNum(int itemNum2) {
        this.itemNum = itemNum2;
    }

    public String getItemSku() {
        return this.itemSku;
    }

    public void setItemSku(String itemSku2) {
        this.itemSku = itemSku2;
    }

    public String getBehaviorType() {
        return this.behaviorType;
    }

    public void setBehaviorType(String behaviorType2) {
        this.behaviorType = behaviorType2;
    }
}
