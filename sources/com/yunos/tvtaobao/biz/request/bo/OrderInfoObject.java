package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderInfoObject implements Serializable {
    private static final long serialVersionUID = 1960216151087042450L;
    private String bizOrderId;
    private String confirmTime;
    private String createTime;
    private ArrayList<String> icon;
    private ArrayList<OrderCellObject> orderCell;
    private ArrayList<String> orderMessage;
    private String orderStatus;
    private String orderStatusCode;
    private ArrayList<String> payDesc;
    private String payOrderId;
    private String postFee;
    private ArrayList<String> promotion;
    private String sendTime;
    private String totalPrice;
    private String type;
    private String version;

    public String getOrderStatusCode() {
        return this.orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode2) {
        this.orderStatusCode = orderStatusCode2;
    }

    public String getPostFee() {
        return this.postFee;
    }

    public void setPostFee(String postFee2) {
        this.postFee = postFee2;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(String sendTime2) {
        this.sendTime = sendTime2;
    }

    public String getConfirmTime() {
        return this.confirmTime;
    }

    public void setConfirmTime(String confirmTime2) {
        this.confirmTime = confirmTime2;
    }

    public ArrayList<String> getOrderMessage() {
        return this.orderMessage;
    }

    public void setOrderMessage(ArrayList<String> orderMessage2) {
        this.orderMessage = orderMessage2;
    }

    public ArrayList<OrderCellObject> getOrderCell() {
        return this.orderCell;
    }

    public void setOrderCell(ArrayList<OrderCellObject> orderCell2) {
        this.orderCell = orderCell2;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus2) {
        this.orderStatus = orderStatus2;
    }

    public ArrayList<String> getPayDesc() {
        return this.payDesc;
    }

    public void setPayDesc(ArrayList<String> payDesc2) {
        this.payDesc = payDesc2;
    }

    public String getPayOrderId() {
        return this.payOrderId;
    }

    public void setPayOrderId(String payOrderId2) {
        this.payOrderId = payOrderId2;
    }

    public ArrayList<String> getPromotion() {
        return this.promotion;
    }

    public void setPromotion(ArrayList<String> promotion2) {
        this.promotion = promotion2;
    }

    public String getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(String totalPrice2) {
        this.totalPrice = totalPrice2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public String getBizOrderId() {
        return this.bizOrderId;
    }

    public void setBizOrderId(String bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime2) {
        this.createTime = createTime2;
    }

    public ArrayList<String> getIcon() {
        return this.icon;
    }

    public void setIcon(ArrayList<String> icon2) {
        this.icon = icon2;
    }

    public static OrderInfoObject resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderInfoObject orderInfo = new OrderInfoObject();
        if (!obj.isNull("bizOrderId")) {
            orderInfo.setBizOrderId(obj.getString("bizOrderId"));
        }
        if (!obj.isNull("createTime")) {
            orderInfo.setCreateTime(obj.getString("createTime"));
        }
        if (!obj.isNull("orderStatus")) {
            orderInfo.setOrderStatus(obj.getString("orderStatus"));
        }
        if (!obj.isNull("orderStatusCode")) {
            orderInfo.setOrderStatusCode(obj.getString("orderStatusCode"));
        }
        if (!obj.isNull("payOrderId")) {
            orderInfo.setPayOrderId(obj.getString("payOrderId"));
        }
        if (!obj.isNull("totalPrice")) {
            orderInfo.setTotalPrice(obj.getString("totalPrice"));
        }
        if (!obj.isNull("postFee")) {
            orderInfo.setPostFee(obj.getString("postFee"));
        }
        if (!obj.isNull("type")) {
            orderInfo.setType(obj.getString("type"));
        }
        if (!obj.isNull("version")) {
            orderInfo.setVersion(obj.getString("version"));
        }
        if (!obj.isNull("sendTime")) {
            orderInfo.setSendTime(obj.getString("sendTime"));
        }
        if (!obj.isNull("confirmTime")) {
            orderInfo.setConfirmTime(obj.getString("confirmTime"));
        }
        if (!obj.isNull("icon")) {
            JSONArray array = obj.getJSONArray("icon");
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp.add(array.getString(i));
            }
            orderInfo.setIcon(temp);
        }
        if (!obj.isNull("orderCell")) {
            JSONArray array2 = obj.getJSONArray("orderCell");
            ArrayList<OrderCellObject> temp2 = new ArrayList<>();
            for (int i2 = 0; i2 < array2.length(); i2++) {
                temp2.add(OrderCellObject.resolveFromMTOP(array2.getJSONObject(i2)));
            }
            orderInfo.setOrderCell(temp2);
        }
        if (!obj.isNull("payDesc")) {
            JSONArray array3 = obj.getJSONArray("payDesc");
            ArrayList<String> temp3 = new ArrayList<>();
            for (int i3 = 0; i3 < array3.length(); i3++) {
                temp3.add(array3.getString(i3));
            }
            orderInfo.setPayDesc(temp3);
        }
        if (!obj.isNull("promotion")) {
            JSONArray array4 = obj.getJSONArray("promotion");
            ArrayList<String> temp4 = new ArrayList<>();
            for (int i4 = 0; i4 < array4.length(); i4++) {
                temp4.add(array4.getString(i4));
            }
            orderInfo.setPromotion(temp4);
        }
        if (obj.isNull("orderMessage")) {
            return orderInfo;
        }
        JSONArray array5 = obj.getJSONArray("orderMessage");
        ArrayList<String> temp5 = new ArrayList<>();
        for (int i5 = 0; i5 < array5.length(); i5++) {
            temp5.add(array5.getString(i5));
        }
        orderInfo.setOrderMessage(temp5);
        return orderInfo;
    }
}
