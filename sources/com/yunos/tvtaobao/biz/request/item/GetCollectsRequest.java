package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CollectList;
import java.util.Map;
import org.json.JSONObject;

public class GetCollectsRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.mcl.fav.queryColGoods";
    private static final long serialVersionUID = 1132143426750772778L;
    private int currentPage = 1;
    private int pageSize = 10;

    public GetCollectsRequest(int page, int size) {
        this.currentPage = page;
        this.pageSize = size;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("pageSize", String.valueOf(this.pageSize));
        addParams("currentPage", String.valueOf(this.currentPage));
        addParams("method", "queryColGood");
        return null;
    }

    /* access modifiers changed from: protected */
    public CollectList resolveResponse(JSONObject obj) throws Exception {
        JSONObject result;
        if (obj == null || (result = obj.optJSONObject("result")) == null) {
            return null;
        }
        return (CollectList) JSON.parseObject(result.toString(), CollectList.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
