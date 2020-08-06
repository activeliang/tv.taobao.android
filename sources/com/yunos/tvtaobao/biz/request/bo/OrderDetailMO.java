package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailMO implements Serializable {
    private static final long serialVersionUID = 421670632341695025L;
    private DeliverInfo deliverInfo;
    private OrderInfoObject orderInfo;
    private OrderSellerInfo sellerInfo;

    public OrderInfoObject getOrderInfo() {
        return this.orderInfo;
    }

    public void setOrderInfo(OrderInfoObject orderInfo2) {
        this.orderInfo = orderInfo2;
    }

    public OrderSellerInfo getSellerInfo() {
        return this.sellerInfo;
    }

    public void setSellerInfo(OrderSellerInfo sellerInfo2) {
        this.sellerInfo = sellerInfo2;
    }

    public DeliverInfo getDeliverInfo() {
        return this.deliverInfo;
    }

    public void setDeliverInfo(DeliverInfo deliverInfo2) {
        this.deliverInfo = deliverInfo2;
    }

    public static OrderDetailMO resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderDetailMO orderDetail = new OrderDetailMO();
        if (!obj.isNull("orderInfo")) {
            orderDetail.setOrderInfo(OrderInfoObject.resolveFromMTOP(obj.getJSONObject("orderInfo")));
        }
        if (!obj.isNull("sellerInfo")) {
            orderDetail.setSellerInfo(OrderSellerInfo.resolveFromMTOP(obj.getJSONObject("sellerInfo")));
        }
        if (obj.isNull("deliverInfo")) {
            return orderDetail;
        }
        orderDetail.setDeliverInfo(DeliverInfo.resolveFromMTOP(obj.getJSONObject("deliverInfo")));
        return orderDetail;
    }
}
