package com.yunos.tvtaobao.request;

import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class CheckLogStatusRequest extends BaseMtopRequest {
    /* access modifiers changed from: protected */
    public Boolean resolveResponse(JSONObject obj) throws Exception {
        if ("1".equals(obj.optString("result"))) {
            return true;
        }
        return false;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.device.logswitch.get";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userNick", User.getNick());
        return params;
    }
}
