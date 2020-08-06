package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class Quantity {
    private JSONObject data;

    public Quantity(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public int getValue() {
        return this.data.getIntValue("value");
    }

    public void setValue(int value) {
        this.data.put("value", (Object) Integer.valueOf(value));
    }

    public String toString() {
        return "Quantity [value=" + getValue() + "]";
    }
}
