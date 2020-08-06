package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AddBagRequest extends BaseMtopRequest {
    private static final String API = "mtop.trade.addBag";
    private static final long serialVersionUID = -7010317316195443367L;
    private String extParams;
    private String itemId;
    private int quantity;
    private String skuId;

    public AddBagRequest(String itemId2, int quantity2, String skuId2, String extParams2) {
        this.itemId = itemId2;
        this.quantity = quantity2;
        this.skuId = skuId2;
        this.extParams = extParams2;
        ZpLogger.e(this.TAG, "itemId = " + itemId2 + " quantity = " + quantity2 + "  skuId = " + skuId2 + " extParams = " + extParams2);
        ZpLogger.i("[tvOptionsDebug] mtop.trade.addBag exParams ----> ", extParams2 == null ? "" : extParams2);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(this.itemId)) {
            params.put("itemId", this.itemId);
        }
        params.put("quantity", String.valueOf(this.quantity));
        params.put("skuId", this.skuId);
        if (!TextUtils.isEmpty(this.extParams)) {
            params.put(ApiUnitHelper.EX_QUERY_KEY, this.extParams);
        }
        return params;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.1";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public <T> T resolveResponse(JSONObject obj) throws Exception {
        return null;
    }
}
