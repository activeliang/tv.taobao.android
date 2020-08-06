package com.yunos.tvtaobao.biz.request.item;

import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.OrderLogisticMo;
import java.util.Map;
import org.json.JSONObject;

public class GetOrderLogistic extends BaseMtopRequest {
    private static final String API = "mtop.logistic.getLogisticByOrderId";
    private static final long serialVersionUID = -9160622703250397359L;
    private Long orderId;

    public GetOrderLogistic(Long orderId2) {
        setOrderId(orderId2);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("orderId", String.valueOf(this.orderId));
        return null;
    }

    /* access modifiers changed from: protected */
    public OrderLogisticMo resolveResponse(JSONObject obj) throws Exception {
        return OrderLogisticMo.resolveFromMTOP(obj);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId2) {
        this.orderId = orderId2;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }
}
