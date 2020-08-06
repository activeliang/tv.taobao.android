package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderRebateBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetBuildOrderRebateMoneyRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tvTaoCouponService.getTradeCoupon";
    private static final String TAG = "GetBuildOrderRebateMoneyRequest";

    public GetBuildOrderRebateMoneyRequest(String paramsStr, String extParams) {
        addParams("paramsStr", paramsStr);
        addParams("extParams", extParams);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public List<BuildOrderRebateBo> resolveResponse(JSONObject obj) throws Exception {
        if (obj.isNull("result")) {
            return null;
        }
        ZpLogger.v(TAG, obj.toString());
        return JSON.parseArray(obj.getString("result"), BuildOrderRebateBo.class);
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
