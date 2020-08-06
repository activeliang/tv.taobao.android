package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class BrandBean {
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
        private String titleColor;
        private String titleIcon;
        private String titleText;

        public String getTitleColor() {
            return this.titleColor;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleIcon() {
            return this.titleIcon;
        }

        public void setTitleIcon(String titleIcon2) {
            this.titleIcon = titleIcon2;
        }

        public String getTitleText() {
            return this.titleText;
        }

        public void setTitleText(String titleText2) {
            this.titleText = titleText2;
        }
    }
}
