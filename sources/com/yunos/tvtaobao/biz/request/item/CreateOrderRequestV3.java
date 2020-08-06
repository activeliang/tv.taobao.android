package com.yunos.tvtaobao.biz.request.item;

import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CreateOrderResult;
import java.util.Map;
import org.json.JSONObject;

public class CreateOrderRequestV3 extends BaseMtopRequest {
    private static final String API = "mtop.trade.createOrder";
    private static final long serialVersionUID = 2946152565790016558L;
    private String params;

    public CreateOrderRequestV3(String params2) {
        this.params = params2;
        addParams("orderMarker", "m:terminal=alitv|v:uuid=" + CloudUUIDWrapper.getCloudUUID());
        addParams("params", params2);
        addParams("feature", "{\"gzip\":\"true\"}");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
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
        return false;
    }

    /* access modifiers changed from: protected */
    public CreateOrderResult resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return CreateOrderResult.fromMTOP(obj);
    }
}
