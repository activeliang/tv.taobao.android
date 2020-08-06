package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.RelatedItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetWapRelatedItems extends BaseMtopRequest {
    private static final String API = "mtop.shop.getWapRelatedItems";
    private static final long serialVersionUID = 8769956883600572543L;
    private String itemId;
    private String sellerId;

    public GetWapRelatedItems(String itemId2, String sellerId2) {
        this.sellerId = sellerId2;
        this.itemId = itemId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.sellerId)) {
            obj.put("sellerId", this.sellerId);
        }
        if (!TextUtils.isEmpty(this.itemId)) {
            obj.put("itemId", this.itemId);
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public List<RelatedItem> resolveResponse(JSONObject obj) throws Exception {
        if (!obj.isNull("itemList")) {
            return JSON.parseArray(obj.getString("itemList"), RelatedItem.class);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }
}
