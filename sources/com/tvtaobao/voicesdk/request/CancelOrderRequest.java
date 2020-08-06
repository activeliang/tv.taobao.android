package com.tvtaobao.voicesdk.request;

import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class CancelOrderRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.speech.order.cancelDelayCreatingOrder";
    private final String TAG = "CancelOrderRequest";
    private final String VERSION = "1.0";

    public CancelOrderRequest(String outOrderId) {
        addParams("outOrderId", outOrderId);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        LogPrint.e("TVTao_CancelOrder", "obj : " + obj);
        return obj.getString(UpdatePreference.UT_CANCEL);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.order.cancelDelayCreatingOrder";
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
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
