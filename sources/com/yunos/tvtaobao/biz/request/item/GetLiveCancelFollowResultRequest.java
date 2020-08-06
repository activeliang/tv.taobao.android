package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveCancelFollowResultRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.weitao.follow.remove";

    public GetLiveCancelFollowResultRequest(String followedId) {
        addParams("followedId", followedId);
        addParams("type", "1");
        addParams("originBiz", "taobaozhibo");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return obj.toString();
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.2";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }
}
