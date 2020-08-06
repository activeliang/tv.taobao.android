package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class JhsItemInfo implements Serializable {
    private static final long serialVersionUID = -6565653073356907357L;
    private Long activityPrice;
    private String discount;
    private Long groupId;
    private String jhsItemStatus;
    private Integer limitNum;
    private String onlineEndTime;
    private String onlineStartTime;
    private String payPostage;
    private Integer soldCount;

    private enum JhsItemInfoStateEnum {
        WAIT_FOR_START,
        AVAIL_BUY,
        EXIST_HOLDER,
        NO_STOCK,
        OUT_OF_TIME
    }

    public Long getActivityPrice() {
        return this.activityPrice;
    }

    public void setActivityPrice(Long activityPrice2) {
        this.activityPrice = activityPrice2;
    }

    public String getDiscount() {
        return this.discount;
    }

    public void setDiscount(String discount2) {
        this.discount = discount2;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId2) {
        this.groupId = groupId2;
    }

    public Integer getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(Integer soldCount2) {
        this.soldCount = soldCount2;
    }

    public Integer getLimitNum() {
        return this.limitNum;
    }

    public void setLimitNum(Integer limitNum2) {
        this.limitNum = limitNum2;
    }

    public String getOnlineStartTime() {
        return this.onlineStartTime;
    }

    public void setOnlineStartTime(String onlineStartTime2) {
        this.onlineStartTime = onlineStartTime2;
    }

    public String getOnlineEndTime() {
        return this.onlineEndTime;
    }

    public void setOnlineEndTime(String onlineEndTime2) {
        this.onlineEndTime = onlineEndTime2;
    }

    public String getJhsItemStatus() {
        return this.jhsItemStatus;
    }

    public void setJhsItemStatus(String jhsItemStatus2) {
        this.jhsItemStatus = jhsItemStatus2;
    }

    public String getPayPostage() {
        return this.payPostage;
    }

    public void setPayPostage(String payPostage2) {
        this.payPostage = payPostage2;
    }

    public static JhsItemInfo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        JhsItemInfo jhsItemInfo = new JhsItemInfo();
        if (!obj.isNull("activityPrice")) {
            jhsItemInfo.setActivityPrice(Long.valueOf(obj.getLong("activityPrice")));
        }
        if (!obj.isNull("discount")) {
            jhsItemInfo.setDiscount(obj.getString("discount"));
        }
        if (!obj.isNull("groupId")) {
            jhsItemInfo.setGroupId(Long.valueOf(obj.getLong("groupId")));
        }
        if (!obj.isNull("soldCount")) {
            jhsItemInfo.setSoldCount(Integer.valueOf(obj.getInt("soldCount")));
        }
        if (!obj.isNull("limitNum")) {
            jhsItemInfo.setLimitNum(Integer.valueOf(obj.getInt("limitNum")));
        }
        if (!obj.isNull("onlineStartTime")) {
            jhsItemInfo.setOnlineEndTime(obj.getString("onlineStartTime"));
        }
        if (!obj.isNull("onlineEndTime")) {
            jhsItemInfo.setOnlineEndTime(obj.getString("onlineEndTime"));
        }
        if (!obj.isNull("jhsItemStatus")) {
            jhsItemInfo.setJhsItemStatus(obj.getString("jhsItemStatus"));
        }
        if (obj.isNull("payPostage")) {
            return jhsItemInfo;
        }
        jhsItemInfo.setPayPostage(obj.getString("payPostage"));
        return jhsItemInfo;
    }

    public boolean isAbleBuy() {
        return this.jhsItemStatus.equals(JhsItemInfoStateEnum.AVAIL_BUY.name());
    }

    public boolean isWaitForStart() {
        return this.jhsItemStatus.equals(JhsItemInfoStateEnum.WAIT_FOR_START.name());
    }

    public boolean isHolder() {
        return this.jhsItemStatus.equals(JhsItemInfoStateEnum.EXIST_HOLDER.name());
    }

    public boolean isNoStock() {
        return this.jhsItemStatus.equals(JhsItemInfoStateEnum.NO_STOCK.name());
    }

    public boolean isEnd() {
        return this.jhsItemStatus.equals(JhsItemInfoStateEnum.OUT_OF_TIME.name());
    }

    public String getBuyStatus() {
        if (isWaitForStart()) {
            return "即将开始";
        }
        if (isAbleBuy()) {
            return "立即购买";
        }
        if (isHolder()) {
            return "已占座";
        }
        if (isNoStock()) {
            return "已卖光";
        }
        if (isEnd()) {
            return "已结束";
        }
        return "立即购买";
    }
}
