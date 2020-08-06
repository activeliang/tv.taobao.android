package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CouponRecommendList;
import java.util.Map;
import org.json.JSONObject;

public class GetCouponRecommendList extends BaseMtopRequest {
    private static final long serialVersionUID = -5119194448917610828L;
    private String API = "mtop.wallet.coupon.getRecommendList";
    private String version = "1.0";

    public GetCouponRecommendList(String sellerId, String couponId) {
        addParams("sellerId", sellerId);
        addParams("couponId", couponId);
        addParams("couponType", "1");
        addParams("bizType", "1");
        addParams(BaseConfig.INTENT_KEY_SOURCE, "2");
    }

    /* access modifiers changed from: protected */
    public CouponRecommendList resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (CouponRecommendList) JSON.parseObject(obj.toString(), CouponRecommendList.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
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
}
