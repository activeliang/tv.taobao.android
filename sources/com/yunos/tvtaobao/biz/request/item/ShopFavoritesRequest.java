package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CouponReceiveParamsBean;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShopFavoritesRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.uc.behavior.shopFavorites";
    private String apiVersion = "1.0";

    public ShopFavoritesRequest(CouponReceiveParamsBean couponReceiveParamsBean) {
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getPage())) {
            addParams("page", couponReceiveParamsBean.getPage());
        }
        List<String> referer = couponReceiveParamsBean.getReferer();
        if (referer != null && referer.size() > 0) {
            JSONArray arrayReferer = new JSONArray();
            for (int i = 0; i < referer.size(); i++) {
                arrayReferer.put(referer.get(i));
            }
            addParams(RequestParameters.SUBRESOURCE_REFERER, arrayReferer.toString());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getProvince())) {
            addParams("province", couponReceiveParamsBean.getProvince());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCity())) {
            addParams("city", couponReceiveParamsBean.getCity());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getSellerId())) {
            addParams("sellerId", couponReceiveParamsBean.getSellerId());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getShopId())) {
            addParams("shopId", couponReceiveParamsBean.getShopId());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getItemId())) {
            addParams("itemId", couponReceiveParamsBean.getItemId());
        }
        if (couponReceiveParamsBean.getItemNum() > 0) {
            addParams("itemNum", couponReceiveParamsBean.getItemNum() + "");
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getItemSku())) {
            addParams("itemSku", couponReceiveParamsBean.getItemSku());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getBehaviorType())) {
            addParams("behaviorType", couponReceiveParamsBean.getBehaviorType());
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.optString("result"))) {
            return obj;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.apiVersion;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
