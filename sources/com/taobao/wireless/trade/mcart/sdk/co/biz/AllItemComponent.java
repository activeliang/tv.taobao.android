package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class AllItemComponent extends Component {
    public AllItemComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public Integer getValue() {
        return this.fields.getInteger("value");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String toString() {
        return super.toString() + " - AllItemComponent [value=" + getValue() + ",title=" + getTitle() + "]";
    }
}
