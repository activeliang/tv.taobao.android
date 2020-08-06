package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.LiveDetailBean;
import java.util.Map;
import org.json.JSONObject;

public class GetLiveDetailRequest extends BaseMtopRequest {
    private String API = "mtop.mediaplatform.live.livedetail";
    private String version = "1.0";

    public GetLiveDetailRequest(String liveId, String creatorId) {
        if (liveId != null) {
            addParams("liveId", liveId);
        }
        if (creatorId != null) {
            addParams("creatorId", creatorId);
        }
    }

    /* access modifiers changed from: protected */
    public LiveDetailBean resolveResponse(JSONObject obj) throws Exception {
        if (!TextUtils.isEmpty(obj.toString())) {
            return (LiveDetailBean) JSON.parseObject(obj.toString(), LiveDetailBean.class);
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
