package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class CouponOption {
    private JSONObject data;

    public CouponOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getIcon() {
        return this.data.getString("icon");
    }

    public String getStoreName() {
        return this.data.getString("storeName");
    }

    public String getType() {
        return this.data.getString("type");
    }

    public String getValue() {
        return this.data.getString("value");
    }
}
