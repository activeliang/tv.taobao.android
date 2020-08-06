package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class MockData implements Serializable {
    private DeliveryBean delivery;
    private FeatureBean feature;
    private PriceBeanX price;
    private SkuCoreBean skuCore;
    private TradeBean trade;

    public static class DeliveryBean {
    }

    public DeliveryBean getDelivery() {
        return this.delivery;
    }

    public void setDelivery(DeliveryBean delivery2) {
        this.delivery = delivery2;
    }

    public FeatureBean getFeature() {
        return this.feature;
    }

    public void setFeature(FeatureBean feature2) {
        this.feature = feature2;
    }

    public PriceBeanX getPrice() {
        return this.price;
    }

    public void setPrice(PriceBeanX price2) {
        this.price = price2;
    }

    public SkuCoreBean getSkuCore() {
        return this.skuCore;
    }

    public void setSkuCore(SkuCoreBean skuCore2) {
        this.skuCore = skuCore2;
    }

    public TradeBean getTrade() {
        return this.trade;
    }

    public void setTrade(TradeBean trade2) {
        this.trade = trade2;
    }

    public static class FeatureBean {
        private String hasCoupon;
        private boolean hasSku;
        private boolean showSku;

        public String getHasCoupon() {
            return this.hasCoupon;
        }

        public void setHasCoupon(String hasCoupon2) {
            this.hasCoupon = hasCoupon2;
        }

        public boolean isHasSku() {
            return this.hasSku;
        }

        public void setHasSku(boolean hasSku2) {
            this.hasSku = hasSku2;
        }

        public boolean isShowSku() {
            return this.showSku;
        }

        public void setShowSku(boolean showSku2) {
            this.showSku = showSku2;
        }
    }

    public static class PriceBeanX {
        private PriceBean price;

        public PriceBean getPrice() {
            return this.price;
        }

        public void setPrice(PriceBean price2) {
            this.price = price2;
        }

        public static class PriceBean {
            private String priceText;

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }
        }
    }

    public static class SkuCoreBean {
        private Sku2infoBean sku2info;
        private SkuItemBean skuItem;

        public Sku2infoBean getSku2info() {
            return this.sku2info;
        }

        public void setSku2info(Sku2infoBean sku2info2) {
            this.sku2info = sku2info2;
        }

        public SkuItemBean getSkuItem() {
            return this.skuItem;
        }

        public void setSkuItem(SkuItemBean skuItem2) {
            this.skuItem = skuItem2;
        }

        public static class Sku2infoBean {
            private MockData$SkuCoreBean$Sku2infoBean$_$0Bean _$0;

            public MockData$SkuCoreBean$Sku2infoBean$_$0Bean get_$0() {
                return this._$0;
            }

            public void set_$0(MockData$SkuCoreBean$Sku2infoBean$_$0Bean _$02) {
                this._$0 = _$02;
            }
        }

        public static class SkuItemBean {
            private boolean hideQuantity;

            public boolean isHideQuantity() {
                return this.hideQuantity;
            }

            public void setHideQuantity(boolean hideQuantity2) {
                this.hideQuantity = hideQuantity2;
            }
        }
    }

    public static class TradeBean {
        private boolean buyEnable;
        private boolean cartEnable;

        public boolean isBuyEnable() {
            return this.buyEnable;
        }

        public void setBuyEnable(boolean buyEnable2) {
            this.buyEnable = buyEnable2;
        }

        public boolean isCartEnable() {
            return this.cartEnable;
        }

        public void setCartEnable(boolean cartEnable2) {
            this.cartEnable = cartEnable2;
        }
    }
}
