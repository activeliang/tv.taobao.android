package com.taobao.wireless.trade.mbuy.sdk.co.misc;

import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

public class SkuLevelInfo {
    private JSONObject data;

    public SkuLevelInfo(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getName() {
        return this.data.getString("name");
    }

    public String getValue() {
        return this.data.getString("value");
    }

    public String getColor() {
        return this.data.getString(UpdatePreference.COLOR);
    }
}
