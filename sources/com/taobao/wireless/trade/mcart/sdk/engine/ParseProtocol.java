package com.taobao.wireless.trade.mcart.sdk.engine;

import com.alibaba.fastjson.JSONObject;

public interface ParseProtocol {
    void parse(String str, JSONObject jSONObject);
}
