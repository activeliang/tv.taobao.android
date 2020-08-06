package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutBagAgain;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutAgainRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.waimai.order.again.get";
    private String version = "1.0";

    public TakeOutAgainRequest(String storeId, String orderItems) {
        if (!TextUtils.isEmpty(storeId)) {
            addParams("storeId", storeId);
        }
        if (!TextUtils.isEmpty(orderItems)) {
            addParams("orderItems", orderItems);
        }
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

    /* access modifiers changed from: protected */
    public TakeOutBagAgain resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (TakeOutBagAgain) JSON.parseObject(obj.toString(), TakeOutBagAgain.class);
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
