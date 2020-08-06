package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class CouponBean {
    private DataBeanXXXX data;
    private String tag;

    public DataBeanXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXX {
        private List<CellsBean> cells;
        private String robIcon;
        private String titleColor;

        public String getRobIcon() {
            return this.robIcon;
        }

        public void setRobIcon(String robIcon2) {
            this.robIcon = robIcon2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public List<CellsBean> getCells() {
            return this.cells;
        }

        public void setCells(List<CellsBean> cells2) {
            this.cells = cells2;
        }

        public static class CellsBean {
            private EventBean event;
            private String icon;
            private String sort;
            private String title;
            private String type;

            public static class EventBean {
            }

            public EventBean getEvent() {
                return this.event;
            }

            public void setEvent(EventBean event2) {
                this.event = event2;
            }

            public String getIcon() {
                return this.icon;
            }

            public void setIcon(String icon2) {
                this.icon = icon2;
            }

            public String getSort() {
                return this.sort;
            }

            public void setSort(String sort2) {
                this.sort = sort2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }
        }
    }
}
