package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class QueryBagRequestBo implements Serializable {
    private static final long serialVersionUID = 3162775385145816370L;
    private String cartFrom;
    private int extStatus = 0;
    private String feature;
    private boolean isPage = false;
    private int netType = 0;
    private String p = null;

    public int getExtStatus() {
        return this.extStatus;
    }

    public void setExtStatus(int extStatus2) {
        this.extStatus = extStatus2;
    }

    public int getNetType() {
        return this.netType;
    }

    public void setNetType(int netType2) {
        this.netType = netType2;
    }

    public String getP() {
        return this.p;
    }

    public void setP(String p2) {
        this.p = p2;
    }

    public boolean isPage() {
        return this.isPage;
    }

    public void setPage(boolean isPage2) {
        this.isPage = isPage2;
    }

    public String getFeature() {
        return this.feature;
    }

    public void setFeature(String feature2) {
        this.feature = feature2;
    }

    public String getCartFrom() {
        return this.cartFrom;
    }

    public void setCartFrom(String cartFrom2) {
        this.cartFrom = cartFrom2;
    }

    public String toString() {
        return "QueryBagRequestBo{extStatus=" + this.extStatus + ", netType=" + this.netType + ", p='" + this.p + '\'' + ", isPage=" + this.isPage + ", feature='" + this.feature + '\'' + ", cartFrom='" + this.cartFrom + '\'' + '}';
    }
}
