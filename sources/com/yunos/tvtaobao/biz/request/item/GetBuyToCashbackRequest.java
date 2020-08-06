package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class GetBuyToCashbackRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.tvtao.tvtaosearchservice.getBuyToCashback";
    private String item_ids = null;
    private String version = "1.0";

    public GetBuyToCashbackRequest(String item_ids2) {
        if (!TextUtils.isEmpty(item_ids2)) {
            addParams(BaseConfig.NID_FROM_CART, item_ids2);
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
    public Map<String, String> resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.v(this.TAG, obj.toString());
        return (Map) JSON.parseObject(obj.toString(), new HashMap<>().getClass());
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
