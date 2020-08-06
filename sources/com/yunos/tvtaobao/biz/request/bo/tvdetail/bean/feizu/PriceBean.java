package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class PriceBean {
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
        private AidedPriceBean aidedPrice;
        private String backgroundUrl;
        private List<?> extra;
        private MainPriceBean mainPrice;
        private RightRegionBean rightRegion;

        public AidedPriceBean getAidedPrice() {
            return this.aidedPrice;
        }

        public void setAidedPrice(AidedPriceBean aidedPrice2) {
            this.aidedPrice = aidedPrice2;
        }

        public String getBackgroundUrl() {
            return this.backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl2) {
            this.backgroundUrl = backgroundUrl2;
        }

        public MainPriceBean getMainPrice() {
            return this.mainPrice;
        }

        public void setMainPrice(MainPriceBean mainPrice2) {
            this.mainPrice = mainPrice2;
        }

        public RightRegionBean getRightRegion() {
            return this.rightRegion;
        }

        public void setRightRegion(RightRegionBean rightRegion2) {
            this.rightRegion = rightRegion2;
        }

        public List<?> getExtra() {
            return this.extra;
        }

        public void setExtra(List<?> extra2) {
            this.extra = extra2;
        }

        public static class AidedPriceBean {
            private List<?> extra;
            private String lineThrough;
            private String prePriceText;
            private String price;
            private String priceText;

            public String getLineThrough() {
                return this.lineThrough;
            }

            public void setLineThrough(String lineThrough2) {
                this.lineThrough = lineThrough2;
            }

            public String getPrePriceText() {
                return this.prePriceText;
            }

            public void setPrePriceText(String prePriceText2) {
                this.prePriceText = prePriceText2;
            }

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price2) {
                this.price = price2;
            }

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }

            public List<?> getExtra() {
                return this.extra;
            }

            public void setExtra(List<?> extra2) {
                this.extra = extra2;
            }
        }

        public static class MainPriceBean {
            private List<?> extra;
            private String price;
            private String priceText;

            public String getPrice() {
                return this.price;
            }

            public void setPrice(String price2) {
                this.price = price2;
            }

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }

            public List<?> getExtra() {
                return this.extra;
            }

            public void setExtra(List<?> extra2) {
                this.extra = extra2;
            }
        }

        public static class RightRegionBean {
            private String picUrl;

            public String getPicUrl() {
                return this.picUrl;
            }

            public void setPicUrl(String picUrl2) {
                this.picUrl = picUrl2;
            }
        }
    }
}
