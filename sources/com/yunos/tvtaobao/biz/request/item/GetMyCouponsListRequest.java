package com.yunos.tvtaobao.biz.request.item;

import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.MyCouponsList;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetMyCouponsListRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 1990846608850777395L;
    private String API = "mtop.wallet.coupon.getmycouponlistbytype";
    private String v = DispatchConstants.VER_CODE;

    public GetMyCouponsListRequest(String bizType, String couponType) {
        addParams("bizType", bizType);
        addParams("couponType", couponType);
        addParams(BaseConfig.INTENT_KEY_SOURCE, "0");
        ZpLogger.e(getClass().getSimpleName(), "bizType===" + bizType + "===couponType====" + couponType);
    }

    /* access modifiers changed from: protected */
    public MyCouponsList resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.e(getClass().getSimpleName(), "obj===" + obj.toString());
        if (obj == null) {
            return null;
        }
        return (MyCouponsList) JSON.parseObject(obj.toString(), MyCouponsList.class);
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
        return this.v;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
