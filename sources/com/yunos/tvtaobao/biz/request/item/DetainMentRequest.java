package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.fastjson.JSON;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class DetainMentRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 7592070242650783051L;
    private String API = McartConstants.RECOMMEND_API_NAME;
    private String user_Id = "";
    private String version = "2.0";

    public DetainMentRequest(String user_Id2) {
        this.user_Id = user_Id2;
        addParams("appId", "987");
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(user_Id2)) {
            params.put("user_Id", user_Id2);
        }
        params.put("callSource", "tvtaobao");
        params.put("platform", DispatchConstants.ANDROID);
        params.put("channel", Config.getTTid());
        params.put("pageSize", "200");
        String param = JSON.toJSONString(params);
        ZpLogger.d("DetainMentRequest", param);
        addParams("params", param);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
