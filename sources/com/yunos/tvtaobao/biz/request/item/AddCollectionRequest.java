package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class AddCollectionRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.mercury.addcollect";
    private static final long serialVersionUID = 8352342586453408484L;

    public AddCollectionRequest(String itemId) {
        addParams("itemId", itemId);
        addParams("appName", "detail");
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
