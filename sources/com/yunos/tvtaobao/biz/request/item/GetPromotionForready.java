package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.PromotionBean;
import java.util.Map;
import org.json.JSONObject;

public class GetPromotionForready extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.promotionservice.getpromotionforready";
    private String version = "1.0";

    public GetPromotionForready() {
        addParams(BaseConfig.INTENT_KEY_SOURCE, "ali");
    }

    /* access modifiers changed from: protected */
    public PromotionBean resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.toString())) {
            return (PromotionBean) JSON.parseObject(new JSONObject(obj.getString("result")).toString(), PromotionBean.class);
        }
        return null;
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
