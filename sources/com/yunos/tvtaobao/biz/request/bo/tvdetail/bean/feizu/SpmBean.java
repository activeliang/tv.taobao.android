package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class SpmBean {
    private DataBeanXXXXXXXXXX data;
    private String tag;

    public DataBeanXXXXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXXXXXXX {
        private String itemId;
        private String pageSpm;
        private String pageType;
        private String sellerId;
        private String shopId;
        private String shopType;

        public String getItemId() {
            return this.itemId;
        }

        public void setItemId(String itemId2) {
            this.itemId = itemId2;
        }

        public String getPageSpm() {
            return this.pageSpm;
        }

        public void setPageSpm(String pageSpm2) {
            this.pageSpm = pageSpm2;
        }

        public String getPageType() {
            return this.pageType;
        }

        public void setPageType(String pageType2) {
            this.pageType = pageType2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getShopId() {
            return this.shopId;
        }

        public void setShopId(String shopId2) {
            this.shopId = shopId2;
        }

        public String getShopType() {
            return this.shopType;
        }

        public void setShopType(String shopType2) {
            this.shopType = shopType2;
        }
    }
}
