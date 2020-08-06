package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.OrderListLogistics;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class GetLogisticsRequest extends BaseMtopRequest {
    private static final String API = "mtop.cnwireless.CNLogisticDetailService.queryLogisDetailByTradeId";
    private final String VERSION = "2.0";

    public GetLogisticsRequest(String orderId) {
        addParams("orderId", orderId);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public List<OrderListLogistics> resolveResponse(JSONObject obj) throws Exception {
        if (obj != null && obj.has("orderList")) {
            return JSON.parseArray(obj.getJSONArray("orderList").toString(), OrderListLogistics.class);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "2.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }
}
