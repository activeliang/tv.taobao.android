package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class FeiZhuBean {
    private String buyText;
    private String cartText;
    private String flayerTitle;
    private boolean hasCoupon;
    private String mileageTitle;
    private String newPrice;
    private String oldPrice;
    private String rightDesc;
    private List<String> service;
    private String soldCount;

    public String getFlayerTitle() {
        return this.flayerTitle;
    }

    public void setFlayerTitle(String flayerTitle2) {
        this.flayerTitle = flayerTitle2;
    }

    public String getMileageTitle() {
        return this.mileageTitle;
    }

    public void setMileageTitle(String mileageTitle2) {
        this.mileageTitle = mileageTitle2;
    }

    public String getRightDesc() {
        return this.rightDesc;
    }

    public void setRightDesc(String rightDesc2) {
        this.rightDesc = rightDesc2;
    }

    public boolean isHasCoupon() {
        return this.hasCoupon;
    }

    public void setHasCoupon(boolean hasCoupon2) {
        this.hasCoupon = hasCoupon2;
    }

    public String getBuyText() {
        return this.buyText;
    }

    public void setBuyText(String buyText2) {
        this.buyText = buyText2;
    }

    public String getCartText() {
        return this.cartText;
    }

    public void setCartText(String cartText2) {
        this.cartText = cartText2;
    }

    public String getOldPrice() {
        return this.oldPrice;
    }

    public void setOldPrice(String oldPrice2) {
        this.oldPrice = oldPrice2;
    }

    public String getNewPrice() {
        return this.newPrice;
    }

    public void setNewPrice(String newPrice2) {
        this.newPrice = newPrice2;
    }

    public List<String> getService() {
        return this.service;
    }

    public void setService(List<String> service2) {
        this.service = service2;
    }

    public String getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(String soldCount2) {
        this.soldCount = soldCount2;
    }

    public String toString() {
        return "FeiZhuBean{service=" + this.service + ", soldCount='" + this.soldCount + '\'' + ", oldPrice='" + this.oldPrice + '\'' + ", newPrice='" + this.newPrice + '\'' + '}';
    }
}
