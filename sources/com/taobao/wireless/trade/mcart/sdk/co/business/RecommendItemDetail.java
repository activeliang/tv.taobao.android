package com.taobao.wireless.trade.mcart.sdk.co.business;

import java.util.HashMap;

public class RecommendItemDetail {
    private String categoryId;
    private String commentTimes;
    private HashMap extMap;
    private String itemId;
    private String itemName;
    private String monthSellCount;
    private String pic;
    private String price;
    private String promotionPrice;
    private String rate;
    private String sellCount;
    private String sellerId;
    private String url;

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

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId2) {
        this.categoryId = categoryId2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public HashMap getExtMap() {
        return this.extMap;
    }

    public void setExtMap(HashMap extMap2) {
        this.extMap = extMap2;
    }

    public String getPromotionPrice() {
        return this.promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice2) {
        this.promotionPrice = promotionPrice2;
    }

    public String getCommentTimes() {
        return this.commentTimes;
    }

    public void setCommentTimes(String commentTimes2) {
        this.commentTimes = commentTimes2;
    }

    public String getSellCount() {
        return this.sellCount;
    }

    public void setSellCount(String sellCount2) {
        this.sellCount = sellCount2;
    }

    public String getMonthSellCount() {
        return this.monthSellCount;
    }

    public void setMonthSellCount(String monthSellCount2) {
        this.monthSellCount = monthSellCount2;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getRate() {
        return this.rate;
    }

    public void setRate(String rate2) {
        this.rate = rate2;
    }
}
