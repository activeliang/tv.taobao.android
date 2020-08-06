package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class AddFollowRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.social.follow.weitao.add";
    private String VERSION = "3.2";

    public AddFollowRequest(String followedId) {
        if (!TextUtils.isEmpty(followedId)) {
            addParams("followedId", followedId);
        }
        addParams("originBiz", "tvtaobao");
        addParams("type", "1");
    }

    /* access modifiers changed from: protected */
    public AddFollowRequest resolveResponse(JSONObject jsonObject) throws Exception {
        try {
            return (AddFollowRequest) JSON.parseObject(jsonObject.toString(), AddFollowRequest.class);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return this.VERSION;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
