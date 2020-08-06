package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class TaokeLoginAnalysisRequest extends BaseMtopRequest {
    private String api;
    private String stbId;

    public TaokeLoginAnalysisRequest(String nickname, String bizSource, String referer) {
        this.api = "mtop.taobao.tvtao.taokeservice.login";
        this.stbId = "XXX";
        this.stbId = DeviceUtil.getStbID();
        if (this.stbId != null) {
            addParams("login", this.stbId);
        }
        if (!TextUtils.isEmpty(nickname)) {
            addParams("name", nickname);
        }
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
