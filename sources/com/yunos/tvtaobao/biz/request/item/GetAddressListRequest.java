package com.yunos.tvtaobao.biz.request.item;

import android.content.Context;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.Address;
import com.yunos.tvtaobao.biz.request.core.JsonResolver;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetAddressListRequest extends BaseMtopRequest {
    private static final String API = "com.taobao.mtop.deliver.getAddressList";
    private static final long serialVersionUID = -5188348129646949861L;

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("sid", CoreApplication.getLoginHelper((Context) null).getSessionId());
        return null;
    }

    /* access modifiers changed from: protected */
    public List<Address> resolveResponse(JSONObject obj) throws Exception {
        return JsonResolver.resolveAddressList(obj);
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
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
