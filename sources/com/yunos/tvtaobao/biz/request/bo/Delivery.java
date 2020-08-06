package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Delivery implements Serializable {
    private static final long serialVersionUID = -2803043364298675013L;
    private int deliveryFeeType;
    private List<String> deliveryFees;
    private String destination;
    private String title;

    public int getDeliveryFeeType() {
        return this.deliveryFeeType;
    }

    public void setDeliveryFeeType(int deliveryFeeType2) {
        this.deliveryFeeType = deliveryFeeType2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public static Delivery resolveFromMTOP(JSONObject obj) throws JSONException {
        JSONArray jsonArray;
        if (obj == null) {
            return null;
        }
        Delivery d = new Delivery();
        if (!obj.isNull("deliveryFeeType")) {
            d.setDeliveryFeeType(obj.optInt("deliveryFeeType"));
        }
        if (!obj.isNull("title")) {
            d.setTitle(obj.getString("title"));
        }
        if (!obj.isNull("destination")) {
            d.setDestination(obj.getString("destination"));
        }
        if (obj.isNull("deliveryFees") || (jsonArray = obj.getJSONArray("deliveryFees")) == null || jsonArray.length() <= 0) {
            return d;
        }
        List<String> deliveryFees2 = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                deliveryFees2.add(new JSONObject(jsonArray.getString(i)).getString("title"));
            } catch (Exception e) {
            }
        }
        if (deliveryFees2.size() < 1) {
            return d;
        }
        d.setDeliveryFees(deliveryFees2);
        return d;
    }

    public List<String> getDeliveryFees() {
        return this.deliveryFees;
    }

    public void setDeliveryFees(List<String> deliveryFees2) {
        this.deliveryFees = deliveryFees2;
    }
}
