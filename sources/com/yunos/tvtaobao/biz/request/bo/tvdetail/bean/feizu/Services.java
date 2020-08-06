package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class Services {
    private ServicesData data;
    private String tag;

    public void setData(ServicesData data2) {
        this.data = data2;
    }

    public ServicesData getData() {
        return this.data;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public String getTag() {
        return this.tag;
    }

    public static class ServicesData {
        private List<ServicesCells> cells;
        private String titleColor;

        public void setCells(List<ServicesCells> cells2) {
            this.cells = cells2;
        }

        public List<ServicesCells> getCells() {
            return this.cells;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }

        public static class ServicesCells {
            private String icon;
            private String title;

            public void setIcon(String icon2) {
                this.icon = icon2;
            }

            public String getIcon() {
                return this.icon;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getTitle() {
                return this.title;
            }
        }
    }
}
