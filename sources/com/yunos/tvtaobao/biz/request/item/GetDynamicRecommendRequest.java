package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.DynamicRecommend;
import java.util.Map;
import org.json.JSONObject;

public class GetDynamicRecommendRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.payresult.getHotInfo";

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public DynamicRecommend resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (DynamicRecommend) JSON.parseObject(obj.toString(), DynamicRecommend.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
