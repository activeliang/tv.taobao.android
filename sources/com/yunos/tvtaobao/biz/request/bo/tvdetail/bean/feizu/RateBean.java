package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class RateBean {
    private DataBeanXXXXXXXXXXX data;
    private String tag;

    public DataBeanXXXXXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXXXXXXXX {
        private JumpInfoBeanXX jumpInfo;
        private RateCellBean rateCell;
        private String total;

        public JumpInfoBeanXX getJumpInfo() {
            return this.jumpInfo;
        }

        public void setJumpInfo(JumpInfoBeanXX jumpInfo2) {
            this.jumpInfo = jumpInfo2;
        }

        public RateCellBean getRateCell() {
            return this.rateCell;
        }

        public void setRateCell(RateCellBean rateCell2) {
            this.rateCell = rateCell2;
        }

        public String getTotal() {
            return this.total;
        }

        public void setTotal(String total2) {
            this.total = total2;
        }

        public static class JumpInfoBeanXX {
            private String buyUrlType;
            private String jumpH5Url;
            private String jumpNative;
            private String showBuyButton;

            public String getBuyUrlType() {
                return this.buyUrlType;
            }

            public void setBuyUrlType(String buyUrlType2) {
                this.buyUrlType = buyUrlType2;
            }

            public String getJumpH5Url() {
                return this.jumpH5Url;
            }

            public void setJumpH5Url(String jumpH5Url2) {
                this.jumpH5Url = jumpH5Url2;
            }

            public String getJumpNative() {
                return this.jumpNative;
            }

            public void setJumpNative(String jumpNative2) {
                this.jumpNative = jumpNative2;
            }

            public String getShowBuyButton() {
                return this.showBuyButton;
            }

            public void setShowBuyButton(String showBuyButton2) {
                this.showBuyButton = showBuyButton2;
            }
        }

        public static class RateCellBean {
            private List<String> picList;
            private String rateContent;
            private String rateDate;
            private String rateId;
            private String userAvatar;
            private String userNick;

            public String getRateContent() {
                return this.rateContent;
            }

            public void setRateContent(String rateContent2) {
                this.rateContent = rateContent2;
            }

            public String getRateDate() {
                return this.rateDate;
            }

            public void setRateDate(String rateDate2) {
                this.rateDate = rateDate2;
            }

            public String getRateId() {
                return this.rateId;
            }

            public void setRateId(String rateId2) {
                this.rateId = rateId2;
            }

            public String getUserAvatar() {
                return this.userAvatar;
            }

            public void setUserAvatar(String userAvatar2) {
                this.userAvatar = userAvatar2;
            }

            public String getUserNick() {
                return this.userNick;
            }

            public void setUserNick(String userNick2) {
                this.userNick = userNick2;
            }

            public List<String> getPicList() {
                return this.picList;
            }

            public void setPicList(List<String> picList2) {
                this.picList = picList2;
            }
        }
    }
}
