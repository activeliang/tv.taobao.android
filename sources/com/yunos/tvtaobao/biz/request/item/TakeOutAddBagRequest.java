package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.AddBagBo;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutAddBagRequest extends BaseMtopRequest {
    private String API = "mtop.trade.addbag";
    private String version = "3.1";

    public TakeOutAddBagRequest(String itemId, String skuId, String quantity, String exParams, String cartFrom) {
        if (!TextUtils.isEmpty(itemId)) {
            addParams("itemId", itemId);
        }
        if (!TextUtils.isEmpty(skuId)) {
            addParams("skuId", skuId);
        }
        if (!TextUtils.isEmpty(quantity)) {
            addParams("quantity", quantity);
        }
        if (!TextUtils.isEmpty(exParams)) {
            addParams(ApiUnitHelper.EX_QUERY_KEY, exParams);
        }
        if (!TextUtils.isEmpty(cartFrom)) {
            addParams("cartFrom", cartFrom);
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
    public AddBagBo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (AddBagBo) JSON.parseObject(obj.toString(), AddBagBo.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
