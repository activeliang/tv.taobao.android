package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class SkuBean {
    private DataBean data;
    private String tag;

    private static class DataBean {
        private String enableSelectCount;
        private String hideActionPrice;
        private String mainPic;
        private List<PropsBean> props;
        private String selectedDateTitle;
        private ShowInventoryRuleBean showInventoryRule;
        private SkuListExtendBean skuListExtend;
        private SkuMapBean skuMap;
        private String skuShareInventory;
        private SkuStrokeStatusBean skuStrokeStatus;
        private String startDateTitle;
        private String status;

        public static class SkuListExtendBean {

            public static class HotelBean {
            }
        }

        public static class SkuMapBean {
        }

        private DataBean() {
        }

        public String getEnableSelectCount() {
            return this.enableSelectCount;
        }

        public void setEnableSelectCount(String enableSelectCount2) {
            this.enableSelectCount = enableSelectCount2;
        }

        public String getHideActionPrice() {
            return this.hideActionPrice;
        }

        public void setHideActionPrice(String hideActionPrice2) {
            this.hideActionPrice = hideActionPrice2;
        }

        public String getMainPic() {
            return this.mainPic;
        }

        public void setMainPic(String mainPic2) {
            this.mainPic = mainPic2;
        }

        public String getSelectedDateTitle() {
            return this.selectedDateTitle;
        }

        public void setSelectedDateTitle(String selectedDateTitle2) {
            this.selectedDateTitle = selectedDateTitle2;
        }

        public ShowInventoryRuleBean getShowInventoryRule() {
            return this.showInventoryRule;
        }

        public void setShowInventoryRule(ShowInventoryRuleBean showInventoryRule2) {
            this.showInventoryRule = showInventoryRule2;
        }

        public SkuListExtendBean getSkuListExtend() {
            return this.skuListExtend;
        }

        public void setSkuListExtend(SkuListExtendBean skuListExtend2) {
            this.skuListExtend = skuListExtend2;
        }

        public SkuMapBean getSkuMap() {
            return this.skuMap;
        }

        public void setSkuMap(SkuMapBean skuMap2) {
            this.skuMap = skuMap2;
        }

        public String getSkuShareInventory() {
            return this.skuShareInventory;
        }

        public void setSkuShareInventory(String skuShareInventory2) {
            this.skuShareInventory = skuShareInventory2;
        }

        public SkuStrokeStatusBean getSkuStrokeStatus() {
            return this.skuStrokeStatus;
        }

        public void setSkuStrokeStatus(SkuStrokeStatusBean skuStrokeStatus2) {
            this.skuStrokeStatus = skuStrokeStatus2;
        }

        public String getStartDateTitle() {
            return this.startDateTitle;
        }

        public void setStartDateTitle(String startDateTitle2) {
            this.startDateTitle = startDateTitle2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public List<PropsBean> getProps() {
            return this.props;
        }

        public void setProps(List<PropsBean> props2) {
            this.props = props2;
        }

        public static class ShowInventoryRuleBean {
            private String max;
            private String showInventoryNum;

            public String getMax() {
                return this.max;
            }

            public void setMax(String max2) {
                this.max = max2;
            }

            public String getShowInventoryNum() {
                return this.showInventoryNum;
            }

            public void setShowInventoryNum(String showInventoryNum2) {
                this.showInventoryNum = showInventoryNum2;
            }
        }

        public static class SkuStrokeStatusBean {
            private String backGroundColor;
            private String backGroundHeadUrl;
            private String backGroundUrl;
            private String desc;
            private String showStroke;
            private String status;
            private String textColor;

            public String getBackGroundColor() {
                return this.backGroundColor;
            }

            public void setBackGroundColor(String backGroundColor2) {
                this.backGroundColor = backGroundColor2;
            }

            public String getBackGroundHeadUrl() {
                return this.backGroundHeadUrl;
            }

            public void setBackGroundHeadUrl(String backGroundHeadUrl2) {
                this.backGroundHeadUrl = backGroundHeadUrl2;
            }

            public String getBackGroundUrl() {
                return this.backGroundUrl;
            }

            public void setBackGroundUrl(String backGroundUrl2) {
                this.backGroundUrl = backGroundUrl2;
            }

            public String getDesc() {
                return this.desc;
            }

            public void setDesc(String desc2) {
                this.desc = desc2;
            }

            public String getShowStroke() {
                return this.showStroke;
            }

            public void setShowStroke(String showStroke2) {
                this.showStroke = showStroke2;
            }

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status2) {
                this.status = status2;
            }

            public String getTextColor() {
                return this.textColor;
            }

            public void setTextColor(String textColor2) {
                this.textColor = textColor2;
            }
        }

        public static class PropsBean {
            private String skuPropId;
            private List<SkuPropListBean> skuPropList;
            private String skuPropTitle;

            public String getSkuPropId() {
                return this.skuPropId;
            }

            public void setSkuPropId(String skuPropId2) {
                this.skuPropId = skuPropId2;
            }

            public String getSkuPropTitle() {
                return this.skuPropTitle;
            }

            public void setSkuPropTitle(String skuPropTitle2) {
                this.skuPropTitle = skuPropTitle2;
            }

            public List<SkuPropListBean> getSkuPropList() {
                return this.skuPropList;
            }

            public void setSkuPropList(List<SkuPropListBean> skuPropList2) {
                this.skuPropList = skuPropList2;
            }

            public static class SkuPropListBean {
                private String pvId;
                private String value;

                public String getPvId() {
                    return this.pvId;
                }

                public void setPvId(String pvId2) {
                    this.pvId = pvId2;
                }

                public String getValue() {
                    return this.value;
                }

                public void setValue(String value2) {
                    this.value = value2;
                }
            }
        }
    }
}
