package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String appointOrderId;
    private String buyerMessage;
    private TakeOutOrderInfoDetails4ContactInfo contactInfo;
    private String deliveryTime;
    private int deliveryType;
    private TakeOutOrderInfoDetails4Address details4Address;
    private TakeOutOrderInfoDetails4StoreInfo details4StoreInfo;
    private TakeOutOrderInfoDetails4TradeInfo details4Trade;
    private String expectDeliveryTime;
    private boolean hasDeliver;
    private String invoice;
    private boolean isFromOnlinePay;
    private TakeOutOrderInfoDetails4OnTimeInfo onTimeInfo;
    private boolean onlinePay;
    private TakeOutOrderInfoDetails4Fee orderInfoDetails4Fee;
    private String outOrderId;
    private ArrayList<TakeOutOrderInfoDetails4Process> processArrayList;
    private String processNodeId;
    private ArrayList<TakeOutOrderProductInfoBase> productInfoList;
    private String shopStatus;
    private String status;
    private String statusDesc;
    private String statusInterval;
    private String tbMainOrderId;

    public String getBuyerMessage() {
        return this.buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage2) {
        this.buyerMessage = buyerMessage2;
    }

    public String getDeliveryTime() {
        return this.deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime2) {
        this.deliveryTime = deliveryTime2;
    }

    public int getDeliveryType() {
        return this.deliveryType;
    }

    public void setDeliveryType(int deliveryType2) {
        this.deliveryType = deliveryType2;
    }

    public String getExpectDeliveryTime() {
        return this.expectDeliveryTime;
    }

    public void setExpectDeliveryTime(String expectDeliveryTime2) {
        this.expectDeliveryTime = expectDeliveryTime2;
    }

    public boolean isHasDeliver() {
        return this.hasDeliver;
    }

    public void setHasDeliver(boolean hasDeliver2) {
        this.hasDeliver = hasDeliver2;
    }

    public String getInvoice() {
        return this.invoice;
    }

    public void setInvoice(String invoice2) {
        this.invoice = invoice2;
    }

    public boolean isOnlinePay() {
        return this.onlinePay;
    }

    public void setOnlinePay(boolean onlinePay2) {
        this.onlinePay = onlinePay2;
    }

    public boolean isFromOnlinePay() {
        return this.isFromOnlinePay;
    }

    public void setFromOnlinePay(boolean fromOnlinePay) {
        this.isFromOnlinePay = fromOnlinePay;
    }

    public String getProcessNodeId() {
        return this.processNodeId;
    }

    public void setProcessNodeId(String processNodeId2) {
        this.processNodeId = processNodeId2;
    }

    public String getShopStatus() {
        return this.shopStatus;
    }

    public void setShopStatus(String shopStatus2) {
        this.shopStatus = shopStatus2;
    }

    public String getStatusDesc() {
        return this.statusDesc;
    }

    public void setStatusDesc(String statusDesc2) {
        this.statusDesc = statusDesc2;
    }

    public String getStatusInterval() {
        return this.statusInterval;
    }

    public void setStatusInterval(String statusInterval2) {
        this.statusInterval = statusInterval2;
    }

    public TakeOutOrderInfoDetails4Address getDetails4Address() {
        return this.details4Address;
    }

    public void setDetails4Address(TakeOutOrderInfoDetails4Address details4Address2) {
        this.details4Address = details4Address2;
    }

    public ArrayList<TakeOutOrderInfoDetails4Process> getProcessArrayList() {
        return this.processArrayList;
    }

    public void setProcessArrayList(ArrayList<TakeOutOrderInfoDetails4Process> processArrayList2) {
        this.processArrayList = processArrayList2;
    }

    public TakeOutOrderInfoDetails4TradeInfo getDetails4Trade() {
        return this.details4Trade;
    }

    public void setDetails4Trade(TakeOutOrderInfoDetails4TradeInfo details4Trade2) {
        this.details4Trade = details4Trade2;
    }

    public TakeOutOrderInfoDetails4StoreInfo getDetails4StoreInfo() {
        return this.details4StoreInfo;
    }

    public void setDetails4StoreInfo(TakeOutOrderInfoDetails4StoreInfo details4StoreInfo2) {
        this.details4StoreInfo = details4StoreInfo2;
    }

    public TakeOutOrderInfoDetails4Fee getOrderInfoDetails4Fee() {
        return this.orderInfoDetails4Fee;
    }

    public void setOrderInfoDetails4Fee(TakeOutOrderInfoDetails4Fee orderInfoDetails4Fee2) {
        this.orderInfoDetails4Fee = orderInfoDetails4Fee2;
    }

    public TakeOutOrderInfoDetails4ContactInfo getContactInfo() {
        return this.contactInfo;
    }

    public void setContactInfo(TakeOutOrderInfoDetails4ContactInfo contactInfo2) {
        this.contactInfo = contactInfo2;
    }

    public TakeOutOrderInfoDetails4OnTimeInfo getOnTimeInfo() {
        return this.onTimeInfo;
    }

    public void setOnTimeInfo(TakeOutOrderInfoDetails4OnTimeInfo onTimeInfo2) {
        this.onTimeInfo = onTimeInfo2;
    }

    public ArrayList<TakeOutOrderProductInfoBase> getProductInfoList() {
        return this.productInfoList;
    }

    public void setProductInfoList(ArrayList<TakeOutOrderProductInfoBase> productInfoList2) {
        this.productInfoList = productInfoList2;
    }

    public String getAppointOrderId() {
        return this.appointOrderId;
    }

    public void setAppointOrderId(String appointOrderId2) {
        this.appointOrderId = appointOrderId2;
    }

    public String getTbMainOrderId() {
        return this.tbMainOrderId;
    }

    public void setTbMainOrderId(String tbMainOrderId2) {
        this.tbMainOrderId = tbMainOrderId2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getOutOrderId() {
        return this.outOrderId;
    }

    public void setOutOrderId(String outOrderId2) {
        this.outOrderId = outOrderId2;
    }

    public static TakeOutOrderInfoDetails resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails orderInfoDetails = new TakeOutOrderInfoDetails();
        if (obj == null) {
            return orderInfoDetails;
        }
        orderInfoDetails.setTbMainOrderId(obj.optString("tbMainOrderId"));
        orderInfoDetails.setStatus(obj.optString("status"));
        orderInfoDetails.setShopStatus(obj.optString("shopStatus"));
        orderInfoDetails.setOnlinePay(obj.optBoolean("onlinePay", false));
        orderInfoDetails.setFromOnlinePay(obj.optBoolean("isOnlinePay", false));
        orderInfoDetails.setHasDeliver(obj.optBoolean("hasDeliver", false));
        orderInfoDetails.setDeliveryType(obj.optInt("deliveryType", 0));
        orderInfoDetails.setStatusDesc(obj.optString("statusDesc"));
        orderInfoDetails.setStatusInterval(obj.optString("statusInterval"));
        orderInfoDetails.setProcessNodeId(obj.optString("processNodeId"));
        orderInfoDetails.setExpectDeliveryTime(obj.optString("expectDeliveryTime"));
        orderInfoDetails.setDeliveryTime(obj.optString("deliveryTime"));
        orderInfoDetails.setBuyerMessage(obj.optString("buyerMessage"));
        orderInfoDetails.setOutOrderId(obj.optString("outOrderId"));
        orderInfoDetails.setAppointOrderId(obj.optString("appointOrderId"));
        if (!obj.isNull("onTimeInfo")) {
            orderInfoDetails.setOnTimeInfo(TakeOutOrderInfoDetails4OnTimeInfo.resolverFromMtop(obj.getJSONObject("onTimeInfo")));
        }
        if (!obj.isNull("orderContactInfo")) {
            orderInfoDetails.setContactInfo(TakeOutOrderInfoDetails4ContactInfo.resolverFromMtop(obj.getJSONObject("orderContactInfo")));
        }
        if (!obj.isNull("orderFee")) {
            orderInfoDetails.setOrderInfoDetails4Fee(TakeOutOrderInfoDetails4Fee.resolverFromMtop(obj.getJSONObject("orderFee")));
        }
        if (!obj.isNull("serviceAddress")) {
            orderInfoDetails.setDetails4Address(TakeOutOrderInfoDetails4Address.resolverFromMtop(obj.getJSONObject("serviceAddress")));
        }
        if (!obj.isNull("tradeInfo")) {
            orderInfoDetails.setDetails4Trade(TakeOutOrderInfoDetails4TradeInfo.resolverFromMtop(obj.getJSONObject("tradeInfo")));
        }
        if (!obj.isNull("storeInfo")) {
            orderInfoDetails.setDetails4StoreInfo(TakeOutOrderInfoDetails4StoreInfo.resolverFromMtop(obj.getJSONObject("storeInfo")));
        }
        if (!obj.isNull("orderItems")) {
            JSONArray array = obj.getJSONArray("orderItems");
            ArrayList<TakeOutOrderProductInfoBase> temp = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp.add(TakeOutOrderProductInfoBase.resolverFromMtop(array.getJSONObject(i)));
            }
            orderInfoDetails.setProductInfoList(temp);
        }
        if (obj.isNull("processInfo")) {
            return orderInfoDetails;
        }
        JSONArray array2 = obj.getJSONArray("processInfo");
        ArrayList<TakeOutOrderInfoDetails4Process> temp2 = new ArrayList<>();
        for (int i2 = 0; i2 < array2.length(); i2++) {
            temp2.add(TakeOutOrderInfoDetails4Process.resolverFromMtop(array2.getJSONObject(i2)));
        }
        orderInfoDetails.setProcessArrayList(temp2);
        return orderInfoDetails;
    }
}
