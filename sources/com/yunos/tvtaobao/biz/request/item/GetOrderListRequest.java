package com.yunos.tvtaobao.biz.request.item;

import anet.channel.strategy.dispatch.DispatchConstants;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.OrderListData;
import java.util.Map;
import org.json.JSONObject;

public class GetOrderListRequest extends BaseMtopRequest {
    private static final long serialVersionUID = 8628119122639344258L;
    private final String API = "mtop.order.queryBoughtList";
    private final String version = DispatchConstants.VER_CODE;

    public GetOrderListRequest(String tabCode) {
        addParams("page", "1");
        addParams("tabCode", tabCode);
        addParams("appName", "tborder");
        addParams("appVersion", "1.0");
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.order.queryBoughtList";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return DispatchConstants.VER_CODE;
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
    public OrderListData resolveResponse(JSONObject obj) throws Exception {
        return OrderListData.resolverFromMtop(obj);
    }
}
