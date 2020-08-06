package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetServerTimeRequest extends BaseMtopRequest {
    private static final String API = "mtop.common.getTimestamp";
    private static final String API_VERSION = "*";
    private static final long serialVersionUID = 4985157177343215998L;

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Long resolveResponse(JSONObject json) throws Exception {
        if (json == null || !json.has("t")) {
            return null;
        }
        long t = json.optLong("t");
        if (t != 0) {
            return Long.valueOf(t);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return API_VERSION;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
