package com.yunos.tvtaobao.biz.request.item;

import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.Cat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetShopCatInfoRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.search.api.getCatInfoInShop";
    private static final long serialVersionUID = -2585365513297237130L;
    private String catId = WVPackageMonitorInterface.FORCE_ONLINE_FAILED;
    private String sellerId;
    private String shopId;

    public GetShopCatInfoRequest(String sellerId2, String shopId2) {
        this.sellerId = sellerId2;
        this.shopId = shopId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.sellerId)) {
            obj.put("sellerId", this.sellerId);
        }
        if (!TextUtils.isEmpty(this.shopId)) {
            obj.put("shopId", this.shopId);
        }
        if (!TextUtils.isEmpty(this.catId)) {
            obj.put("catId", this.catId);
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public List<Cat> resolveResponse(JSONObject obj) throws Exception {
        if (!obj.isNull("cats")) {
            return JSON.parseArray(obj.getString("cats"), Cat.class);
        }
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
}
