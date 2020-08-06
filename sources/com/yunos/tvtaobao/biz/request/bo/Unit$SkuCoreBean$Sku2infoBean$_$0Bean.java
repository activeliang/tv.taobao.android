package com.yunos.tvtaobao.biz.request.bo;

public class Unit$SkuCoreBean$Sku2infoBean$_$0Bean {
    private String limit;
    private PriceBeanXX price;
    private String quantity;

    public PriceBeanXX getPrice() {
        return this.price;
    }

    public void setPrice(PriceBeanXX price2) {
        this.price = price2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getLimit() {
        return this.limit;
    }

    public void setLimit(String limit2) {
        this.limit = limit2;
    }

    public static class PriceBeanXX {
        private String priceMoney;
        private String priceText;
        private String showTitle;

        public String getPriceMoney() {
            return this.priceMoney;
        }

        public void setPriceMoney(String priceMoney2) {
            this.priceMoney = priceMoney2;
        }

        public String getPriceText() {
            return this.priceText;
        }

        public void setPriceText(String priceText2) {
            this.priceText = priceText2;
        }

        public String getShowTitle() {
            return this.showTitle;
        }

        public void setShowTitle(String showTitle2) {
            this.showTitle = showTitle2;
        }
    }
}
