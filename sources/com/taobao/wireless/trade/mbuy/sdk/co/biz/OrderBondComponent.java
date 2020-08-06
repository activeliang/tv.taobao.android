package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class OrderBondComponent extends Component {
    public OrderBondComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getQuantity() {
        return this.fields.getString("quantity");
    }

    public String toString() {
        return super.toString() + " - OrderBondComponent [quantity=" + getQuantity() + "]";
    }
}
