package com.tvtaobao.voicesdk.request;

import com.tvtaobao.voicesdk.bean.AlipayAuthResult;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class AlipayAuthQueryRequest extends BaseMtopRequest {
    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.alipay.authquery";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("deviceId", CloudUUIDWrapper.getCloudUUID());
        data.put("tbUserId", User.getUserId());
        data.put("ver", "2.0");
        data.put("autoGenQRCode", "false");
        return data;
    }

    /* access modifiers changed from: protected */
    public AlipayAuthResult resolveResponse(JSONObject obj) throws Exception {
        return AlipayAuthResult.resolveFromJson(obj);
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
