package com.taobao.wireless.trade.mcart.sdk.co.mtop;

import com.alibaba.fastjson.JSONObject;
import mtopsdk.mtop.domain.BaseOutDo;

public class MtopTradeQueryBagListResponse extends BaseOutDo {
    private JSONObject data;

    public void setData(JSONObject data2) {
        this.data = data2;
    }

    public JSONObject getData() {
        return this.data;
    }
}
