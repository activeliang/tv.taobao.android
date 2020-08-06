package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.WeitaoFollowBean;
import java.util.Map;
import org.json.JSONObject;

public class WeitaoFollowRequest extends BaseMtopRequest {
    private String API = "mtop.cybertron.follow.detail";
    private String mAccountType;
    private String mPubAccountId;

    public WeitaoFollowRequest(String accountType, String pubAccountId) {
        this.mAccountType = accountType;
        this.mPubAccountId = pubAccountId;
        if (!TextUtils.isEmpty(this.mAccountType)) {
            addParams("accountType", this.mAccountType);
        }
        if (!TextUtils.isEmpty(this.mPubAccountId)) {
            addParams("pubAccountId", this.mPubAccountId);
        }
    }

    /* access modifiers changed from: protected */
    public WeitaoFollowBean resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.toString())) {
            return (WeitaoFollowBean) JSON.parseObject(obj.toString(), WeitaoFollowBean.class);
        }
        return null;
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
