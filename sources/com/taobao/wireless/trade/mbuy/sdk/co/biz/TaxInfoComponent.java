package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class TaxInfoComponent extends Component {
    public TaxInfoComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public String getTax() {
        return this.fields.getString("tax");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    public boolean isStrikeThrough() {
        return this.fields.getBooleanValue("strikeThrough");
    }
}
