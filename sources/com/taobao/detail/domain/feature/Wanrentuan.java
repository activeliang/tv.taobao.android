package com.taobao.detail.domain.feature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wanrentuan implements Serializable {
    public String finalLevelPrice;
    private Long finalPayment;
    public String formatTime;
    private boolean fullPayment;
    private List<String> giftsList;
    private String groupUC;
    private String maxAmount;
    public String originalPrice;
    private Long price;
    private String purchasedAmount;
    private Long remainTime;
    private int status;
    public String totalPrice;
    private int type;
    private List<String> wrtLevelCoupons;
    private List<Integer> wrtLevelFinalPrices = new ArrayList();
    private List<Integer> wrtLevelNeedCounts = new ArrayList();

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public boolean isFullPayment() {
        return this.fullPayment;
    }

    public void setFullPayment(boolean fullPayment2) {
        this.fullPayment = fullPayment2;
    }

    public Long getFinalPayment() {
        return this.finalPayment;
    }

    public void setFinalPayment(Long finalPayment2) {
        this.finalPayment = finalPayment2;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price2) {
        this.price = price2;
    }

    public Long getRemainTime() {
        return this.remainTime;
    }

    public void setRemainTime(Long remainTime2) {
        this.remainTime = remainTime2;
    }

    public String getGroupUC() {
        return this.groupUC;
    }

    public void setGroupUC(String groupUC2) {
        this.groupUC = groupUC2;
    }

    public String getMaxAmount() {
        return this.maxAmount;
    }

    public void setMaxAmount(String maxAmount2) {
        this.maxAmount = maxAmount2;
    }

    public String getPurchasedAmount() {
        return this.purchasedAmount;
    }

    public void setPurchasedAmount(String purchasedAmount2) {
        this.purchasedAmount = purchasedAmount2;
    }

    public List<Integer> getWrtLevelNeedCounts() {
        return this.wrtLevelNeedCounts;
    }

    public void setWrtLevelNeedCounts(List<Integer> wrtLevelNeedCounts2) {
        this.wrtLevelNeedCounts = wrtLevelNeedCounts2;
    }

    public List<Integer> getWrtLevelFinalPrices() {
        return this.wrtLevelFinalPrices;
    }

    public void setWrtLevelFinalPrices(List<Integer> wrtLevelFinalPrices2) {
        this.wrtLevelFinalPrices = wrtLevelFinalPrices2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public List<String> getWrtLevelCoupons() {
        return this.wrtLevelCoupons;
    }

    public void setWrtLevelCoupons(List<String> wrtLevelCoupons2) {
        this.wrtLevelCoupons = wrtLevelCoupons2;
    }

    public List<String> getGiftsList() {
        return this.giftsList;
    }

    public void setGiftsList(List<String> giftsList2) {
        this.giftsList = giftsList2;
    }
}
