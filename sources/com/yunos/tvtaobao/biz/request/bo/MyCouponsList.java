package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class MyCouponsList implements Serializable {
    private static final long serialVersionUID = 4986940290622063079L;
    private String bizTitle;
    private String bizType;
    private ArrayList<MyCoupon> couponList;
    private String couponType;
    private String totalNum;

    public String getBizType() {
        return this.bizType;
    }

    public String getBizTitle() {
        return this.bizTitle;
    }

    public String getCouponType() {
        return this.couponType;
    }

    public String getTotalNum() {
        return this.totalNum;
    }

    public ArrayList<MyCoupon> getCouponList() {
        return this.couponList;
    }

    public void setBizType(String bizType2) {
        this.bizType = bizType2;
    }

    public void setBizTitle(String bizTitle2) {
        this.bizTitle = bizTitle2;
    }

    public void setCouponType(String couponType2) {
        this.couponType = couponType2;
    }

    public void setTotalNum(String totalNum2) {
        this.totalNum = totalNum2;
    }

    public void setCouponList(ArrayList<MyCoupon> couponList2) {
        this.couponList = couponList2;
    }

    public String toString() {
        return "[ totalNum = " + this.totalNum + ", bizTitle = " + this.bizTitle + ", couponType = " + this.couponType + ", bizType = " + this.bizType + ", couponList = " + this.couponList + " ]";
    }
}
