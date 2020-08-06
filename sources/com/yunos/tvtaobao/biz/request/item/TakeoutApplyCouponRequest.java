package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeoutApplyCoupon;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TakeoutApplyCouponRequest extends BaseMtopRequest {
    private String API = "mtop.life.marketing.applyCoupon";
    private String version = "1.0";

    public TakeoutApplyCouponRequest(String asac, String channel) {
        addParams("type", "common");
        addParams("channel", channel);
        addParams("autoBindMobile", "true");
        addParams("asac", asac);
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

    /* access modifiers changed from: protected */
    public TakeoutApplyCoupon resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("TakeoutApplyCouponRequest", "response = " + obj.toString());
        return (TakeoutApplyCoupon) JSON.parseObject(obj.toString(), TakeoutApplyCoupon.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
