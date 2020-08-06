package com.tvtaobao.voicesdk.bean;

import java.util.List;

public class PriceModel {
    private String balance;
    private List<PriceDo> data;
    private String itemId;
    private String picUrl;
    private String price;
    private String title;
    private String trendRenderPicUrl;

    public void setBalance(String balance2) {
        this.balance = balance2;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setData(List<PriceDo> data2) {
        this.data = data2;
    }

    public List<PriceDo> getData() {
        return this.data;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTrendRenderPicUrl() {
        return this.trendRenderPicUrl;
    }

    public void setTrendRenderPicUrl(String trendRenderPicUrl2) {
        this.trendRenderPicUrl = trendRenderPicUrl2;
    }
}
