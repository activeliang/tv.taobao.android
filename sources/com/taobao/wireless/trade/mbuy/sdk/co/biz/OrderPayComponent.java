package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class OrderPayComponent extends Component {
    public OrderPayComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public String getPrice() {
        return this.fields.getString("price");
    }

    public String getQuantity() {
        return this.fields.getString("quantity");
    }

    public String getWeight() {
        double weight = this.fields.getDoubleValue("weight");
        if (weight <= ClientTraceData.b.f47a) {
            return null;
        }
        return String.format("%.3f", new Object[]{Double.valueOf(weight / 1000.0d)});
    }

    public String toString() {
        return super.toString() + " - OrderPayComponent [price=" + getPrice() + ", quantity=" + getQuantity() + "weight=" + getWeight() + "]";
    }
}
