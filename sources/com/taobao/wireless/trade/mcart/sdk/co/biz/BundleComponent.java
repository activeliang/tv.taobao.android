package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class BundleComponent extends Component {
    public BundleComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public boolean isValid() {
        return this.fields.getBooleanValue("valid");
    }

    public String getBundleId() {
        return this.fields.getString("bundleId");
    }

    public String toString() {
        return super.toString() + " - BundleComponent [valid=" + isValid() + ",bundleId=" + getBundleId() + "]";
    }
}
