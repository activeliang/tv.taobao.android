package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.io.Serializable;

public class Trade implements Serializable {
    private String buyEnable;
    private String cartEnable;
    private String redirectUrl;

    public String getBuyEnable() {
        return this.buyEnable;
    }

    public void setBuyEnable(String buyEnable2) {
        this.buyEnable = buyEnable2;
    }

    public String getCartEnable() {
        return this.cartEnable;
    }

    public void setCartEnable(String cartEnable2) {
        this.cartEnable = cartEnable2;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl2) {
        this.redirectUrl = redirectUrl2;
    }
}
