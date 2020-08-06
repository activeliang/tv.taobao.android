package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.YGAcrVideoItem;
import com.yunos.tvtaobao.biz.request.core.JsonResolver;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetYGVideoItemsRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvbuyapiservice.getepisodescheduleinfos";
    private String VERSION = "1.0";

    public GetYGVideoItemsRequest(String episodeId) {
        addParams("episodeId", episodeId);
    }

    /* access modifiers changed from: protected */
    public List<YGAcrVideoItem> resolveResponse(JSONObject obj) throws Exception {
        return JsonResolver.resolveYGAcrVideoItem(obj);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
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
}
