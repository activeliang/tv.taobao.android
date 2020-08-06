package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class ShopPromotionDetailOption {
    private JSONObject data;

    public ShopPromotionDetailOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getIcon() {
        return this.data.getString("icon");
    }

    public String getDesc() {
        return this.data.getString("desc");
    }

    public String getValue() {
        return this.data.getString("value");
    }
}
