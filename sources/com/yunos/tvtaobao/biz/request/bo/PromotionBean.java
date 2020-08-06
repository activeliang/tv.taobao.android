package com.yunos.tvtaobao.biz.request.bo;

public class PromotionBean {
    private DataBean data;
    private String msg;
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg2) {
        this.msg = msg2;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public static class DataBean {
        private String ali_seller_id;
        private String ali_spread_id;
        private int amount;
        private String coupon_type_name;
        private String end_time;
        private String shop_name;
        private String spread_id;
        private int start_fee;
        private String start_time;
        private String title;

        public String getAli_spread_id() {
            return this.ali_spread_id;
        }

        public void setAli_spread_id(String ali_spread_id2) {
            this.ali_spread_id = ali_spread_id2;
        }

        public String getAli_seller_id() {
            return this.ali_seller_id;
        }

        public void setAli_seller_id(String ali_seller_id2) {
            this.ali_seller_id = ali_seller_id2;
        }

        public String getSpread_id() {
            return this.spread_id;
        }

        public void setSpread_id(String spread_id2) {
            this.spread_id = spread_id2;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount2) {
            this.amount = amount2;
        }

        public String getCoupon_type_name() {
            return this.coupon_type_name;
        }

        public void setCoupon_type_name(String coupon_type_name2) {
            this.coupon_type_name = coupon_type_name2;
        }

        public String getShop_name() {
            return this.shop_name;
        }

        public void setShop_name(String shop_name2) {
            this.shop_name = shop_name2;
        }

        public String getStart_time() {
            return this.start_time;
        }

        public void setStart_time(String start_time2) {
            this.start_time = start_time2;
        }

        public String getEnd_time() {
            return this.end_time;
        }

        public void setEnd_time(String end_time2) {
            this.end_time = end_time2;
        }

        public int getStart_fee() {
            return this.start_fee;
        }

        public void setStart_fee(int start_fee2) {
            this.start_fee = start_fee2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }
    }
}
