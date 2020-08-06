package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.CartStyleBean;
import java.util.Map;
import org.json.JSONObject;

public class GetCartStyleRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.tvtao.cartstyle.getschedule";

    public GetCartStyleRequest() {
        addParams("appKey", Config.getChannel());
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
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
    public CartStyleBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (CartStyleBean) JSON.parseObject(obj.toString(), CartStyleBean.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
