package com.tvtaobao.voicesdk.bean;

import java.io.Serializable;

public class BillDo implements Serializable {
    private String actualPaidFee;
    private String auctionPicUrl;
    private String auctionTitle;
    private String bizOrderId;
    private String gmtCreate;
    private String hasDetail;
    private String totalFee;

    public void setActualPaidFee(String actualPaidFee2) {
        this.actualPaidFee = actualPaidFee2;
    }

    public String getActualPaidFee() {
        return this.actualPaidFee;
    }

    public String getAuctionPicUrl() {
        return this.auctionPicUrl;
    }

    public void setAuctionPicUrl(String auctionPicUrl2) {
        this.auctionPicUrl = auctionPicUrl2;
    }

    public void setAuctionTitle(String auctionTitle2) {
        this.auctionTitle = auctionTitle2;
    }

    public String getAuctionTitle() {
        return this.auctionTitle;
    }

    public void setBizOrderId(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public String getBizOrderId() {
        return this.bizOrderId;
    }

    public void setGmtCreate(String gmtCreate2) {
        this.gmtCreate = gmtCreate2;
    }

    public String getGmtCreate() {
        return this.gmtCreate;
    }

    public void setHasDetail(String hasDetail2) {
        this.hasDetail = hasDetail2;
    }

    public String getHasDetail() {
        return this.hasDetail;
    }

    public void setTotalFee(String totalFee2) {
        this.totalFee = totalFee2;
    }

    public String getTotalFee() {
        return this.totalFee;
    }
}
