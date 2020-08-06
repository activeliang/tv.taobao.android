package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class AlitripPromTagBean {
    private DataBeanXXXXXXXXXXXXX data;
    private String tag;

    public DataBeanXXXXXXXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXXXXXXXXXX {
        private String backgroundPicUrl;
        private String marketUrl;
        private String promDesc;
        private String promDescColor;
        private String promPicUrl;

        public String getBackgroundPicUrl() {
            return this.backgroundPicUrl;
        }

        public void setBackgroundPicUrl(String backgroundPicUrl2) {
            this.backgroundPicUrl = backgroundPicUrl2;
        }

        public String getMarketUrl() {
            return this.marketUrl;
        }

        public void setMarketUrl(String marketUrl2) {
            this.marketUrl = marketUrl2;
        }

        public String getPromDesc() {
            return this.promDesc;
        }

        public void setPromDesc(String promDesc2) {
            this.promDesc = promDesc2;
        }

        public String getPromDescColor() {
            return this.promDescColor;
        }

        public void setPromDescColor(String promDescColor2) {
            this.promDescColor = promDescColor2;
        }

        public String getPromPicUrl() {
            return this.promPicUrl;
        }

        public void setPromPicUrl(String promPicUrl2) {
            this.promPicUrl = promPicUrl2;
        }
    }
}
