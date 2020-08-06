package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class SkuProp {
    private String count;
    private boolean hasSku;
    private String itemId;
    private List<Prop> propList;
    private String shopId;
    private String skuId;
    private String title;

    public static class Prop {
        private String Name;
        private String value;

        public String getName() {
            return this.Name;
        }

        public void setName(String name) {
            this.Name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value2) {
            this.value = value2;
        }

        public String toString() {
            return "Prop{Name='" + this.Name + '\'' + ", value='" + this.value + '\'' + '}';
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public List<Prop> getPropList() {
        return this.propList;
    }

    public void setPropList(List<Prop> propList2) {
        this.propList = propList2;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count2) {
        this.count = count2;
    }

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId2) {
        this.skuId = skuId2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public boolean isHasSku() {
        return this.hasSku;
    }

    public void setHasSku(boolean hasSku2) {
        this.hasSku = hasSku2;
    }
}
