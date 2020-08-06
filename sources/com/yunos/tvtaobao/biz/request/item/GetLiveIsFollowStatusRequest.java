package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.LiveIsFollowStatus;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveIsFollowStatusRequest extends BaseMtopRequest {
    private static final String API = "mtop.cybertron.follow.detail";

    public GetLiveIsFollowStatusRequest(String followedId) {
        addParams("type", "1");
        if (!TextUtils.isEmpty(followedId)) {
            addParams("followedId", followedId);
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public LiveIsFollowStatus resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ZpLogger.d(this.TAG, obj.toString());
        return (LiveIsFollowStatus) JSON.parseObject(obj.toString(), LiveIsFollowStatus.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.0";
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return true;
    }
}
