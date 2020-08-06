package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class ScanBindRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.user.bind";
    private String version = "1.0";

    public ScanBindRequest(String uuid, String deviceId, String appKey) {
        addParams("uuid", uuid);
        addParams("deviceId", deviceId);
        addParams("appKey", appKey);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public <T> T resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.v(this.TAG, this.TAG + ".ScanBindRequest --> resolveResponse = " + obj);
        return null;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
