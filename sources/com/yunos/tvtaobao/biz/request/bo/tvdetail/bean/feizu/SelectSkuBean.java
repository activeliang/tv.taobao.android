package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class SelectSkuBean {
    private DataBeanXXXXXXXXXXXXXXX data;
    private String tag;

    public DataBeanXXXXXXXXXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXXXXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXXXXXXXXXXXX {
        private List<String> props;
        private SelectSkuExtendBean selectSkuExtend;

        public SelectSkuExtendBean getSelectSkuExtend() {
            return this.selectSkuExtend;
        }

        public void setSelectSkuExtend(SelectSkuExtendBean selectSkuExtend2) {
            this.selectSkuExtend = selectSkuExtend2;
        }

        public List<String> getProps() {
            return this.props;
        }

        public void setProps(List<String> props2) {
            this.props = props2;
        }

        public static class SelectSkuExtendBean {
            private HotelBean hotel;

            public HotelBean getHotel() {
                return this.hotel;
            }

            public void setHotel(HotelBean hotel2) {
                this.hotel = hotel2;
            }

            public static class HotelBean {
                private String allStoreCount;
                private String extraDesc;

                public String getAllStoreCount() {
                    return this.allStoreCount;
                }

                public void setAllStoreCount(String allStoreCount2) {
                    this.allStoreCount = allStoreCount2;
                }

                public String getExtraDesc() {
                    return this.extraDesc;
                }

                public void setExtraDesc(String extraDesc2) {
                    this.extraDesc = extraDesc2;
                }
            }
        }
    }
}
