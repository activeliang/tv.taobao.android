package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class Weight {
    private JSONObject data;

    public Weight(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public long getValue() {
        return this.data.getLongValue("value");
    }

    public void setTitle(String title) {
        this.data.put("title", (Object) title);
    }

    public void setValue(long value) {
        this.data.put("value", (Object) Long.valueOf(value));
    }

    public String toString() {
        return "Weight [title=" + getTitle() + ",value=" + getValue() + "]";
    }
}
