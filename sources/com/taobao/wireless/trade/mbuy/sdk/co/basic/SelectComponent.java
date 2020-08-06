package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class SelectComponent extends SelectBaseComponent<SelectOption> {
    public SelectComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    /* access modifiers changed from: protected */
    public SelectOption newOption(JSONObject data) {
        return new SelectOption(data);
    }

    /* access modifiers changed from: protected */
    public String getOptionId(SelectOption option) {
        return option.getId();
    }
}
