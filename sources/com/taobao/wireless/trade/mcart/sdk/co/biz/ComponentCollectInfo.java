package com.taobao.wireless.trade.mcart.sdk.co.biz;

public class ComponentCollectInfo {
    private String aucs;
    private long sellerId;
    private Long sumPrice;
    private Long sumWeight;

    public String getAucs() {
        return this.aucs;
    }

    public void setAucs(String aucs2) {
        this.aucs = aucs2;
    }

    public long getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(long sellerId2) {
        this.sellerId = sellerId2;
    }

    public Long getSumPrice() {
        return this.sumPrice;
    }

    public void setSumPrice(Long sumPrice2) {
        this.sumPrice = sumPrice2;
    }

    public Long getSumWeight() {
        return this.sumWeight;
    }

    public void setSumWeight(Long sumWeight2) {
        this.sumWeight = sumWeight2;
    }
}
