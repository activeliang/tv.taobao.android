package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import com.taobao.wireless.trade.mcart.sdk.utils.NotificationCenterImpl;

public class CheckAll {
    protected CartFrom cartFrom = CartFrom.DEFAULT_CLIENT;
    private JSONObject data;

    public CheckAll(JSONObject data2, CartFrom cartFrom2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.cartFrom = cartFrom2 == null ? CartFrom.DEFAULT_CLIENT : cartFrom2;
        this.data = data2;
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public boolean isChecked() {
        return this.data.getBooleanValue("checked");
    }

    public boolean isEditable() {
        return this.data.getBooleanValue("editable");
    }

    public JSONObject getData() {
        return this.data;
    }

    public void setChecked(boolean isChecked, boolean notifyMessage) {
        this.data.put("checked", (Object) Boolean.valueOf(isChecked));
        ComponentBizUtil.refreshAllComponentCheckStatus(isChecked, this.cartFrom);
        ComponentBizUtil.refreshComponentInfoWithoutCheckStatus(this.cartFrom);
        if (notifyMessage) {
            NotificationCenterImpl.getInstance().postNotification(McartConstants.CART_CHECK_SUCCESS, this);
        }
    }

    public String toString() {
        return "CheckAll [title=" + getTitle() + ",checked=" + isChecked() + ",editable=" + isEditable() + "]";
    }
}
