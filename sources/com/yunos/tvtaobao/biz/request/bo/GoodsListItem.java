package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class GoodsListItem implements Serializable {
    private static final long serialVersionUID = 138560600867817904L;
    private String auctionId;
    private String auctionType;
    private String hdfk;
    private String picUrl;
    private String quantity;
    private String reservePrice;
    private String salePrice;
    private int sold;
    private String title;
    private String totalSoldQuantity;

    public String getAuctionId() {
        return this.auctionId;
    }

    public void setAuctionId(String auctionId2) {
        this.auctionId = auctionId2;
    }

    public String getAuctionType() {
        return this.auctionType;
    }

    public void setAuctionType(String auctionType2) {
        this.auctionType = auctionType2;
    }

    public String getHdfk() {
        return this.hdfk;
    }

    public void setHdfk(String hdfk2) {
        this.hdfk = hdfk2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getReservePrice() {
        return this.reservePrice;
    }

    public void setReservePrice(String reservePrice2) {
        this.reservePrice = reservePrice2;
    }

    public String getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(String salePrice2) {
        this.salePrice = salePrice2;
    }

    public int getSold() {
        return this.sold;
    }

    public void setSold(int sold2) {
        this.sold = sold2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getTotalSoldQuantity() {
        return this.totalSoldQuantity;
    }

    public void setTotalSoldQuantity(String totalSoldQuantity2) {
        this.totalSoldQuantity = totalSoldQuantity2;
    }
}
