package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TBaoShopBean;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveHotItemListRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvtaoliveservice.gettaobaolivedetailitemlist";
    private String version = "1.0";

    public GetLiveHotItemListRequest(String type, String liveId, String creatorId) {
        addParams("type", type);
        if (liveId != null) {
            addParams("liveId", liveId);
        }
        if (creatorId != null) {
            addParams("creatorId", creatorId);
        }
    }

    /* access modifiers changed from: protected */
    public TBaoShopBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (TBaoShopBean) JSON.parseObject(obj.toString(), TBaoShopBean.class);
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
        return this.version;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
