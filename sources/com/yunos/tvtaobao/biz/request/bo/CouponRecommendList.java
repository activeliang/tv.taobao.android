package com.yunos.tvtaobao.biz.request.bo;

import java.util.ArrayList;

public class CouponRecommendList {
    private ArrayList<CouponRecommend> list;
    private String recommendTemplateKey;
    private String recommendTitle;
    private String totalNum;

    public String getRecommendTemplateKey() {
        return this.recommendTemplateKey;
    }

    public String getTotalNum() {
        return this.totalNum;
    }

    public String getRecommendTitle() {
        return this.recommendTitle;
    }

    public ArrayList<CouponRecommend> getCouponRecommendList() {
        return this.list;
    }
}
