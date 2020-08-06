package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class ItemListBean implements Serializable {
    public int __intentCount;
    private boolean __isRest;
    public int __limitQuantity = 0;
    public long __limitSkuId = 0;
    private String bestSelling;
    private String checkoutMode;
    private String description;
    private String flashTimeValid;
    private String hasSku;
    private List<ItemAttrListBean> itemAttrList;
    private String itemCateId;
    private String itemId;
    private String itemPicts;
    private MultiAttrBean multiAttr;
    private String originStock;
    private String price;
    private String promotionPrice;
    private String promotioned;
    private String saleCount;
    private String sellerId;
    private String serviceId;
    private String shopId;
    private List<SkuListBean> skuList;
    private String soldMode;
    private String status;
    private int stock;
    private String storeId;
    public List<Tag> tagList;
    private String title;

    public enum Status {
        normal,
        edit,
        outStock,
        rest
    }

    public static class Tag implements Serializable {
        public String subText;
        public String text;
        public String type;
    }

    public void setIsRest(boolean rest) {
        this.__isRest = rest;
    }

    public Status getGoodStatus() {
        if (this.stock <= 0 && !"true".equals(this.hasSku)) {
            return Status.outStock;
        }
        if (this.__isRest) {
            return Status.rest;
        }
        if (this.__intentCount > 0) {
            return Status.edit;
        }
        return Status.normal;
    }

    public String getOriginStock() {
        return this.originStock;
    }

    public void setOriginStock(String originStock2) {
        this.originStock = originStock2;
    }

    public String getBestSelling() {
        return this.bestSelling;
    }

    public void setBestSelling(String bestSelling2) {
        this.bestSelling = bestSelling2;
    }

    public String getCheckoutMode() {
        return this.checkoutMode;
    }

    public void setCheckoutMode(String checkoutMode2) {
        this.checkoutMode = checkoutMode2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getFlashTimeValid() {
        return this.flashTimeValid;
    }

    public void setFlashTimeValid(String flashTimeValid2) {
        this.flashTimeValid = flashTimeValid2;
    }

    public String getHasSku() {
        return this.hasSku;
    }

    public void setHasSku(String hasSku2) {
        this.hasSku = hasSku2;
    }

    public String getItemCateId() {
        return this.itemCateId;
    }

    public void setItemCateId(String itemCateId2) {
        this.itemCateId = itemCateId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getItemPicts() {
        return this.itemPicts;
    }

    public void setItemPicts(String itemPicts2) {
        this.itemPicts = itemPicts2;
    }

    public MultiAttrBean getMultiAttr() {
        return this.multiAttr;
    }

    public void setMultiAttr(MultiAttrBean multiAttr2) {
        this.multiAttr = multiAttr2;
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

    public String getPromotioned() {
        return this.promotioned;
    }

    public void setPromotioned(String promotioned2) {
        this.promotioned = promotioned2;
    }

    public String getSaleCount() {
        return this.saleCount;
    }

    public void setSaleCount(String saleCount2) {
        this.saleCount = saleCount2;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId2) {
        this.serviceId = serviceId2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getSoldMode() {
        return this.soldMode;
    }

    public void setSoldMode(String soldMode2) {
        this.soldMode = soldMode2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock2) {
        this.stock = stock2;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId2) {
        this.storeId = storeId2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public List<ItemAttrListBean> getItemAttrList() {
        return this.itemAttrList;
    }

    public void setItemAttrList(List<ItemAttrListBean> itemAttrList2) {
        this.itemAttrList = itemAttrList2;
    }

    public List<SkuListBean> getSkuList() {
        return this.skuList;
    }

    public void setSkuList(List<SkuListBean> skuList2) {
        this.skuList = skuList2;
    }

    public static class MultiAttrBean implements Serializable {
        private List<AttrListBean> attrList;

        public List<AttrListBean> getAttrList() {
            return this.attrList;
        }

        public void setAttrList(List<AttrListBean> attrList2) {
            this.attrList = attrList2;
        }

        public static class AttrListBean implements Serializable {
            private String name;
            private List<String> value;

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public List<String> getValue() {
                return this.value;
            }

            public void setValue(List<String> value2) {
                this.value = value2;
            }
        }
    }

    public static class SkuListBean implements Serializable {
        private String price;
        private String promotionPrice;
        private String quantity;
        private String skuId;
        private String title;

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

        public String toString() {
            return "SkuListBean{price='" + this.price + '\'' + ", promotionPrice='" + this.promotionPrice + '\'' + ", quantity='" + this.quantity + '\'' + ", skuId='" + this.skuId + '\'' + ", title='" + this.title + '\'' + '}';
        }
    }

    public static class ItemAttrListBean implements Serializable {
        private String attrType;
        private String desc;
        private String icon;
        private String sortId;

        public String getAttrType() {
            return this.attrType;
        }

        public void setAttrType(String attrType2) {
            this.attrType = attrType2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getSortId() {
            return this.sortId;
        }

        public void setSortId(String sortId2) {
            this.sortId = sortId2;
        }
    }
}
