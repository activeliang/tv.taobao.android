package com.yunos.tvtaobao.biz.request.bo;

public class CouponRecommend {
    private String currentPrice;
    private String itemId;
    private String itemTitle;
    private String originalCost;
    private String picUrl;
    private String shopName;
    private String url;

    public String getItemId() {
        return this.itemId;
    }

    public String getItemTitle() {
        return this.itemTitle;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public String getShopName() {
        return this.shopName;
    }

    public String getOriginalCost() {
        return this.originalCost;
    }

    public String getCurrentPrice() {
        return this.currentPrice;
    }
}
