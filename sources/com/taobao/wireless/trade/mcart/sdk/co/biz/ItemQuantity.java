package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class ItemQuantity {
    private JSONObject data;

    public ItemQuantity(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public long getQuantity() {
        return this.data.getLongValue("quantity");
    }

    public long getMax() {
        return this.data.getLongValue("max");
    }

    public int getMin() {
        return this.data.getIntValue("min");
    }

    public int getMultiple() {
        return this.data.getIntValue("multiple");
    }

    public boolean isEditable() {
        return this.data.getBooleanValue("editable");
    }

    public String toString() {
        return super.toString() + " - QuantityComponent [quantity=" + getQuantity() + ",max=" + getMax() + ",min=" + getMin() + ",multiple=" + getMultiple() + ",editable=" + isEditable() + "]";
    }
}
