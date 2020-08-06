package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.LiveBonusTimeResult;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveBonusTimeRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.welafretime.getTimeList";

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public LiveBonusTimeResult resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return (LiveBonusTimeResult) JSON.parseObject(obj.toString(), LiveBonusTimeResult.class);
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
