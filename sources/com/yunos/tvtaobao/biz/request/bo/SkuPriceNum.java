package com.yunos.tvtaobao.biz.request.bo;

import java.math.BigDecimal;

public class SkuPriceNum {
    private int limit;
    private PriceBean price;
    private int quantity;
    private SubPrice subPrice;

    public String toString() {
        return "SkuPriceNum{price=" + this.price + ", quantity=" + this.quantity + ", limit=" + this.limit + '}';
    }

    public SubPrice getSubPrice() {
        return this.subPrice;
    }

    public void setSubPrice(SubPrice subPrice2) {
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

    public static class SubPrice {
        private int priceMoney;
        private String priceText;
        private String priceTitle;
        private boolean showTitle;
        private boolean sugProm;

        public int getPriceMoney() {
            return this.priceMoney;
        }

        public void setPriceMoney(int priceMoney2) {
            this.priceMoney = priceMoney2;
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

        public boolean isShowTitle() {
            return this.showTitle;
        }

        public void setShowTitle(boolean showTitle2) {
            this.showTitle = showTitle2;
        }

        public boolean isSugProm() {
            return this.sugProm;
        }

        public void setSugProm(boolean sugProm2) {
            this.sugProm = sugProm2;
        }
    }

    public static class PriceBean {
        private BigDecimal priceMoney;
        private String priceText;
        private String priceTitle;

        public BigDecimal getPriceMoney() {
            return this.priceMoney;
        }

        public void setPriceMoney(BigDecimal priceMoney2) {
            this.priceMoney = priceMoney2;
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
