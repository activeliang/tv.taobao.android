package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ShopCoupon;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetShopCoupon extends BaseMtopRequest {
    private static final String API = "mtop.shop.querybuyerbonus";
    private static final long serialVersionUID = -1818780348054330252L;
    private String sellerId;

    public GetShopCoupon(String sellerId2) {
        this.sellerId = sellerId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.sellerId)) {
            obj.put("sellerId", this.sellerId);
        }
        if (!TextUtils.isEmpty(User.getUserId())) {
            obj.put("userId", User.getUserId());
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public List<ShopCoupon> resolveResponse(JSONObject obj) throws Exception {
        if (!obj.isNull("result")) {
            return JSON.parseArray(obj.getString("result"), ShopCoupon.class);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }
}
