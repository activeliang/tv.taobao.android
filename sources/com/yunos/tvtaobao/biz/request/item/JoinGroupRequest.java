package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.JoinGroupResult;
import java.util.Map;
import org.json.JSONObject;

public class JoinGroupRequest extends BaseMtopRequest {
    private static final String API = "mtop.ju.group.join";
    private static final long serialVersionUID = 6872185179642331711L;
    private String itemId;

    public JoinGroupRequest(String itemId2) {
        this.itemId = itemId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("itemId", this.itemId);
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public JoinGroupResult resolveResponse(JSONObject obj) throws Exception {
        return JoinGroupResult.fromMTOP(obj);
    }
}
