package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Address implements Serializable {
    public static final int DEFAULT_DELIVER_ADDRESS_STATUS = 1;
    public static final int UNDEFAULT_DELIVER_ADDRESS_STATUS = 0;
    private static final long serialVersionUID = -587025104691192413L;
    private String addressDetail = "";
    private int addressType = 0;
    private String area = "";
    private String city = "";
    private String deliverId = "";
    private String divisionCode = "";
    private String fullName = "";
    private String mobile = "";
    private String post = "";
    private String province = "";
    private int status;

    public String getDeliverId() {
        return this.deliverId;
    }

    public void setDeliverId(String deliverId2) {
        this.deliverId = deliverId2;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName2) {
        this.fullName = fullName2;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile2) {
        this.mobile = mobile2;
    }

    public String getPost() {
        return this.post;
    }

    public void setPost(String post2) {
        this.post = post2;
    }

    public String getDivisionCode() {
        return this.divisionCode;
    }

    public void setDivisionCode(String divisionCode2) {
        this.divisionCode = divisionCode2;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province2) {
        this.province = province2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area2) {
        this.area = area2;
    }

    public String getAddressDetail() {
        return this.addressDetail;
    }

    public String getFullAddress() {
        return this.province + " " + this.city + " " + this.area + " " + this.addressDetail;
    }

    public void setAddressDetail(String addressDetail2) {
        this.addressDetail = addressDetail2;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status2) {
        this.status = status2;
    }

    public int getAddressType() {
        return this.addressType;
    }

    public void setAddressType(int addressType2) {
        this.addressType = addressType2;
    }

    public static Address fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Address item = new Address();
        item.setDeliverId(obj.optString("deliverId"));
        item.setFullName(obj.optString("fullName"));
        item.setMobile(obj.optString("mobile"));
        item.setPost(obj.optString("post"));
        item.setDivisionCode(obj.optString("divisionCode"));
        item.setProvince(obj.optString("province"));
        item.setCity(obj.optString("city"));
        item.setArea(obj.optString("area"));
        item.setAddressDetail(obj.optString("addressDetail"));
        item.setStatus(obj.optInt("status"));
        item.setAddressType(obj.optInt("addressType"));
        return item;
    }

    public boolean equals(Address a) {
        if (!this.deliverId.equals(a.deliverId) || !this.fullName.equals(a.fullName) || !this.mobile.equals(a.mobile) || !this.post.equals(a.post) || !this.divisionCode.equals(a.divisionCode) || !this.addressDetail.equals(a.addressDetail)) {
            return false;
        }
        return true;
    }
}
