package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CollectionsInfo;
import java.util.Map;
import org.json.JSONObject;

public class GetNewCollectsRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.mercury.platform.collections.get";
    private static final long serialVersionUID = 1132143426750772778L;
    private int pageSize = 10;
    private String startTime = "0";

    public GetNewCollectsRequest(String startTime2, int size) {
        this.startTime = startTime2;
        this.pageSize = size;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("itemType", "1");
        addParams("platformCode", "0");
        addParams("appName", "favorite");
        addParams("startTime", this.startTime);
        addParams("pageSize", String.valueOf(this.pageSize));
        return null;
    }

    /* access modifiers changed from: protected */
    public CollectionsInfo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (CollectionsInfo) JSON.parseObject(obj.toString(), CollectionsInfo.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
