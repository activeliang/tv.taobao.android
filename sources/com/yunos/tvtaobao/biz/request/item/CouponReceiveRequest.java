package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CouponReceiveParamsBean;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class CouponReceiveRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.uc.behavior.couponReceive";
    private static final long serialVersionUID = -534597323930871635L;
    private String apiVersion = "1.0";

    public CouponReceiveRequest(CouponReceiveParamsBean couponReceiveParamsBean) {
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
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCouponId())) {
            addParams("couponId", couponReceiveParamsBean.getCouponId());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCouponRule())) {
            addParams("couponRule", couponReceiveParamsBean.getCouponRule());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCouponAmount())) {
            addParams("couponAmount", couponReceiveParamsBean.getCouponAmount());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCouponValidityStart())) {
            addParams("couponValidityStart", couponReceiveParamsBean.getCouponValidityStart());
        }
        if (!TextUtils.isEmpty(couponReceiveParamsBean.getCouponValidityEnd())) {
            addParams("couponValidityEnd", couponReceiveParamsBean.getCouponValidityEnd());
        }
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

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
