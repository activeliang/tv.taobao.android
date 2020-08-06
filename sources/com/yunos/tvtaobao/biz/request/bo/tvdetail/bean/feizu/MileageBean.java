package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class MileageBean {
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
        private String desc;
        private String flayerTitle;
        private String icon;
        private String title;
        private String titleColor;

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getFlayerTitle() {
            return this.flayerTitle;
        }

        public void setFlayerTitle(String flayerTitle2) {
            this.flayerTitle = flayerTitle2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }
    }
}
