package com.taobao.wireless.trade.mbuy.sdk.co.misc;

import com.alibaba.fastjson.JSONObject;

public class Option {
    protected JSONObject data;
    public OptionStatus status = OptionStatus.NORMAL;

    public Option(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
        this.status = OptionStatus.getOptionStatusByDesc(this.data.getString("status"));
    }
}
