package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.fastjson.JSON;
import com.yunos.tv.core.common.User;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.DoPayOrders;
import java.util.Map;
import org.json.JSONObject;

public class DoPayRequest extends BaseMtopRequest {
    private static final String API = "mtop.order.doPay";
    private static final long serialVersionUID = 3393110315383379325L;
    private String apiVersion = "2.0";
    private Long orderId;
    private String sid;

    public DoPayRequest(Long orderId2) {
        setSid(User.getSessionId());
        setOrderId(orderId2);
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        addParams("sid", this.sid);
        addParams("orderId", String.valueOf(this.orderId));
        return null;
    }

    /* access modifiers changed from: protected */
    public DoPayOrders resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return (DoPayOrders) JSON.parseObject(obj.toString(), DoPayOrders.class);
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid2) {
        this.sid = sid2;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId2) {
        this.orderId = orderId2;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return false;
    }
}
