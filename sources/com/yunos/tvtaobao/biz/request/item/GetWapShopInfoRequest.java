package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.SellerInfo;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class GetWapShopInfoRequest extends BaseMtopRequest {
    private static final String API = "mtop.shop.getWapShopInfo";
    private static final long serialVersionUID = -4340652315471148802L;
    private String IsUserNickEncoded;
    private String sellerId;
    private String shopId;
    private String userNick;

    public GetWapShopInfoRequest(String sellerId2, String shopId2, String userNick2, String IsUserNickEncoded2) {
        this.sellerId = sellerId2;
        this.shopId = shopId2;
        this.userNick = userNick2;
        this.IsUserNickEncoded = IsUserNickEncoded2;
    }

    public GetWapShopInfoRequest(String sellerId2, String shopId2) {
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
        if (!TextUtils.isEmpty(this.userNick)) {
            obj.put("userNick", this.userNick);
        }
        if (!TextUtils.isEmpty(this.IsUserNickEncoded)) {
            obj.put("IsUserNickEncoded", this.IsUserNickEncoded);
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public SellerInfo resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.toString())) {
            return (SellerInfo) JSON.parseObject(obj.toString(), SellerInfo.class);
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
