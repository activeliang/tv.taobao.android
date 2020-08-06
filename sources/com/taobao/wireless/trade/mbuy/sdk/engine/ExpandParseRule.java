package com.taobao.wireless.trade.mbuy.sdk.engine;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;

public interface ExpandParseRule {
    Component expandComponent(JSONObject jSONObject, BuyEngine buyEngine);
}
