package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class DynamicComponent extends Component {
    public DynamicComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        if (getRender() == null) {
            throw new IllegalArgumentException();
        }
    }
}
