package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class ShopCoupon implements Serializable {
    private static final long serialVersionUID = 5565784138138808947L;
    private String activityId;
    private String bonusName;
    private boolean canApply;
    private String desc;
    private String discountFee;
    private String endTime;
    private int ownNum;
    private String startTime;
    private int type;
    private String validTime;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId2) {
        this.activityId = activityId2;
    }

    public String getBonusName() {
        return this.bonusName;
    }

    public void setBonusName(String bonusName2) {
        this.bonusName = bonusName2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getDiscountFee() {
        return this.discountFee;
    }

    public void setDiscountFee(String discountFee2) {
        this.discountFee = discountFee2;
    }

    public String getValidTime() {
        return this.validTime;
    }

    public void setValidTime(String validTime2) {
        this.validTime = validTime2;
    }

    public boolean isCanApply() {
        return this.canApply;
    }

    public void setCanApply(boolean canApply2) {
        this.canApply = canApply2;
    }

    public int getOwnNum() {
        return this.ownNum;
    }

    public void setOwnNum(int ownNum2) {
        this.ownNum = ownNum2;
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
}
