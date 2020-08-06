package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AdjustBuildOrderRequest extends BaseMtopRequest {
    private static final String API = "mtop.trade.adjustBuildOrder";
    private static final long serialVersionUID = 4951719698201296155L;
    private String params;

    public AdjustBuildOrderRequest(String params2) {
        this.params = params2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> paramsObj = new HashMap<>();
        if (!TextUtils.isEmpty(this.params)) {
            paramsObj.put("params", this.params);
        }
        paramsObj.put("feature", "{\"gzip\":\"true\"}");
        return paramsObj;
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
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
