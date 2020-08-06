package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoDetails4Address implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String address;
    private String area;
    private String areaCode;
    private boolean available;
    private String city;
    private String cityCode;
    private boolean defaultValue;
    private String id;
    private String mobile;
    private String name;
    private String positionX;
    private String positionY;
    private String province;
    private String street;
    private String userId;

    public String getFullAddress() {
        return this.province + " " + this.city + " " + this.area + " " + this.street + " " + this.address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area2) {
        this.area = area2;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode2) {
        this.areaCode = areaCode2;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available2) {
        this.available = available2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode2) {
        this.cityCode = cityCode2;
    }

    public boolean isDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(boolean defaultValue2) {
        this.defaultValue = defaultValue2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province2) {
        this.province = province2;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street2) {
        this.street = street2;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getPositionX() {
        return this.positionX;
    }

    public void setPositionX(String positionX2) {
        this.positionX = positionX2;
    }

    public String getPositionY() {
        return this.positionY;
    }

    public void setPositionY(String positionY2) {
        this.positionY = positionY2;
    }

    public static TakeOutOrderInfoDetails4Address resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoDetails4Address details4Address = new TakeOutOrderInfoDetails4Address();
        if (obj == null) {
            return details4Address;
        }
        details4Address.setDefaultValue(obj.optBoolean("defaultValue", false));
        details4Address.setAddress(obj.optString("address"));
        details4Address.setArea(obj.optString("area"));
        details4Address.setAreaCode(obj.optString("areaCode"));
        details4Address.setAvailable(obj.optBoolean("available", false));
        details4Address.setCity(obj.optString("city"));
        details4Address.setCityCode(obj.optString("cityCode"));
        details4Address.setId(obj.optString("id"));
        details4Address.setMobile(obj.optString("mobile"));
        details4Address.setName(obj.optString("name"));
        details4Address.setProvince(obj.optString("province"));
        details4Address.setStreet(obj.optString("street"));
        details4Address.setUserId(obj.optString("userId"));
        details4Address.setPositionX(obj.optString("x"));
        details4Address.setPositionY(obj.optString("y"));
        return details4Address;
    }
}
