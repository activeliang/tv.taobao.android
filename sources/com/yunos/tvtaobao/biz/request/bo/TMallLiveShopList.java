package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class TMallLiveShopList {
    private ModelBean model;

    public ModelBean getModel() {
        return this.model;
    }

    public void setModel(ModelBean model2) {
        this.model = model2;
    }

    public static class ModelBean {
        private List<DataBean> data;
        private String endId;
        private String endTime;
        private String startId;
        private String startTime;

        public String getEndId() {
            return this.endId;
        }

        public void setEndId(String endId2) {
            this.endId = endId2;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime2) {
            this.endTime = endTime2;
        }

        public String getStartId() {
            return this.startId;
        }

        public void setStartId(String startId2) {
            this.startId = startId2;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setStartTime(String startTime2) {
            this.startTime = startTime2;
        }

        public List<DataBean> getData() {
            return this.data;
        }

        public void setData(List<DataBean> data2) {
            this.data = data2;
        }

        public static class DataBean {
            private boolean favorate = false;
            private String gmtCreate;
            private String gmtModified;
            private String id;
            private String itemAddress;
            private String itemId;
            private String onShelf;
            private String picUrl;
            private String price;
            private String title;

            public void setFavorate(boolean favorate2) {
                this.favorate = favorate2;
            }

            public boolean getFavorate() {
                return this.favorate;
            }

            public String getGmtCreate() {
                return this.gmtCreate;
            }

            public void setGmtCreate(String gmtCreate2) {
                this.gmtCreate = gmtCreate2;
            }

            public String getGmtModified() {
                return this.gmtModified;
            }

            public void setGmtModified(String gmtModified2) {
                this.gmtModified = gmtModified2;
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id2) {
                this.id = id2;
            }

            public String getItemAddress() {
                return this.itemAddress;
            }

            public void setItemAddress(String itemAddress2) {
                this.itemAddress = itemAddress2;
            }

            public String getItemId() {
                return this.itemId;
            }

            public void setItemId(String itemId2) {
                this.itemId = itemId2;
            }

            public String getOnShelf() {
                return this.onShelf;
            }

            public void setOnShelf(String onShelf2) {
                this.onShelf = onShelf2;
            }

            public String getPicUrl() {
                return this.picUrl;
            }

            public void setPicUrl(String picUrl2) {
                this.picUrl = picUrl2;
            }

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price2) {
                this.price = price2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }
        }
    }
}
