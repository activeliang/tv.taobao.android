package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBag;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutUpdateBagRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.waimai.giraffe.singleshoppingcart.update";
    private String version = "1.0";

    public TakeOutUpdateBagRequest(String storeId, String latitude, String longitude, String operateType, String paramList) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
        if (!TextUtils.isEmpty(latitude)) {
            addParams(ClientTraceData.b.d, latitude);
        }
        if (!TextUtils.isEmpty(longitude)) {
            addParams(ClientTraceData.b.f54c, longitude);
        }
        if (!TextUtils.isEmpty(operateType)) {
            addParams("operateType", operateType);
        }
        if (!TextUtils.isEmpty(paramList)) {
            addParams("paramList", paramList);
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
        return (TakeOutBag) JSON.parseObject(obj.toString(), TakeOutBag.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
