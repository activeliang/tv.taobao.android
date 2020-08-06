package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class RemoveFollowRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.weitao.follow.remove";
    private String VERSION = "3.2";

    public RemoveFollowRequest(String followedId) {
        if (!TextUtils.isEmpty(followedId)) {
            addParams("followedId", followedId);
        }
        addParams("originBiz", "tvtaobao");
        addParams("type", "1");
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject jsonObject) throws Exception {
        if (jsonObject.has("toastMsg")) {
            return jsonObject.optString("toastMsg");
        }
        return "";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.VERSION;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
