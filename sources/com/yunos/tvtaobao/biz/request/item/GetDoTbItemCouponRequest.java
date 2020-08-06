package com.yunos.tvtaobao.biz.request.item;

import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetDoTbItemCouponRequest extends BaseMtopRequest {
    private static final String API = "tvactivity.bonus.spend.taobaoItem";
    private static final String KEY_ITEM_ID_PARAMS = "itemId";
    private static final String KEY_SID_PARAMS = "sid";
    private static final String KEY_UUID_PARAMS = "uuid";
    private static final long serialVersionUID = 8102251196032160768L;
    private String mItemId = null;
    private String mSid = null;
    private String mUUID = null;

    public GetDoTbItemCouponRequest(String itemId) {
        this.mItemId = itemId;
        this.mSid = User.getSessionId();
        this.mUUID = CloudUUIDWrapper.getCloudUUID();
        addParams("itemId", this.mItemId);
        addParams("sid", this.mSid);
        addParams(KEY_UUID_PARAMS, this.mUUID);
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

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        return obj.toString();
    }
}
