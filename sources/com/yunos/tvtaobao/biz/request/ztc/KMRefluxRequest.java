package com.yunos.tvtaobao.biz.request.ztc;

import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class KMRefluxRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.kmRefluxService.reflux";
    private static final String VERSION = "1.0";

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    public KMRefluxRequest(String itemId, String title, String picUrl) {
        JSONObject refluxParams = new JSONObject();
        try {
            refluxParams.put("appkey", Config.getChannel());
            refluxParams.put("business", "tvtaobao");
            refluxParams.put(TuwenConstants.PARAMS.PIC_URL, TextUtils.isEmpty(picUrl) ? "" : picUrl);
            refluxParams.put("title", TextUtils.isEmpty(title) ? "" : title);
            refluxParams.put("umtoken", Config.getUmtoken(CoreApplication.getApplication()));
            refluxParams.put("version", Config.getVersionName(CoreApplication.getApplication()));
            addParams("refluxParams", refluxParams.toString());
            addParams("itemId", itemId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
