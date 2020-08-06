package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ElemBindBo;
import java.util.Map;
import org.json.JSONObject;

public class GetByTaobaoUserIdRequest extends BaseMtopRequest {
    private final String TAG = "mtop.taobao.tvtao.eleme.user.getByTaobaoUserId";
    private final String VERSION = "1.0";

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.eleme.user.getByTaobaoUserId";
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
    public ElemBindBo resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (ElemBindBo) JSON.parseObject(obj.toString(), ElemBindBo.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
