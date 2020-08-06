package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ShopDetailData;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class ShopDetailDataRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.waimai.giraffe.queryShopDetail";
    private String version = "2.0";

    public ShopDetailDataRequest(String shopId, String serviceId, String longitude, String latitude, String extFeature, String pageNo, String genreIds) {
        if (!TextUtils.isEmpty(shopId)) {
            addParams("storeId", shopId);
        }
        if (!TextUtils.isEmpty(serviceId)) {
            addParams(BaseConfig.INTENT_KEY_SERVIECID, serviceId);
        }
        if (!TextUtils.isEmpty(extFeature)) {
            addParams("extFeature", extFeature);
        }
        if (!TextUtils.isEmpty(latitude)) {
            addParams(ClientTraceData.b.d, latitude);
        }
        if (!TextUtils.isEmpty(longitude)) {
            addParams(ClientTraceData.b.f54c, longitude);
        }
        if (!TextUtils.isEmpty(pageNo)) {
            addParams("pageNo", pageNo);
        }
        if (!TextUtils.isEmpty(genreIds)) {
            addParams("genreIds", genreIds);
        }
        addParams("pageSize", "200");
        ZpLogger.e("ShopDetailDataRequest", "storeId+" + shopId + "+serviceId+" + serviceId + "+extFeature+" + extFeature + "+latitude+" + latitude + "+longitude+" + longitude + "+pageNo+" + pageNo + "+genreIds+" + genreIds);
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
    public ShopDetailData resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (ShopDetailData) JSON.parseObject(obj.toString(), ShopDetailData.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
