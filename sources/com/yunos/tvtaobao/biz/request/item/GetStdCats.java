package com.yunos.tvtaobao.biz.request.item;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetStdCats extends BaseMtopRequest {
    private static final String API = "mtop.taobao.iforest.getstdcats";
    private static final long serialVersionUID = 6403615798555209809L;

    public GetStdCats(String categoryId) {
        addParams("cat_ids", categoryId);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return obj.optJSONArray("result").getJSONObject(0).getJSONArray(TuwenConstants.PARAMS.SKU_PATH).getJSONObject(0).optString("catId");
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
}
