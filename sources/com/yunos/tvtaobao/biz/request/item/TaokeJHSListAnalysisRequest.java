package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TaokeJHSListAnalysisRequest extends BaseMtopRequest {
    private String api = "mtop.taobao.tvtao.taokeservice.btoc";
    private String nickname = "";
    private String stbId = "";

    public TaokeJHSListAnalysisRequest(String stbId2, String nickname2, String bizSource, String referer) {
        addParams("login", stbId2);
        addParams("name", nickname2);
        if (bizSource != null) {
            addParams("bizSource", bizSource);
        }
        if (referer != null) {
            addParams(RequestParameters.SUBRESOURCE_REFERER, referer);
        }
    }

    /* access modifiers changed from: protected */
    public <T> T resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.v(this.TAG, this.TAG + ".requestGetItemDetail_v6 --> resolveResponse = " + obj);
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.api;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
