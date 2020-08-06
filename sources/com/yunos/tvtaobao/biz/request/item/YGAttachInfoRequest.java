package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.YGAttachInfo;
import java.util.Map;
import org.json.JSONObject;

public class YGAttachInfoRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.tvtaoliveservice.getattachinfo";
    private final String VERSION = "1.0";

    public YGAttachInfoRequest(String liveId) {
        addParams("liveId", liveId);
    }

    /* access modifiers changed from: protected */
    public YGAttachInfo resolveResponse(JSONObject obj) throws Exception {
        if (obj != null) {
            return YGAttachInfo.fromMTOP(new JSONObject(obj.getString("result")));
        }
        return null;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.tvtaoliveservice.getattachinfo";
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
