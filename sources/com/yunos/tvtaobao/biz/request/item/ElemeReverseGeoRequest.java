package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseHttpRequest;
import java.util.Map;

public class ElemeReverseGeoRequest extends BaseHttpRequest {
    private String latitude;
    private String longitude;

    public ElemeReverseGeoRequest(String latitude2, String longitude2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
    }

    public String resolveResult(String result) throws Exception {
        return result;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpDomain() {
        return String.format("http://restapi.ele.me/bgs/poi/reverse_geo_coding?latitude=%s&longitude=%s", new Object[]{this.latitude, this.longitude});
    }
}
