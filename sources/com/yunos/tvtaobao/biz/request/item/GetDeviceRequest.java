package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.DeviceBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetDeviceRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.dicservice.getdevice";
    private String apiVersion = "1.0";

    public GetDeviceRequest(String model) {
        addParams("model", model);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public DeviceBo resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.d(this.TAG, "GetDeviceRequest data " + obj.toString());
        if (obj != null) {
            return (DeviceBo) JSON.parseObject(obj.toString(), DeviceBo.class);
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
