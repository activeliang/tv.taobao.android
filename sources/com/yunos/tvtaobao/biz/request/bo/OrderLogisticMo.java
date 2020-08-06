package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderLogisticMo implements Serializable {
    private static final long serialVersionUID = -7613669091555822045L;
    private String logisType;
    private String logisticNo;
    private String mailNo;
    private String partnerName;
    private ArrayList<OrderLogisticInfoMo> transitList;

    public String getLogisticNo() {
        return this.logisticNo;
    }

    public void setLogisticNo(String logisticNo2) {
        this.logisticNo = logisticNo2;
    }

    public ArrayList<OrderLogisticInfoMo> getTransitList() {
        return this.transitList;
    }

    public void setTransitList(ArrayList<OrderLogisticInfoMo> transitList2) {
        this.transitList = transitList2;
    }

    public String getPartnerName() {
        return this.partnerName;
    }

    public void setPartnerName(String partnerName2) {
        this.partnerName = partnerName2;
    }

    public String getLogisType() {
        return this.logisType;
    }

    public void setLogisType(String logisType2) {
        this.logisType = logisType2;
    }

    public String getMailNo() {
        return this.mailNo;
    }

    public void setMailNo(String mailNo2) {
        this.mailNo = mailNo2;
    }

    public static OrderLogisticMo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderLogisticMo orderLogisticMo = new OrderLogisticMo();
        ArrayList<OrderLogisticInfoMo> orderLogisticInfoList = new ArrayList<>();
        if (obj.isNull("orderList")) {
            return orderLogisticMo;
        }
        JSONObject orderList = (JSONObject) obj.getJSONArray("orderList").get(0);
        if (!orderList.isNull("logisticNo")) {
            orderLogisticMo.setLogisticNo(orderList.getString("logisticNo"));
        }
        if (orderList.isNull("bagList")) {
            return orderLogisticMo;
        }
        JSONObject bagList = (JSONObject) orderList.getJSONArray("bagList").get(0);
        if (!bagList.isNull("logisType")) {
            orderLogisticMo.setLogisType(bagList.getString("logisType"));
        }
        if (!bagList.isNull("partnerName")) {
            orderLogisticMo.setPartnerName(bagList.getString("partnerName"));
        }
        if (!bagList.isNull("mailNo")) {
            orderLogisticMo.setMailNo(bagList.getString("mailNo"));
        }
        if (bagList.isNull("transitList")) {
            return orderLogisticMo;
        }
        JSONArray transitListArray = bagList.getJSONArray("transitList");
        for (int i = 0; i < transitListArray.length(); i++) {
            JSONObject temp = transitListArray.getJSONObject(i);
            OrderLogisticInfoMo tempMo = new OrderLogisticInfoMo();
            if (!temp.isNull("message")) {
                tempMo.setMessage(temp.getString("message"));
            }
            if (!temp.isNull("time")) {
                tempMo.setTime(temp.getString("time"));
                orderLogisticInfoList.add(tempMo);
            }
        }
        orderLogisticMo.setTransitList(orderLogisticInfoList);
        return orderLogisticMo;
    }

    public String toString() {
        return "OrderLogisticMo [logisticNo=" + this.logisticNo + ", transitList=" + this.transitList + ", partnerName=" + this.partnerName + ", logisType=" + this.logisType + ", mailNo=" + this.mailNo + "]";
    }
}
