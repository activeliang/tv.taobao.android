package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class RealPayComponent extends Component {
    public RealPayComponent(JSONObject data, BuyEngine engine) {
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

    public String toString() {
        return super.toString() + " - RealPayComponent [price=" + getPrice() + ", quantity=" + getQuantity() + "]";
    }
}
