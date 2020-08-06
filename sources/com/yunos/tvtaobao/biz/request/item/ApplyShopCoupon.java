package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class ApplyShopCoupon extends BaseMtopRequest {
    public static final String API = "mtop.taobao.buyerResourceMtopWriteService.applyCoupon";
    private static final long serialVersionUID = -2588212548156853370L;
    private String spreadId;
    private String supplierId;
    private String uuid;

    public ApplyShopCoupon(String sellerId, String spreadId2, String uuid2) {
        this.supplierId = sellerId;
        this.spreadId = spreadId2;
        this.uuid = uuid2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.supplierId)) {
            obj.put("supplierId", this.supplierId);
        }
        if (!TextUtils.isEmpty(this.spreadId)) {
            obj.put("spreadId", this.spreadId);
        }
        if (!TextUtils.isEmpty(this.uuid)) {
            obj.put("uuid", this.uuid);
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
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
        return true;
    }
}
