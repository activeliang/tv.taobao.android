package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderDeliveryData;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutGetOrderDeliveryRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 8628119122639344258L;
    private final String API = "mtop.taobao.waimai.cheetah.getelmtrackinginfosafe";
    private final String version = "1.0";

    public TakeOutGetOrderDeliveryRequest(String mainOrderId) {
        addParams("tbMainOrderId", mainOrderId);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.waimai.cheetah.getelmtrackinginfosafe";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public TakeOutOrderDeliveryData resolveResponse(JSONObject obj) throws Exception {
        return TakeOutOrderDeliveryData.resolverFromMtop(obj);
    }
}
