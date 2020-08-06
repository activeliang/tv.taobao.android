package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetTaobaoPointSign extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tvtaopointservice.checkInPoints";
    private static final long serialVersionUID = 6403615798555209809L;

    public GetTaobaoPointSign() {
        addParams("appkey", Config.getAppKey());
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
        if (obj.has("points")) {
            return obj.getString("points");
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
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
