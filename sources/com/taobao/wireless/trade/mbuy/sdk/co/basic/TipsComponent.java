package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class TipsComponent extends Component {
    private TipsType tipsType = TipsType.getTipsTypeByDesc(this.fields.getString("tipsType"));

    public TipsComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        if (this.tipsType == null || getResource() == null) {
            throw new IllegalArgumentException();
        }
    }

    public TipsType getTipsType() {
        return this.tipsType;
    }

    public String getResource() {
        return this.fields.getString("resource");
    }
}
