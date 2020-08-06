package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class SkuBean implements Serializable {
    private List<SkuBean> attrList;
    private String name;
    private String price;
    private String promotionPrice;
    private String quantity;
    private String skuId;
    private String title;
    private List<SkuBean> value;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public List<SkuBean> getValue() {
        return this.value;
    }

    public void setValue(List<SkuBean> value2) {
        this.value = value2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getPromotionPrice() {
        return this.promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice2) {
        this.promotionPrice = promotionPrice2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public static class ValueBean implements Serializable {
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value2) {
            this.value = value2;
        }
    }
}
