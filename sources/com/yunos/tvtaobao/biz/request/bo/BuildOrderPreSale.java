package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class BuildOrderPreSale implements Serializable {
    private static final long serialVersionUID = 572761700631368713L;
    private String deliverTitle;
    private String deliverValue;
    private String endTime;
    private String extraText;
    private String notifyTitle;
    private String notifyValue;
    private String orderedItemAmount;
    private String presale;
    private String presaleTitle;
    private String priceText;
    private String priceTitle;
    private String promotion;
    private String startTime;
    private String status;
    private String text;
    private String tip;

    public String getPromotion() {
        return this.promotion;
    }

    public void setPromotion(String promotion2) {
        this.promotion = promotion2;
    }

    public String getDeliverTitle() {
        return this.deliverTitle;
    }

    public void setDeliverTitle(String deliverTitle2) {
        this.deliverTitle = deliverTitle2;
    }

    public String getDeliverValue() {
        return this.deliverValue;
    }

    public void setDeliverValue(String deliverValue2) {
        this.deliverValue = deliverValue2;
    }

    public String getPresaleTitle() {
        return this.presaleTitle;
    }

    public void setPresaleTitle(String presaleTitle2) {
        this.presaleTitle = presaleTitle2;
    }

    public String getPriceTitle() {
        return this.priceTitle;
    }

    public void setPriceTitle(String priceTitle2) {
        this.priceTitle = priceTitle2;
    }

    public String getPresale() {
        return this.presale;
    }

    public void setPresale(String presale2) {
        this.presale = presale2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip2) {
        this.tip = tip2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getPriceText() {
        return this.priceText;
    }

    public void setPriceText(String priceText2) {
        this.priceText = priceText2;
    }

    public String getExtraText() {
        return this.extraText;
    }

    public void setExtraText(String extraText2) {
        this.extraText = extraText2;
    }

    public String getOrderedItemAmount() {
        return this.orderedItemAmount;
    }

    public void setOrderedItemAmount(String orderedItemAmount2) {
        this.orderedItemAmount = orderedItemAmount2;
    }

    public String getNotifyValue() {
        return this.notifyValue;
    }

    public void setNotifyValue(String notifyValue2) {
        this.notifyValue = notifyValue2;
    }

    public String getNotifyTitle() {
        return this.notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle2) {
        this.notifyTitle = notifyTitle2;
    }

    public String toString() {
        return "BuildOrderPreSale{priceText='" + this.priceText + '\'' + ", priceTitle='" + this.priceTitle + '\'' + ", presale='" + this.presale + '\'' + ", presaleTitle='" + this.presaleTitle + '\'' + ", extraText='" + this.extraText + '\'' + ", orderedItemAmount='" + this.orderedItemAmount + '\'' + ", tip='" + this.tip + '\'' + ", startTime='" + this.startTime + '\'' + ", endTime='" + this.endTime + '\'' + ", status='" + this.status + '\'' + ", text='" + this.text + '\'' + ", notifyTitle='" + this.notifyTitle + '\'' + ", notifyValue='" + this.notifyValue + '\'' + ", deliverTitle='" + this.deliverTitle + '\'' + ", deliverValue='" + this.deliverValue + '\'' + ", promotion='" + this.promotion + '\'' + '}';
    }
}
