package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.util;

import com.google.gson.annotations.SerializedName;
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
        private boolean hasSku;
        private boolean showSku;

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
            @SerializedName("0")
            private MockData$SkuCoreBean$Sku2infoBean$_$0Bean _$0;
            @SerializedName("3290521132334")
            private MockData$SkuCoreBean$Sku2infoBean$_$3290521132334Bean _$3290521132334;
            @SerializedName("3290521132335")
            private MockData$SkuCoreBean$Sku2infoBean$_$3290521132335Bean _$3290521132335;
            @SerializedName("3290521132336")
            private MockData$SkuCoreBean$Sku2infoBean$_$3290521132336Bean _$3290521132336;

            public MockData$SkuCoreBean$Sku2infoBean$_$0Bean get_$0() {
                return this._$0;
            }

            public void set_$0(MockData$SkuCoreBean$Sku2infoBean$_$0Bean _$02) {
                this._$0 = _$02;
            }

            public MockData$SkuCoreBean$Sku2infoBean$_$3290521132334Bean get_$3290521132334() {
                return this._$3290521132334;
            }

            public void set_$3290521132334(MockData$SkuCoreBean$Sku2infoBean$_$3290521132334Bean _$32905211323342) {
                this._$3290521132334 = _$32905211323342;
            }

            public MockData$SkuCoreBean$Sku2infoBean$_$3290521132335Bean get_$3290521132335() {
                return this._$3290521132335;
            }

            public void set_$3290521132335(MockData$SkuCoreBean$Sku2infoBean$_$3290521132335Bean _$32905211323352) {
                this._$3290521132335 = _$32905211323352;
            }

            public MockData$SkuCoreBean$Sku2infoBean$_$3290521132336Bean get_$3290521132336() {
                return this._$3290521132336;
            }

            public void set_$3290521132336(MockData$SkuCoreBean$Sku2infoBean$_$3290521132336Bean _$32905211323362) {
                this._$3290521132336 = _$32905211323362;
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
