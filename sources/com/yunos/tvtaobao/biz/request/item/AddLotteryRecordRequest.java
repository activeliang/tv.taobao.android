package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class AddLotteryRecordRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvtaolotteryservice.addlotterydetailrecord";
    private String version = "1.0";

    public AddLotteryRecordRequest(String amount, String uuid) {
        addParams("amount", amount);
        addParams(BaseConfig.INTENT_KEY_SOURCE, "2");
        addParams("type", "2");
        addParams("uuid", uuid);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
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
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
