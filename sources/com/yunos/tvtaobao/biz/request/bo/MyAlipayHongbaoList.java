package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAlipayHongbaoList implements Serializable {
    private static final long serialVersionUID = 895703763902174871L;
    private boolean activityTime;
    private String alipayDate;
    private ArrayList<MyAlipayHongbao> couponList;
    private boolean showFissionPacket;
    private String totalAmount;
    private String totalCount;

    public String getTotalCount() {
        return this.totalCount;
    }

    public String gettotalAmount() {
        return this.totalAmount;
    }

    public String getalipayDate() {
        return this.alipayDate;
    }

    public boolean getactivityTime() {
        return this.activityTime;
    }

    public boolean getshowFissionPacket() {
        return this.showFissionPacket;
    }

    public ArrayList<MyAlipayHongbao> getCouponList() {
        return this.couponList;
    }

    public void setTotalCount(String totalCount2) {
        this.totalCount = totalCount2;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(String totalAmount2) {
        this.totalAmount = totalAmount2;
    }

    public String getAlipayDate() {
        return this.alipayDate;
    }

    public void setAlipayDate(String alipayDate2) {
        this.alipayDate = alipayDate2;
    }

    public boolean isActivityTime() {
        return this.activityTime;
    }

    public void setActivityTime(boolean activityTime2) {
        this.activityTime = activityTime2;
    }

    public boolean isShowFissionPacket() {
        return this.showFissionPacket;
    }

    public void setShowFissionPacket(boolean showFissionPacket2) {
        this.showFissionPacket = showFissionPacket2;
    }

    public void setCouponList(ArrayList<MyAlipayHongbao> couponList2) {
        this.couponList = couponList2;
    }

    public String toString() {
        return "[ totalCount = " + this.totalCount + ", totalAmount = " + this.totalAmount + ", alipayDate = " + this.alipayDate + ", activityTime = " + this.activityTime + ", showFissionPacket = " + this.showFissionPacket + ", couponList = " + this.couponList + " ]";
    }
}
