package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderListData implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String buyerId;
    private int errorCode;
    private int nextPageNo;
    private ArrayList<TakeOutOrderInfoBase> orderInfoBaseList;
    private int total;

    public ArrayList<TakeOutOrderInfoBase> getOrderInfoBaseList() {
        return this.orderInfoBaseList;
    }

    public void setOrderInfoBaseList(ArrayList<TakeOutOrderInfoBase> orderInfoBaseList2) {
        this.orderInfoBaseList = orderInfoBaseList2;
    }

    public String getBuyerId() {
        return this.buyerId;
    }

    public void setBuyerId(String buyerId2) {
        this.buyerId = buyerId2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode2) {
        this.errorCode = errorCode2;
    }

    public int getNextPageNo() {
        return this.nextPageNo;
    }

    public void setNextPageNo(int nextPageNo2) {
        this.nextPageNo = nextPageNo2;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public static TakeOutOrderListData resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderListData orderListData = new TakeOutOrderListData();
        orderListData.setTotal(0);
        if (obj == null) {
            return orderListData;
        }
        orderListData.setTotal(obj.optInt("totalNumber", 0));
        orderListData.setBuyerId(obj.optString("buyerId"));
        orderListData.setErrorCode(obj.optInt("errorCode", 0));
        orderListData.setNextPageNo(obj.optInt("nextPageNo", 0));
        if (obj.isNull("orders")) {
            return orderListData;
        }
        JSONArray array = obj.getJSONArray("orders");
        ArrayList<TakeOutOrderInfoBase> temp = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            temp.add(TakeOutOrderInfoBase.resolverFromMtop(array.getJSONObject(i)));
        }
        orderListData.setOrderInfoBaseList(temp);
        return orderListData;
    }
}
