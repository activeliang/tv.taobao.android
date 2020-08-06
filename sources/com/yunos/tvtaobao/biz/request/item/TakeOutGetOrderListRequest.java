package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TakeOutOrderListData;
import java.util.Map;
import org.json.JSONObject;

public class TakeOutGetOrderListRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 8628119122639344258L;
    private final String API = "mtop.taobao.waimai.cheetah.orders.get";
    private final String version = "1.0";

    public TakeOutGetOrderListRequest(int pageNo) {
        addParams("pageNo", String.valueOf(pageNo));
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.waimai.cheetah.orders.get";
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
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public TakeOutOrderListData resolveResponse(JSONObject obj) throws Exception {
        return TakeOutOrderListData.resolverFromMtop(obj);
    }
}
