package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.MyFollowBean;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import org.json.JSONObject;

public class GetMyFollowRequest extends BaseMtopRequest {
    private static final String API = "mtop.taobao.social.follow.weitao.third.list";
    private static final String VERSION = "1.0";

    public GetMyFollowRequest(String isShop, String isDaRen, String cursor, String pageSize) {
        addParams("isShop", isShop);
        addParams("isDaRen", isDaRen);
        addParams("pageSize", pageSize);
        if (!TextUtils.isEmpty(cursor)) {
            addParams("cursor", cursor);
        }
    }

    /* access modifiers changed from: protected */
    public MyFollowBean resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (MyFollowBean) JSON.parseObject(obj.toString(), MyFollowBean.class);
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
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
