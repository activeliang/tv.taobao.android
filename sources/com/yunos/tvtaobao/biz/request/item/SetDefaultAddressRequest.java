package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class SetDefaultAddressRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.mtop.deliver.editAddressStatus";
    private static final long serialVersionUID = -4988470284016262595L;
    private String deliverId;

    public SetDefaultAddressRequest(String deliverId2) {
        this.deliverId = deliverId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("deliverId", this.deliverId);
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "*";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
