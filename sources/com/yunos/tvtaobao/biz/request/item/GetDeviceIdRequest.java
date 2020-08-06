package com.yunos.tvtaobao.biz.request.item;

import anet.channel.strategy.dispatch.DispatchConstants;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetDeviceIdRequest extends BaseMtopRequest {
    private static final String API = "mtop.sys.newDeviceId";
    private static final long serialVersionUID = 4546635303661975952L;
    private String apiVersion = DispatchConstants.VER_CODE;

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("device_global_id", CloudUUIDWrapper.getCloudUUID());
        addParams("new_device", "false");
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

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (!obj.isNull("device_id")) {
            return obj.getString("device_id");
        }
        return "";
    }
}
