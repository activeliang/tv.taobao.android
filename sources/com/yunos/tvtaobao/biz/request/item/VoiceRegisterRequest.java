package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceRegisterRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.speech.nlp.register";
    private final String TAG = "VoiceRegisterRequest";
    private String VERSION = "1.0";

    public VoiceRegisterRequest(String referrer, String className, String type, String params) {
        ZpLogger.d("TVTao_VoiceRegisterRequest", "VoiceRegisterRequest referrer : " + referrer + " ,className : " + className + " ,type : " + type + " ,\n" + "parmas : " + params);
        addParams(CommonData.KEY_CLASS_NAME, className);
        addParams("referrer", referrer);
        addParams("behavior", type);
        addParams("params", params);
        String uuid = CloudUUIDWrapper.getCloudUUID();
        addParams("uuid", TextUtils.isEmpty(uuid) ? "false" : uuid);
        try {
            JSONObject extParams = new JSONObject();
            extParams.put("umToken", Config.getUmtoken(CoreApplication.getApplication()));
            extParams.put("wua", Config.getWua(CoreApplication.getApplication()));
            extParams.put("isSimulator", Config.isSimulator(CoreApplication.getApplication()));
            extParams.put("userAgent", Config.getAndroidSystem(CoreApplication.getApplication()));
            addParams("extParams", extParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.VERSION;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public JSONObject resolveResponse(JSONObject obj) throws Exception {
        return obj;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
