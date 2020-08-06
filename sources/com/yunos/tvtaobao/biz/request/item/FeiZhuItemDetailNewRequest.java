package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class FeiZhuItemDetailNewRequest extends BaseMtopRequest {
    private static final String API = "mtop.trip.traveldetailskip.detail.get";
    private static final String apiversion = "5.0";

    public FeiZhuItemDetailNewRequest(String itemid) {
        if (!TextUtils.isEmpty(itemid)) {
            addParams("itemId", itemid);
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return apiversion;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
