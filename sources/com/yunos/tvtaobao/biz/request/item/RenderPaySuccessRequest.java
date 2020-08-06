package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class RenderPaySuccessRequest extends BaseMtopRequest {
    private String API = "mtop.trade.receipt.renderPaySuccess";
    private String version = "1.0";

    public RenderPaySuccessRequest(String idStr) {
        if (!TextUtils.isEmpty(idStr)) {
            addParams("mainBizOrderIdsStr", idStr);
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }
}
