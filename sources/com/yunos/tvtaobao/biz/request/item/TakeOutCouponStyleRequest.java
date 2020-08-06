package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutCouponStyleRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.takeoutcouponstyle.getpageinfo";
    private String version = "1.0";

    public TakeOutCouponStyleRequest() {
        addParams("appkey", Config.getChannel());
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
    public TakeOutCouponStyle resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("TakeOutCouponStyleRequest", "response = " + obj.toString());
        return (TakeOutCouponStyle) JSON.parseObject(obj.toString(), TakeOutCouponStyle.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
