package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class WeitaoAddRequest extends BaseMtopRequest {
    private String API = "mtop.taobao.social.follow.weitao.add";
    private String mAccountType;
    private String mOriginBiz = "tvtaobao";
    private String mOriginFlag;
    private String mOriginPage;
    private String mPubAccountId;

    public WeitaoAddRequest(String accountType, String pubAccountId, String originPage, String originFlag) {
        this.mAccountType = accountType;
        this.mPubAccountId = pubAccountId;
        this.mOriginPage = originPage;
        this.mOriginFlag = originFlag;
        if (!TextUtils.isEmpty(this.mAccountType)) {
            addParams("accountType", this.mAccountType);
        }
        if (!TextUtils.isEmpty(this.mPubAccountId)) {
            addParams("pubAccountId", this.mPubAccountId);
        }
        if (!TextUtils.isEmpty(this.mOriginBiz)) {
            addParams("originBiz", this.mOriginBiz);
        }
        addParams("originPage", this.mOriginPage);
        addParams("originFlag", this.mOriginFlag);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
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
        return "3.1";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
