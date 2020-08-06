package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class OrderInfoComponent extends Component {
    public OrderInfoComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getIcon() {
        JSONObject iconNode = this.fields.getJSONObject("icon");
        if (iconNode != null) {
            return iconNode.getString("image");
        }
        return null;
    }

    public String toString() {
        return super.toString() + " - OrderGroupComponent [title=" + getTitle() + ", icon=" + getIcon() + "]";
    }
}
