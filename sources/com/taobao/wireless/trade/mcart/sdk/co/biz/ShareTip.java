package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class ShareTip {
    private JSONObject data;

    public ShareTip(JSONObject data2) {
        this.data = data2;
    }

    public long getStart() {
        return this.data.getLongValue("start");
    }

    public long getEnd() {
        return this.data.getLongValue("end");
    }

    public String getText1() {
        return this.data.getString("text1");
    }

    public String getText2() {
        return this.data.getString("text2");
    }
}
