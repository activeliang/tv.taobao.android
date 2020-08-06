package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.LoadingBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetAdvertsRequest extends BaseMtopRequest {
    private static final String API = "com.yunos.tv.tao.itemService.getAdverts";
    private static final long serialVersionUID = -534597323930871635L;
    private String apiVersion = "1.0";

    public GetAdvertsRequest(String uuid, String loadVersion, String extParams) {
        addParams("uuid", uuid);
        addParams("loadVersion", loadVersion);
        addParams("extParams", extParams);
        ZpLogger.e(this.TAG, "uuid = " + uuid + " loadVersion = " + loadVersion + "  extParams = " + extParams);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("uuid", CloudUUIDWrapper.getCloudUUID());
        return null;
    }

    /* access modifiers changed from: protected */
    public List<LoadingBo> resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.optString("result"))) {
            return JSON.parseArray(obj.optString("result"), LoadingBo.class);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.apiVersion;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
