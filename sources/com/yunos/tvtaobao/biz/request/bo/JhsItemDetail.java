package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class JhsItemDetail implements Serializable {
    private static final long serialVersionUID = -2982865539369660901L;
    private Long activityPrice;
    private Double discount;
    private Long hangtagPrice;
    private Long id;
    private int isLock;
    private Integer itemDisplayStatus;
    private Long itemId;
    private String longName;
    private Long onlineEndTime;
    private Long onlineStartTime;
    private Long originalPrice;
    private String picUrl;
    private String picUrlNew;
    private String shortName;
    private List<String> showTagNames;
    private int soldCount;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId2) {
        this.itemId = itemId2;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName2) {
        this.shortName = shortName2;
    }

    public String getLongName() {
        return this.longName;
    }

    public void setLongName(String longName2) {
        this.longName = longName2;
    }

    public Long getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(Long originalPrice2) {
        this.originalPrice = originalPrice2;
    }

    public Long getActivityPrice() {
        return this.activityPrice;
    }

    public void setActivityPrice(Long activityPrice2) {
        this.activityPrice = activityPrice2;
    }

    public Long getHangtagPrice() {
        return this.hangtagPrice;
    }

    public void setHangtagPrice(Long hangtagPrice2) {
        this.hangtagPrice = hangtagPrice2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicUrlNew() {
        return this.picUrlNew;
    }

    public void setPicUrlNew(String picUrlNew2) {
        this.picUrlNew = picUrlNew2;
    }

    public int getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(int soldCount2) {
        this.soldCount = soldCount2;
    }

    public int getIsLock() {
        return this.isLock;
    }

    public void setIsLock(int isLock2) {
        this.isLock = isLock2;
    }

    public Long getOnlineStartTime() {
        return this.onlineStartTime;
    }

    public void setOnlineStartTime(Long onlineStartTime2) {
        this.onlineStartTime = onlineStartTime2;
    }

    public Long getOnlineEndTime() {
        return this.onlineEndTime;
    }

    public void setOnlineEndTime(Long onlineEndTime2) {
        this.onlineEndTime = onlineEndTime2;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discount2) {
        this.discount = discount2;
    }

    public Integer getItemDisplayStatus() {
        return this.itemDisplayStatus;
    }

    public void setItemDisplayStatus(Integer itemDisplayStatus2) {
        this.itemDisplayStatus = itemDisplayStatus2;
    }

    public List<String> getShowTagNames() {
        return this.showTagNames;
    }

    public void setShowTagNames(List<String> showTagNames2) {
        this.showTagNames = showTagNames2;
    }
}
