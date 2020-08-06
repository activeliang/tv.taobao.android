package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class FinalPayComponent extends Component {
    public FinalPayComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public String getDescColor() {
        return this.fields.getString("descColor");
    }

    public String geValueColor() {
        return this.fields.getString("valueColor");
    }
}
