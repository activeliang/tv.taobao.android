package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;

public class SelectOption {
    protected JSONObject data;

    public SelectOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getId() {
        return this.data.getString("optionId");
    }

    public String getName() {
        return this.data.getString("name");
    }

    public String getPrice() {
        return this.data.getString("value");
    }
}
