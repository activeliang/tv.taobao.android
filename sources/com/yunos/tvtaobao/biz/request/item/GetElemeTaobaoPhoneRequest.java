package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ElemeOauthBo;
import java.util.Map;
import org.json.JSONObject;

public class GetElemeTaobaoPhoneRequest extends BaseMtopRequest {
    private final String TAG = "mtop.alibaba.ucc.bind.register.element.get";
    private final String VERSION = "1.0";

    public GetElemeTaobaoPhoneRequest(String requestToken) {
        addParams("requestToken", requestToken);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.alibaba.ucc.bind.register.element.get";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public ElemeOauthBo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (ElemeOauthBo) JSON.parseObject(obj.toString(), ElemeOauthBo.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
