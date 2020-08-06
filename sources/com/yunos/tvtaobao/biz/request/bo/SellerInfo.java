package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class SellerInfo implements Serializable {
    private static final String TAG = "SellerInfo";
    private static final long serialVersionUID = 8507287277753520352L;
    private String city;
    private String collectorCount;
    private String id;
    private String isMall;
    private String nick;
    private String phone;
    private String phoneExt;
    private String picUrl;
    private String productCount;
    private String prov;
    private String rankNum;
    private String rankType;
    private String rateSum;
    private String sellerId;
    private shopDSRScore shopDSRScore;
    private String starts;
    private String title;
    private String wapIcon;

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getIsMall() {
        return this.isMall;
    }

    public void setIsMall(String isMall2) {
        this.isMall = isMall2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getWapIcon() {
        return this.wapIcon;
    }

    public void setWapIcon(String wapIcon2) {
        this.wapIcon = wapIcon2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getRateSum() {
        return this.rateSum;
    }

    public void setRateSum(String rateSum2) {
        this.rateSum = rateSum2;
    }

    public String getRankType() {
        return this.rankType;
    }

    public void setRankType(String rankType2) {
        this.rankType = rankType2;
    }

    public String getRankNum() {
        return this.rankNum;
    }

    public void setRankNum(String rankNum2) {
        this.rankNum = rankNum2;
    }

    public String getProv() {
        return this.prov;
    }

    public void setProv(String prov2) {
        this.prov = prov2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getPhoneExt() {
        return this.phoneExt;
    }

    public void setPhoneExt(String phoneExt2) {
        this.phoneExt = phoneExt2;
    }

    public String getCollectorCount() {
        return this.collectorCount;
    }

    public void setCollectorCount(String collectorCount2) {
        this.collectorCount = collectorCount2;
    }

    public String getStarts() {
        return this.starts;
    }

    public void setStarts(String starts2) {
        this.starts = starts2;
    }

    public String getProductCount() {
        return this.productCount;
    }

    public void setProductCount(String productCount2) {
        this.productCount = productCount2;
    }

    public shopDSRScore getShopDSRScore() {
        return this.shopDSRScore;
    }

    public void setShopDSRScore(shopDSRScore shopDSRScore2) {
        this.shopDSRScore = shopDSRScore2;
    }

    public String toString() {
        return "SellerInfo [id=" + this.id + ", title=" + this.title + ", isMall=" + this.isMall + ", nick=" + this.nick + ", sellerId=" + this.sellerId + ", wapIcon=" + this.wapIcon + ", picUrl=" + this.picUrl + ", rateSum=" + this.rateSum + ", rankType=" + this.rankType + ", rankNum=" + this.rankNum + ", prov=" + this.prov + ", city=" + this.city + ", phone=" + this.phone + ", phoneExt=" + this.phoneExt + ", collectorCount=" + this.collectorCount + ", starts=" + this.starts + ", productCount=" + this.productCount + ", shopDSRScore=" + this.shopDSRScore + "]";
    }
}
