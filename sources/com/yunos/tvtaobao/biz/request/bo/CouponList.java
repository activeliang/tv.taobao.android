package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class CouponList implements Serializable {
    private static final long serialVersionUID = -2982865539369660900L;
    private ArrayList<Coupon> list;
    private String totalNums;

    public ArrayList<Coupon> getList() {
        return this.list;
    }

    public void setList(ArrayList<Coupon> list2) {
        this.list = list2;
    }

    public String getTotalNums() {
        return this.totalNums;
    }

    public void setTotalNums(String totalNums2) {
        this.totalNums = totalNums2;
    }
}
