package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetTbItemCouponInfoRequest extends BaseMtopRequest {
    private static final String API = "tvactivity.bonus.query.taobaoItemBouns";
    private static final String KEY_ITEM_NUM_ID = "itemId";
    private static final long serialVersionUID = 7129574033956976483L;
    private String itemNumId = null;

    public GetTbItemCouponInfoRequest(String itemNumId2) {
        this.itemNumId = itemNumId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("itemId", this.itemNumId);
        return null;
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

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj != null && !obj.isNull("result")) {
            return obj.getString("result");
        }
        return null;
    }
}
