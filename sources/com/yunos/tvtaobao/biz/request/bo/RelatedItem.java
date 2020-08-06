package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class RelatedItem implements Serializable {
    private static final long serialVersionUID = 4098407391230509977L;
    private String itemId;
    private String picUrl;
    private String reservePrice;
    private String salePrice;
    private String sold;
    private String title;

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(String salePrice2) {
        this.salePrice = salePrice2;
    }

    public String getReservePrice() {
        return this.reservePrice;
    }

    public void setReservePrice(String reservePrice2) {
        this.reservePrice = reservePrice2;
    }

    public String getSold() {
        return this.sold;
    }

    public void setSold(String sold2) {
        this.sold = sold2;
    }
}
