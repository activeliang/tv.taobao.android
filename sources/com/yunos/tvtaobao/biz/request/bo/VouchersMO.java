package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class VouchersMO {
    private String desc;
    private List<DetailsBean> details;
    private LatestStatusBean latestStatus;
    private String status;
    private String thresholdDesc;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public LatestStatusBean getLatestStatus() {
        return this.latestStatus;
    }

    public void setLatestStatus(LatestStatusBean latestStatus2) {
        this.latestStatus = latestStatus2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getThresholdDesc() {
        return this.thresholdDesc;
    }

    public void setThresholdDesc(String thresholdDesc2) {
        this.thresholdDesc = thresholdDesc2;
    }

    public List<DetailsBean> getDetails() {
        return this.details;
    }

    public void setDetails(List<DetailsBean> details2) {
        this.details = details2;
    }

    public static class LatestStatusBean {
        private String conditionDesc;
        private String deductDesc;
        private String description;
        private String icon;
        private String status;
        private String statusContent;
        private String storeId;
        private String storeName;
        private String targetUrl;

        public String getConditionDesc() {
            return this.conditionDesc;
        }

        public void setConditionDesc(String conditionDesc2) {
            this.conditionDesc = conditionDesc2;
        }

        public String getDeductDesc() {
            return this.deductDesc;
        }

        public void setDeductDesc(String deductDesc2) {
            this.deductDesc = deductDesc2;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description2) {
            this.description = description2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStatusContent() {
            return this.statusContent;
        }

        public void setStatusContent(String statusContent2) {
            this.statusContent = statusContent2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getStoreName() {
            return this.storeName;
        }

        public void setStoreName(String storeName2) {
            this.storeName = storeName2;
        }

        public String getTargetUrl() {
            return this.targetUrl;
        }

        public void setTargetUrl(String targetUrl2) {
            this.targetUrl = targetUrl2;
        }
    }

    public static class DetailsBean {
        private String beginTime;
        private String deductAmount;
        private String deductThreshold;
        private String endTime;
        private String serviceId;
        private String status;
        private String storeId;
        private String storeName;
        private String voucherId;
        private String voucherType;

        public String getBeginTime() {
            return this.beginTime;
        }

        public void setBeginTime(String beginTime2) {
            this.beginTime = beginTime2;
        }

        public String getDeductAmount() {
            return this.deductAmount;
        }

        public void setDeductAmount(String deductAmount2) {
            this.deductAmount = deductAmount2;
        }

        public String getDeductThreshold() {
            return this.deductThreshold;
        }

        public void setDeductThreshold(String deductThreshold2) {
            this.deductThreshold = deductThreshold2;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String endTime2) {
            this.endTime = endTime2;
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public void setServiceId(String serviceId2) {
            this.serviceId = serviceId2;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStoreId() {
            return this.storeId;
        }

        public void setStoreId(String storeId2) {
            this.storeId = storeId2;
        }

        public String getStoreName() {
            return this.storeName;
        }

        public void setStoreName(String storeName2) {
            this.storeName = storeName2;
        }

        public String getVoucherId() {
            return this.voucherId;
        }

        public void setVoucherId(String voucherId2) {
            this.voucherId = voucherId2;
        }

        public String getVoucherType() {
            return this.voucherType;
        }

        public void setVoucherType(String voucherType2) {
            this.voucherType = voucherType2;
        }
    }
}
