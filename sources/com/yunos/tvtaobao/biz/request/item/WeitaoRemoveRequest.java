package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class WeitaoRemoveRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.weitao.follow.remove";
    private String mOriginBiz = "tvtaobao";
    private String mOriginFlag;
    private String mOriginPage;
    private String mPubAccountId;

    public WeitaoRemoveRequest(String pubAccountId, String originPage, String originFlag) {
        this.mPubAccountId = pubAccountId;
        this.mOriginPage = originPage;
        this.mOriginFlag = originFlag;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.1";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(this.mPubAccountId)) {
            params.put("pubAccountId", this.mPubAccountId);
        }
        if (!TextUtils.isEmpty(this.mOriginBiz)) {
            params.put("originBiz", this.mOriginBiz);
        }
        params.put("originPage", this.mOriginPage);
        params.put("originFlag", this.mOriginFlag);
        return params;
    }
}
