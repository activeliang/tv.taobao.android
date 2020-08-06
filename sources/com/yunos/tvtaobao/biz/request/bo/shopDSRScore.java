package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class shopDSRScore implements Serializable {
    private static final String TAG = "shopDSRScore";
    private static final long serialVersionUID = -260168997921881849L;
    private double cg;
    private String consignmentScore;
    private String merchandisScore;
    private double mg;
    private String sellerGoodPercent;
    private String serviceScore;
    private double sg;

    public String getMerchandisScore() {
        return this.merchandisScore;
    }

    public void setMerchandisScore(String merchandisScore2) {
        this.merchandisScore = merchandisScore2;
    }

    public double getMg() {
        return this.mg;
    }

    public void setMg(double mg2) {
        this.mg = mg2;
    }

    public String getServiceScore() {
        return this.serviceScore;
    }

    public void setServiceScore(String serviceScore2) {
        this.serviceScore = serviceScore2;
    }

    public double getSg() {
        return this.sg;
    }

    public void setSg(double sg2) {
        this.sg = sg2;
    }

    public String getConsignmentScore() {
        return this.consignmentScore;
    }

    public void setConsignmentScore(String consignmentScore2) {
        this.consignmentScore = consignmentScore2;
    }

    public double getCg() {
        return this.cg;
    }

    public void setCg(double cg2) {
        this.cg = cg2;
    }

    public String getSellerGoodPercent() {
        return this.sellerGoodPercent;
    }

    public void setSellerGoodPercent(String sellerGoodPercent2) {
        this.sellerGoodPercent = sellerGoodPercent2;
    }

    public String toString() {
        return "shopDSRScore [merchandisScore=" + this.merchandisScore + ", mg=" + this.mg + ", serviceScore=" + this.serviceScore + ", sg=" + this.sg + ", consignmentScore=" + this.consignmentScore + ", cg=" + this.cg + ", sellerGoodPercent=" + this.sellerGoodPercent + "]";
    }
}
