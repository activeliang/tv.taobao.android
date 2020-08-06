package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

public class ResourceBean {
    private EntrancesBean entrances;

    public EntrancesBean getEntrances() {
        return this.entrances;
    }

    public void setEntrances(EntrancesBean entrances2) {
        this.entrances = entrances2;
    }

    public static class EntrancesBean {
        private AskAllBean askAll;

        public AskAllBean getAskAll() {
            return this.askAll;
        }

        public void setAskAll(AskAllBean askAll2) {
            this.askAll = askAll2;
        }

        public static class AskAllBean {
            private String icon;
            private String link;
            private String text;

            public String getIcon() {
                return this.icon;
            }

            public void setIcon(String icon2) {
                this.icon = icon2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getLink() {
                return this.link;
            }

            public void setLink(String link2) {
                this.link = link2;
            }
        }
    }
}
