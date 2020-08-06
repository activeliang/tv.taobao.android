package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class CancelCollectionRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.mercury.delCollects";
    private static final long serialVersionUID = 8352342586453408484L;

    public CancelCollectionRequest(String itemId) {
        JSONArray array = new JSONArray();
        array.put(itemId);
        addParams("itemIds", array.toString());
        addParams("appName", "detail");
        addParams("favType", "1");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.d("CheckFavRequest", obj + "-------------");
        if (!obj.isNull("result")) {
            return obj.getString("result");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
