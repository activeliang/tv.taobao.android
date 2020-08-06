package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class RichSelectComponent extends SelectBaseComponent<RichSelectOption> {
    public RichSelectComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    /* access modifiers changed from: protected */
    public RichSelectOption newOption(JSONObject data) throws Exception {
        return new RichSelectOption(data);
    }

    /* access modifiers changed from: protected */
    public String getOptionId(RichSelectOption option) {
        return option.getOptionId();
    }

    public String getUrl() {
        return getRuleUrl();
    }

    public String getRuleUrl() {
        return this.fields.getString("ruleUrl");
    }

    public String getIcon() {
        return this.fields.getString("icon");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public String getOptionTitle() {
        return this.fields.getString("optionTitle");
    }
}
