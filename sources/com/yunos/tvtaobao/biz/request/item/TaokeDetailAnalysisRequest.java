package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TaokeDetailAnalysisRequest extends BaseMtopRequest {
    private String api = "mtop.taobao.tvtao.taokeservice.info";

    public TaokeDetailAnalysisRequest(String stbId, String nickname, String tid, String sourceType, String sellerId, String bizSource, String referer, String action) {
        addParams("login", stbId);
        addParams("name", nickname);
        addParams(BaseConfig.INTENT_KEY_TID, tid);
        addParams("source_type", sourceType);
        addParams("sellerId", sellerId);
        if (bizSource != null) {
            addParams("bizSource", bizSource);
        }
        if (referer != null) {
            addParams(RequestParameters.SUBRESOURCE_REFERER, referer);
        }
        if (action != null) {
            addParams("action", action);
        }
        ZpLogger.v(this.TAG, this.TAG + ".TaokeDetailAnalysisRequest --> sellerId = " + sellerId);
        ZpLogger.v(this.TAG, this.TAG + ".TaokeDetailAnalysisRequest --> tid = " + tid);
        ZpLogger.v(this.TAG, this.TAG + ".TaokeDetailAnalysisRequest --> bizSource = " + bizSource);
    }

    /* access modifiers changed from: protected */
    public <T> T resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.v(this.TAG, this.TAG + ".TaokeDetailAnalysisRequest --> resolveResponse = " + obj);
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
