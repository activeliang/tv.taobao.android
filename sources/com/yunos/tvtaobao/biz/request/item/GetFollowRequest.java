package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.FollowData;
import java.util.Map;
import org.json.JSONObject;

public class GetFollowRequest extends BaseMtopRequest {
    public GetFollowRequest(boolean isShop, boolean isDaRen, String cursor) {
        addParams("isShop", "" + isShop);
        addParams("isDaRen", "" + isDaRen);
        if (!TextUtils.isEmpty(cursor)) {
            addParams("cursor", "" + cursor);
        }
    }

    /* access modifiers changed from: protected */
    public FollowData resolveResponse(JSONObject jsonObject) throws Exception {
        try {
            return (FollowData) JSON.parseObject(jsonObject.toString(), FollowData.class);
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
        return "mtop.taobao.cmin.mypath";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
