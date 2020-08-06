package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

public class ParamsBean {
    private TrackParamsBean trackParams;

    public TrackParamsBean getTrackParams() {
        return this.trackParams;
    }

    public void setTrackParams(TrackParamsBean trackParams2) {
        this.trackParams = trackParams2;
    }

    public static class TrackParamsBean {
        private String BC_type;
        private String brandId;
        private String categoryId;

        public String getBrandId() {
            return this.brandId;
        }

        public void setBrandId(String brandId2) {
            this.brandId = brandId2;
        }

        public String getBC_type() {
            return this.BC_type;
        }

        public void setBC_type(String BC_type2) {
            this.BC_type = BC_type2;
        }

        public String getCategoryId() {
            return this.categoryId;
        }

        public void setCategoryId(String categoryId2) {
            this.categoryId = categoryId2;
        }
    }
}
