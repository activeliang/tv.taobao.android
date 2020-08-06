package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderListData implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private int total;

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public static OrderListData resolverFromMtop(JSONObject obj) throws JSONException {
        JSONObject obj2;
        JSONObject obj3;
        JSONObject obj4;
        if (obj == null) {
            return null;
        }
        OrderListData orderListData = new OrderListData();
        orderListData.setTotal(0);
        JSONObject obj1 = obj.optJSONObject("data");
        if (obj1 == null || (obj2 = obj1.optJSONObject("meta")) == null || (obj3 = obj2.optJSONObject("page")) == null || (obj4 = obj3.optJSONObject("fields")) == null) {
            return orderListData;
        }
        orderListData.setTotal(obj4.optInt("totalNumber", 0));
        return orderListData;
    }
}
