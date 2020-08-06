package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBag;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutGetBagRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.waimai.giraffe.singleshoppingcart.get";
    private String version = "1.0";

    public TakeOutGetBagRequest(String storeId, String longitude, String latitude, String extFeature) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
        if (!TextUtils.isEmpty(longitude)) {
            addParams(ClientTraceData.b.f54c, longitude);
        }
        if (!TextUtils.isEmpty(latitude)) {
            addParams(ClientTraceData.b.d, latitude);
        }
        if (!TextUtils.isEmpty(extFeature)) {
            addParams("extFeature", extFeature);
        }
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
    public TakeOutBag resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e("TakeOutGetBagRequest", "response = " + obj.toString());
        return (TakeOutBag) JSON.parseObject(obj.toString(), TakeOutBag.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
