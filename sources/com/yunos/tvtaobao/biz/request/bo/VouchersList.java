package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class VouchersList {
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return this.result;
    }

    public void setResult(List<ResultBean> result2) {
        this.result = result2;
    }

    public static class ResultBean {
        private String activityId;
        private String amount;
        private String conditionType;
        private String description;
        private String exchangeConsumeAmount;
        private ExchangeDescriptionBean exchangeDescription;
        private String exchangeLeftNum;
        private ExchangeTipsBean exchangeTips;
        private String exchangeType;
        private String hongbaoTotalNum;
        private String isNewExclusive;
        private String name;
        private String popupLink;
        private String popupParam;
        private PopupTipsBean popupTips;
        private String popupType;
        private String status;
        private SuccessTipsBean successTips;

        public String getActivityId() {
            return this.activityId;
        }

        public void setActivityId(String activityId2) {
            this.activityId = activityId2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getAmount() {
            return this.amount;
        }

        public void setAmount(String amount2) {
            this.amount = amount2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getConditionType() {
            return this.conditionType;
        }

        public void setConditionType(String conditionType2) {
            this.conditionType = conditionType2;
        }

        public String getExchangeType() {
            return this.exchangeType;
        }

        public void setExchangeType(String exchangeType2) {
            this.exchangeType = exchangeType2;
        }

        public ExchangeDescriptionBean getExchangeDescription() {
            return this.exchangeDescription;
        }

        public void setExchangeDescription(ExchangeDescriptionBean exchangeDescription2) {
            this.exchangeDescription = exchangeDescription2;
        }

        public ExchangeTipsBean getExchangeTips() {
            return this.exchangeTips;
        }

        public void setExchangeTips(ExchangeTipsBean exchangeTips2) {
            this.exchangeTips = exchangeTips2;
        }

        public String getExchangeLeftNum() {
            return this.exchangeLeftNum;
        }

        public void setExchangeLeftNum(String exchangeLeftNum2) {
            this.exchangeLeftNum = exchangeLeftNum2;
        }

        public String getExchangeConsumeAmount() {
            return this.exchangeConsumeAmount;
        }

        public void setExchangeConsumeAmount(String exchangeConsumeAmount2) {
            this.exchangeConsumeAmount = exchangeConsumeAmount2;
        }

        public String getPopupType() {
            return this.popupType;
        }

        public void setPopupType(String popupType2) {
            this.popupType = popupType2;
        }

        public SuccessTipsBean getSuccessTips() {
            return this.successTips;
        }

        public void setSuccessTips(SuccessTipsBean successTips2) {
            this.successTips = successTips2;
        }

        public String getHongbaoTotalNum() {
            return this.hongbaoTotalNum;
        }

        public void setHongbaoTotalNum(String hongbaoTotalNum2) {
            this.hongbaoTotalNum = hongbaoTotalNum2;
        }

        public PopupTipsBean getPopupTips() {
            return this.popupTips;
        }

        public void setPopupTips(PopupTipsBean popupTips2) {
            this.popupTips = popupTips2;
        }

        public String getPopupLink() {
            return this.popupLink;
        }

        public void setPopupLink(String popupLink2) {
            this.popupLink = popupLink2;
        }

        public String getIsNewExclusive() {
            return this.isNewExclusive;
        }

        public void setIsNewExclusive(String isNewExclusive2) {
            this.isNewExclusive = isNewExclusive2;
        }

        public String getPopupParam() {
            return this.popupParam;
        }

        public void setPopupParam(String popupParam2) {
            this.popupParam = popupParam2;
        }

        public static class ExchangeDescriptionBean {
            private String highlight;
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getHighlight() {
                return this.highlight;
            }

            public void setHighlight(String highlight2) {
                this.highlight = highlight2;
            }
        }

        public static class ExchangeTipsBean {
            private String highlight;
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getHighlight() {
                return this.highlight;
            }

            public void setHighlight(String highlight2) {
                this.highlight = highlight2;
            }
        }

        public static class SuccessTipsBean {
            private String highlight;
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getHighlight() {
                return this.highlight;
            }

            public void setHighlight(String highlight2) {
                this.highlight = highlight2;
            }
        }

        public static class PopupTipsBean {
            private String highlight;
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getHighlight() {
                return this.highlight;
            }

            public void setHighlight(String highlight2) {
                this.highlight = highlight2;
            }
        }
    }
}
