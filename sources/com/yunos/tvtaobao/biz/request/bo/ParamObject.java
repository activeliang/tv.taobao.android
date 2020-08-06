package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class ParamObject implements Serializable {
    private static final long serialVersionUID = -7969575361477885013L;
    private String orderId;
    private String payType;

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(String payType2) {
        this.payType = payType2;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId2) {
        this.orderId = orderId2;
    }

    public static ParamObject resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        ParamObject paramObject = new ParamObject();
        paramObject.setOrderId(obj.optString("orderId"));
        paramObject.setPayType(obj.optString("payType"));
        return paramObject;
    }
}
