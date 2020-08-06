package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.JhsItemDetail;
import java.util.Map;
import org.json.JSONObject;

public class GetJhsItemDetailRequest extends BaseMtopRequest {
    private static final String API = "mtop.ju.detail.getByJuId";
    private static final long serialVersionUID = -8531841827806200048L;
    private Long juId;

    public GetJhsItemDetailRequest(Long juId2) {
        this.juId = juId2;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("juId", String.valueOf(this.juId));
        return null;
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

    /* access modifiers changed from: protected */
    public JhsItemDetail resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (JhsItemDetail) JSON.parseObject(obj.toString(), JhsItemDetail.class);
    }
}
