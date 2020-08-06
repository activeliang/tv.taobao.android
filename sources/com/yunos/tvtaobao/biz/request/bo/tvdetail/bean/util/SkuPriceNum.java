package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util;

public class SkuPriceNum {
    private int limit;
    private PriceBean price;
    private int quantity;
    private PriceBean subPrice;

    public PriceBean getSubPrice() {
        return this.subPrice;
    }

    public void setSubPrice(PriceBean subPrice2) {
        this.subPrice = subPrice2;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit2) {
        this.limit = limit2;
    }

    public PriceBean getPrice() {
        return this.price;
    }

    public void setPrice(PriceBean price2) {
        this.price = price2;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public String toString() {
        return "SkuPriceNum{price=" + this.price + ", quantity=" + this.quantity + ", limit=" + this.limit + '}';
    }

    public static class PriceBean {
        private long priceMoney;
        private String priceText;
        private String priceTitle;

        public long getPriceMoney() {
            return this.priceMoney;
        }

        public void setPriceMoney(int priceMoney2) {
            this.priceMoney = (long) priceMoney2;
        }

        public String getPriceText() {
            return this.priceText;
        }

        public void setPriceText(String priceText2) {
            this.priceText = priceText2;
        }

        public String getPriceTitle() {
            return this.priceTitle;
        }

        public void setPriceTitle(String priceTitle2) {
            this.priceTitle = priceTitle2;
        }
    }
}
