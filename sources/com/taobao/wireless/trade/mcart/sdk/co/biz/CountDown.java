package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;

public class CountDown {
    private JSONObject data;

    public CountDown(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getText() {
        return this.data.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
    }

    public long getStart() {
        return this.data.getLongValue("start");
    }

    public long getEnd() {
        return this.data.getLongValue("end");
    }

    public long getcdEnd() {
        return this.data.getLongValue("cdEnd");
    }

    public long getEndTime() {
        return this.data.getLongValue("endTime");
    }
}
