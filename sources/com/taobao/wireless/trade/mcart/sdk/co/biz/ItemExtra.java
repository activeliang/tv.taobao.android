package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class ItemExtra {
    private JSONObject mData;

    public ItemExtra(JSONObject data) {
        if (data == null) {
            throw new IllegalStateException();
        }
        this.mData = data;
    }

    public boolean isPriorityBuy() {
        if (this.mData.getIntValue("isPriorityBuy") == 1) {
            return true;
        }
        return false;
    }

    public int getIsPriorityBuy() {
        return this.mData.getIntValue("isPriorityBuy");
    }

    public JSONObject getData() {
        return this.mData;
    }

    public String toString() {
        return super.toString() + " - ItemExtra [" + ",isPriorityBuy=" + this.mData.getLongValue("isPriorityBuy") + "]";
    }
}
