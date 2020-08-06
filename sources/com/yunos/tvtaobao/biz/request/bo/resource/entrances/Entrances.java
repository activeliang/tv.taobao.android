package com.yunos.tvtaobao.biz.request.bo.resource.entrances;

import java.io.Serializable;

public class Entrances implements Serializable {
    private Coupon mCoupon;

    public Coupon getCoupon() {
        return this.mCoupon;
    }

    public void setCoupon(Coupon coupon) {
        this.mCoupon = coupon;
    }
}
