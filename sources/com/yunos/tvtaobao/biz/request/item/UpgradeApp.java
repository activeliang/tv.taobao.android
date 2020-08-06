package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class UpgradeApp extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tvtaoappservice.upgradev2";

    public UpgradeApp(String version, String uuid, String channelId, String code, String versionCode, String versionName, String systemInfo, String umtoken, String modelInfo, String extParams) {
        addParams("versionCode", versionCode);
        addParams("code", code);
        addParams("versionName", versionName);
        addParams("uuid", uuid);
        addParams("channelId", channelId);
        addParams("systemInfo", systemInfo);
        addParams("version", version);
        addParams("umToken", umtoken);
        addParams("modelInfo", modelInfo);
        addParams("extParams", extParams);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.d(this.TAG, "obj " + obj.toString());
        if (obj != null) {
            return obj.toString();
        }
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
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
