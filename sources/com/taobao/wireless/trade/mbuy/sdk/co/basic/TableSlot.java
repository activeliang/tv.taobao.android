package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;

public class TableSlot {
    private JSONObject data;

    public TableSlot(JSONObject data2) {
        this.data = data2;
    }

    public String getValue() {
        return this.data.getString("value");
    }

    public TextStyle getTextStyle() {
        JSONObject d = this.data.getJSONObject("css");
        if (d != null) {
            return new TextStyle(d);
        }
        return null;
    }
}
