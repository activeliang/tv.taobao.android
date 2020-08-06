package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class TakeoutApplyCoupon {
    private List<CouponsBean> coupons;
    private FeaturesBean features;
    private String newRetCode;
    private String newRetMsg;
    private String retCode;
    private ShowStyleBean showStyle;

    public FeaturesBean getFeatures() {
        return this.features;
    }

    public void setFeatures(FeaturesBean features2) {
        this.features = features2;
    }

    public String getNewRetCode() {
        return this.newRetCode;
    }

    public void setNewRetCode(String newRetCode2) {
        this.newRetCode = newRetCode2;
    }

    public String getNewRetMsg() {
        return this.newRetMsg;
    }

    public void setNewRetMsg(String newRetMsg2) {
        this.newRetMsg = newRetMsg2;
    }

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String retCode2) {
        this.retCode = retCode2;
    }

    public ShowStyleBean getShowStyle() {
        return this.showStyle;
    }

    public void setShowStyle(ShowStyleBean showStyle2) {
        this.showStyle = showStyle2;
    }

    public List<CouponsBean> getCoupons() {
        return this.coupons;
    }

    public void setCoupons(List<CouponsBean> coupons2) {
        this.coupons = coupons2;
    }

    public static class FeaturesBean {
        private String isElemeNewGuest;
        private String isTaobaoNewGuest;
        private String mobile;

        public String getIsTaobaoNewGuest() {
            return this.isTaobaoNewGuest;
        }

        public void setIsTaobaoNewGuest(String isTaobaoNewGuest2) {
            this.isTaobaoNewGuest = isTaobaoNewGuest2;
        }

        public String getIsElemeNewGuest() {
            return this.isElemeNewGuest;
        }

        public void setIsElemeNewGuest(String isElemeNewGuest2) {
            this.isElemeNewGuest = isElemeNewGuest2;
        }

        public String getMobile() {
            return this.mobile;
        }

        public void setMobile(String mobile2) {
            this.mobile = mobile2;
        }
    }

    public static class ShowStyleBean {
        private String styleType;

        public String getStyleType() {
            return this.styleType;
        }

        public void setStyleType(String styleType2) {
            this.styleType = styleType2;
        }
    }

    public static class CouponsBean {
        private String amount;
        private String availableFee;
        private String condition;
        private String conditionText;
        private String couponShowType;
        private String endTime;
        private String id;
        private String scope;
        private String source;
        private String startTime;
        private String title;
        private String type;
        private String waimaiNewGuest;

        public String getAmount() {
            return this.amount;
        }

        public void setAmount(String amount2) {
            this.amount = amount2;
        }

        public String getAvailableFee() {
            return this.availableFee;
        }

        public void setAvailableFee(String availableFee2) {
            this.availableFee = availableFee2;
        }

        public String getCondition() {
            return this.condition;
        }

        public void setCondition(String condition2) {
            this.condition = condition2;
        }

        public String getConditionText() {
            return this.conditionText;
        }

        public void setConditionText(String conditionText2) {
            this.conditionText = conditionText2;
        }

        public String getCouponShowType() {
            return this.couponShowType;
        }

        public void setCouponShowType(String couponShowType2) {
            this.couponShowType = couponShowType2;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime2) {
            this.endTime = endTime2;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getScope() {
            return this.scope;
        }

        public void setScope(String scope2) {
            this.scope = scope2;
        }

        public String getSource() {
            return this.source;
        }

        public void setSource(String source2) {
            this.source = source2;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public void setStartTime(String startTime2) {
            this.startTime = startTime2;
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

        public String getWaimaiNewGuest() {
            return this.waimaiNewGuest;
        }

        public void setWaimaiNewGuest(String waimaiNewGuest2) {
            this.waimaiNewGuest = waimaiNewGuest2;
        }
    }
}
