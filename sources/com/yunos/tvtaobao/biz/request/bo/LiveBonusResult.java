package com.yunos.tvtaobao.biz.request.bo;

public class LiveBonusResult {
    private String amount;
    private String amountDoor;
    private String couponType;
    private String isLifeGroup;
    private String message;
    private String promotionName;
    private String remain;
    private String shopName;
    private String status;
    private String type;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getCouponType() {
        return this.couponType;
    }

    public void setCouponType(String couponType2) {
        this.couponType = couponType2;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount2) {
        this.amount = amount2;
    }

    public String getAmountDoor() {
        return this.amountDoor;
    }

    public void setAmountDoor(String amountDoor2) {
        this.amountDoor = amountDoor2;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName2) {
        this.shopName = shopName2;
    }

    public String getPromotionName() {
        return this.promotionName;
    }

    public void setPromotionName(String promotionName2) {
        this.promotionName = promotionName2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public String getIsLifeGroup() {
        return this.isLifeGroup;
    }

    public void setIsLifeGroup(String isLifeGroup2) {
        this.isLifeGroup = isLifeGroup2;
    }

    public String getRemain() {
        return this.remain;
    }

    public void setRemain(String remain2) {
        this.remain = remain2;
    }
}
