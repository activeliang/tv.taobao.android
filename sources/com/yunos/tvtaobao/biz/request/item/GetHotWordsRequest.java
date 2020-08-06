package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetHotWordsRequest extends BaseMtopRequest {
    private static String API = "com.yunos.tv.tao.itemService.getHotWords";
    private static final long serialVersionUID = -7150582517538460125L;
    private String version = "1.0";

    public GetHotWordsRequest(String type) {
        addParams("uuid", CloudUUIDWrapper.getCloudUUID());
        addParams("key", "q");
        addParams("type", type);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public List<String> resolveResponse(JSONObject obj) throws Exception {
        if (obj == null || TextUtils.isEmpty(obj.toString()) || TextUtils.isEmpty(obj.optString("result"))) {
            return null;
        }
        return JSON.parseArray(obj.optString("result"), String.class);
    }
}
