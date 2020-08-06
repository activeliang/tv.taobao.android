package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.GoodsList;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONObject;

public class GetShopItemListRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.search.api.getShopItemList";
    private static final long serialVersionUID = 6966879569286042791L;
    private String catId;
    private String currentPage;
    private String endPrice;
    private String newDays;
    private String pageSize;
    private String q;
    private String shopId;
    private String sort;
    private String startPrice;
    private String uid;

    public GetShopItemListRequest(String uid2, String shopId2, String sort2, String newDays2, String catId2, String q2, String startPrice2, String endPrice2, String pageSize2, String currentPage2) {
        this.uid = uid2;
        this.shopId = shopId2;
        this.sort = sort2;
        this.newDays = newDays2;
        this.catId = catId2;
        this.q = q2;
        this.startPrice = startPrice2;
        this.endPrice = endPrice2;
        this.pageSize = pageSize2;
        this.currentPage = currentPage2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> obj = new HashMap<>();
        if (!TextUtils.isEmpty(this.uid)) {
            obj.put(XStateConstants.KEY_UID, this.uid);
        }
        if (!TextUtils.isEmpty(this.shopId)) {
            obj.put("shopId", this.shopId);
        }
        if (!TextUtils.isEmpty(this.sort)) {
            obj.put("sort", this.sort);
        }
        if (!TextUtils.isEmpty(this.newDays)) {
            obj.put("newDays", this.newDays);
        }
        if (!TextUtils.isEmpty(this.catId)) {
            obj.put("catId", this.catId);
        }
        if (!TextUtils.isEmpty(this.q)) {
            obj.put("q", this.q);
        }
        if (!TextUtils.isEmpty(this.startPrice)) {
            obj.put("startPrice", this.startPrice);
        }
        if (!TextUtils.isEmpty(this.endPrice)) {
            obj.put("endPrice", this.endPrice);
        }
        if (!TextUtils.isEmpty(this.pageSize)) {
            obj.put("pageSize", this.pageSize);
        }
        if (!TextUtils.isEmpty(this.currentPage)) {
            obj.put("currentPage", this.currentPage);
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public GoodsList resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.toString())) {
            return (GoodsList) JSON.parseObject(obj.toString(), GoodsList.class);
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
