package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderCellObject implements Serializable {
    private static final long serialVersionUID = 5019801047356034179L;
    private ArrayList<String> icon;
    private String itemId;
    private ArrayList<String> itemPromtion;
    private String itemProperty;
    private String orderId;
    private String pic;
    private String quantity;
    private String sPrice;
    private String snapshot;
    private String title;

    public String getSnapshot() {
        return this.snapshot;
    }

    public void setSnapshot(String snapshot2) {
        this.snapshot = snapshot2;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId2) {
        this.orderId = orderId2;
    }

    public ArrayList<String> getIcon() {
        return this.icon;
    }

    public String getItemProperty() {
        return this.itemProperty;
    }

    public void setItemProperty(String itemProperty2) {
        this.itemProperty = itemProperty2;
    }

    public void setIcon(ArrayList<String> icon2) {
        this.icon = icon2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public ArrayList<String> getItemPromtion() {
        return this.itemPromtion;
    }

    public void setItemPromtion(ArrayList<String> itemPromtion2) {
        this.itemPromtion = itemPromtion2;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getSPrice() {
        return this.sPrice;
    }

    public void setSPrice(String sPrice2) {
        this.sPrice = sPrice2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public static OrderCellObject resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        OrderCellObject orderCell = new OrderCellObject();
        if (!obj.isNull("itemId")) {
            orderCell.setItemId(obj.getString("itemId"));
        }
        if (!obj.isNull("orderId")) {
            orderCell.setOrderId(obj.getString("orderId"));
        }
        if (!obj.isNull("snapshot")) {
            orderCell.setSnapshot(obj.getString("snapshot"));
        }
        if (!obj.isNull("pic")) {
            orderCell.setPic(obj.getString("pic"));
        }
        if (!obj.isNull("quantity")) {
            orderCell.setQuantity(obj.getString("quantity"));
        }
        if (!obj.isNull("title")) {
            orderCell.setTitle(obj.getString("title"));
        }
        if (!obj.isNull("itemProperty")) {
            orderCell.setItemProperty(obj.getString("itemProperty"));
        }
        if (!obj.isNull("sPrice")) {
            orderCell.setSPrice(obj.getString("sPrice"));
        }
        if (!obj.isNull("icon")) {
            JSONArray array = obj.getJSONArray("icon");
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp.add(array.getString(i));
            }
            orderCell.setIcon(temp);
        }
        if (obj.isNull("itemPromtion")) {
            return orderCell;
        }
        JSONArray array2 = obj.getJSONArray("itemPromtion");
        ArrayList<String> temp2 = new ArrayList<>();
        for (int i2 = 0; i2 < array2.length(); i2++) {
            temp2.add(array2.getString(i2));
        }
        orderCell.setItemPromtion(temp2);
        return orderCell;
    }
}
