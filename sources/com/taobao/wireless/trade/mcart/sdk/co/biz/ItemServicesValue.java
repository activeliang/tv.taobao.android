package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class ItemServicesValue {
    private JSONObject data;

    public ItemServicesValue(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getName() {
        return this.data.getString("name");
    }

    public String getId() {
        return this.data.getString("id");
    }

    public String toString() {
        return "[name=" + getName() + ", id=" + getId() + "]";
    }
}
