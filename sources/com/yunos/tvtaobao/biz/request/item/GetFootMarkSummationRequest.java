package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GetFootMarkSummationResponse;
import java.util.Map;
import org.json.JSONObject;

public class GetFootMarkSummationRequest extends BaseMtopRequest {
    /* access modifiers changed from: protected */
    public GetFootMarkSummationResponse resolveResponse(JSONObject jsonObject) throws Exception {
        try {
            return (GetFootMarkSummationResponse) JSON.parseObject(jsonObject.toString(), GetFootMarkSummationResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.cmin.mycount";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
