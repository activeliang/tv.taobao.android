package com.yunos.tvtaobao.biz.request.bo;

public class ResourceBean {
    private CouponBeanX coupon;
    private EntrancesBean entrances;

    public static class CouponBeanX {
    }

    public EntrancesBean getEntrances() {
        return this.entrances;
    }

    public void setEntrances(EntrancesBean entrances2) {
        this.entrances = entrances2;
    }

    public CouponBeanX getCoupon() {
        return this.coupon;
    }

    public void setCoupon(CouponBeanX coupon2) {
        this.coupon = coupon2;
    }

    public static class EntrancesBean {
        private CouponBean coupon;
        private TmallCouponBean tmallCoupon;

        public CouponBean getCoupon() {
            return this.coupon;
        }

        public void setCoupon(CouponBean coupon2) {
            this.coupon = coupon2;
        }

        public TmallCouponBean getTmallCoupon() {
            return this.tmallCoupon;
        }

        public void setTmallCoupon(TmallCouponBean tmallCoupon2) {
            this.tmallCoupon = tmallCoupon2;
        }

        public static class CouponBean {
            private String icon;
            private String linkText;
            private String text;

            public String getIcon() {
                return this.icon;
            }

            public void setIcon(String icon2) {
                this.icon = icon2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getLinkText() {
                return this.linkText;
            }

            public void setLinkText(String linkText2) {
                this.linkText = linkText2;
            }
        }

        public static class TmallCouponBean {
            private String icon;
            private String linkText;
            private String text;

            public String getIcon() {
                return this.icon;
            }

            public void setIcon(String icon2) {
                this.icon = icon2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getLinkText() {
                return this.linkText;
            }

            public void setLinkText(String linkText2) {
                this.linkText = linkText2;
            }
        }
    }
}
