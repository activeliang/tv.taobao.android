package com.taobao.wireless.trade.mcart.sdk.co.business;

import java.util.List;

public class ItemRecommendResult {
    private List<RecommendItemDetail> cartRecommendItemDetails;
    private String scm;

    public String getScm() {
        return this.scm;
    }

    public void setScm(String scm2) {
        this.scm = scm2;
    }

    public List<RecommendItemDetail> getCartRecommendItemDetails() {
        return this.cartRecommendItemDetails;
    }

    public void setCartRecommendItemDetails(List<RecommendItemDetail> cartRecommendItemDetails2) {
        this.cartRecommendItemDetails = cartRecommendItemDetails2;
    }
}
