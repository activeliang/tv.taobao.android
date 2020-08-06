package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class ItemComponent extends Component {
    public ItemComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getItemId() {
        return this.fields.getString("itemId");
    }

    public String getSkuId() {
        return this.fields.getString("skuId");
    }

    public String getCartId() {
        return this.fields.getString("cartId");
    }

    public boolean isValid() {
        Boolean valid = this.fields.getBoolean("valid");
        if (valid != null) {
            return valid.booleanValue();
        }
        return true;
    }

    public String getReason() {
        return this.fields.getString("reason");
    }

    public String toString() {
        return super.toString() + " - ItemComponent [itemId=" + getItemId() + ", skuId=" + getSkuId() + ", cartId=" + getCartId() + ", valid=" + isValid() + "]";
    }
}
