package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetTaobaoPointValidateblackfilter extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.commonservice.validateblackfilter";
    private static final long serialVersionUID = 6403615798555209809L;

    public GetTaobaoPointValidateblackfilter(String categoryId, String itemId) {
        addParams("categoryId", categoryId);
        addParams("itemId", itemId);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Boolean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return false;
        }
        return Boolean.valueOf(obj.optBoolean("result"));
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
