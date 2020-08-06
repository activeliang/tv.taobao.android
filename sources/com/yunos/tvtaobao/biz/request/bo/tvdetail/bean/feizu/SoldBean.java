package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class SoldBean {
    private DataBean data;
    private String tag;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBean {
        private String sold;
        private String soldCount;

        public String getSold() {
            return this.sold;
        }

        public void setSold(String sold2) {
            this.sold = sold2;
        }

        public String getSoldCount() {
            return this.soldCount;
        }

        public void setSoldCount(String soldCount2) {
            this.soldCount = soldCount2;
        }
    }
}
