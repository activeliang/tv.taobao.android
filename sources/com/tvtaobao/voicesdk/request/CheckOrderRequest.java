package com.tvtaobao.voicesdk.request;

import android.text.TextUtils;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class CheckOrderRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.orderextservice.searchLastTradeLogistics";

    public CheckOrderRequest(String timeText, String q, String start, String end) {
        addParams("timeText", timeText);
        addParams("q", q);
        addParams("beginTime", start);
        addParams("endTime", end);
        addParams("v", "2.0");
    }

    public CheckOrderRequest(String timeText, String q, String start, String end, String v) {
        addParams("timeText", timeText);
        addParams("q", q);
        addParams("beginTime", start);
        addParams("endTime", end);
        if (TextUtils.isEmpty(v)) {
            addParams("v", "2.0");
        } else {
            addParams("v", v);
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        LogPrint.w("TVTao_CheckOrderRequest", "obj : " + obj);
        return obj;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
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
        return true;
    }
}
