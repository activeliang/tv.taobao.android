package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.LiveBonusResult;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveBonusResultRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.lottery.sendbenefit";

    public GetLiveBonusResultRequest(String bizId, String type, String asac) {
        addParams("bizId", bizId);
        addParams("type", type);
        if (!TextUtils.isEmpty(CloudUUIDWrapper.getCloudUUID())) {
            addParams("uuid", CloudUUIDWrapper.getCloudUUID());
        }
        if (!TextUtils.isEmpty(asac)) {
            addParams("asac", asac);
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public LiveBonusResult resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return (LiveBonusResult) JSON.parseObject(obj.toString(), LiveBonusResult.class);
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
