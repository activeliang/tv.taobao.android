package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.sdk.android.oss.common.OSSHeaders;
import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;
import java.util.TreeMap;

public class TakeOutGetGaodeDistanceRequest extends BaseHttpRequest {
    private String appKey = "aee2300ce283a15d48d6e484bce6ab9a";
    private String destination;
    private String origin;

    public TakeOutGetGaodeDistanceRequest(String org2, String dest) {
        this.origin = org2;
        this.destination = dest;
    }

    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new TreeMap<>();
        params.put("key", this.appKey);
        params.put(OSSHeaders.ORIGIN, this.origin);
        params.put("destination", this.destination);
        return params;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return "http://restapi.amap.com/v4/direction/bicycling";
    }
}
