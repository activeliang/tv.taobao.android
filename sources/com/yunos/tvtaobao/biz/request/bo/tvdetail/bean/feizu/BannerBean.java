package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class BannerBean {
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
        private List<String> pics;
        private String rightDesc;

        public List<String> getPics() {
            return this.pics;
        }

        public void setPics(List<String> pics2) {
            this.pics = pics2;
        }

        public String getRightDesc() {
            return this.rightDesc;
        }

        public void setRightDesc(String rightDesc2) {
            this.rightDesc = rightDesc2;
        }
    }
}
