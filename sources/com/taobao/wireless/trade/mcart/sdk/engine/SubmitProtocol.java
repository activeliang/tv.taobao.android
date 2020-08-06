package com.taobao.wireless.trade.mcart.sdk.engine;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.service.RequestType;
import java.util.Map;

public interface SubmitProtocol {
    void setExParamsMap(Map<String, String> map, RequestType requestType);

    void setSubmitData(JSONObject jSONObject, RequestType requestType);
}
