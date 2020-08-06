package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class WeitaoSetDynamic extends BaseMtopRequest {
    private String API = "mtop.cybertron.follow.setDynamic";
    private String mPubAccountId;
    private boolean mStatus;

    public WeitaoSetDynamic(String pubAccountId, boolean status) {
        this.mPubAccountId = pubAccountId;
        this.mStatus = status;
        if (!TextUtils.isEmpty(this.mPubAccountId)) {
            addParams("pubAccountId", this.mPubAccountId);
        }
        addParams("status", this.mStatus + "");
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.e(this.TAG, " obj = " + obj.toString());
        return obj.toString();
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return this.API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
