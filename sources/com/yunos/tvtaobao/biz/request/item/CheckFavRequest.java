package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class CheckFavRequest extends BaseMtopRequest {
    private static final String API = "mtop.favorite.checkUserCollect";
    private static final String TYPE_AUCTION = "1";
    private static final long serialVersionUID = 8352342586453408484L;
    private String itemId = null;

    public CheckFavRequest(String itemId2) {
        this.itemId = itemId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("sid", User.getSessionId());
        addParams("itemId", this.itemId);
        addParams("type", "1");
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.d("CheckFavRequest", obj + "-------------");
        if (!obj.isNull("isCollect")) {
            return obj.getString("isCollect");
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
