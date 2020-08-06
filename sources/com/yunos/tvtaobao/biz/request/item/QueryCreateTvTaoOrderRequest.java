package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultVO_v6;
import java.util.Map;
import org.json.JSONObject;

public class QueryCreateTvTaoOrderRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.tvtaoorderapiservice.createtvtaoorder";
    private String TAG;
    private String nickname;
    private String stbId;

    public QueryCreateTvTaoOrderRequest(String itemNumId, String orderId, String cartFlag, String channel, String versionName, String extParams) {
        this.TAG = "QueryCreateTvTaoOrderRequest";
        this.stbId = "XXX";
        this.nickname = "";
        this.stbId = DeviceUtil.getStbID();
        if (!TextUtils.isEmpty(itemNumId)) {
            addParams("itemNumId", itemNumId);
        }
        addParams("bizOrderId", orderId);
        if ("cart".equals(cartFlag)) {
            addParams("cartFlag", "1");
        } else {
            addParams("cartFlag", "0");
        }
        addParams("from", cartFlag);
        addParams("deviceId", this.stbId);
        addParams("appkey", channel);
        addParams("versionName", versionName);
        addParams("extParams", extParams);
    }

    /* access modifiers changed from: protected */
    public TBDetailResultVO_v6 resolveResponse(JSONObject obj) throws Exception {
        return null;
    }

    /* access modifiers changed from: protected */
    public String getHttpParams() {
        return null;
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
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }
}
