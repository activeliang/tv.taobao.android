package com.taobao.wireless.trade.mcart.sdk.co.biz;

import java.util.List;

public class GroupChargeTotalData {
    private List<GroupChargeData> mGroupChargeDatas;
    private String title;

    public GroupChargeTotalData() {
    }

    public GroupChargeTotalData(String title2, List<GroupChargeData> groupChargeDatas) {
        this.title = title2;
        this.mGroupChargeDatas = groupChargeDatas;
    }

    public String getTitle() {
        return this.title == null ? "" : this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public List<GroupChargeData> getGroupChargeDatas() {
        return this.mGroupChargeDatas;
    }

    public void setGroupChargeDatas(List<GroupChargeData> groupChargeDatas) {
        this.mGroupChargeDatas = groupChargeDatas;
    }
}
