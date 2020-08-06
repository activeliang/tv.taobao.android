package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class DeliverInfo implements Serializable {
    private static final long serialVersionUID = 1558140207778470313L;
    private String address;
    private String memo;
    private String name;
    private String phone;
    private String post;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone2) {
        this.phone = phone2;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo2) {
        this.memo = memo2;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address2) {
        this.address = address2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPost() {
        return this.post;
    }

    public void setPost(String post2) {
        this.post = post2;
    }

    public static DeliverInfo resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        DeliverInfo deliverInfo = new DeliverInfo();
        if (!obj.isNull("address")) {
            deliverInfo.setAddress(obj.getString("address"));
        }
        if (!obj.isNull("name")) {
            deliverInfo.setName(obj.getString("name"));
        }
        if (!obj.isNull("phone")) {
            deliverInfo.setPhone(obj.getString("phone"));
        }
        if (!obj.isNull("post")) {
            deliverInfo.setPost(obj.getString("post"));
        }
        if (obj.isNull("memo")) {
            return deliverInfo;
        }
        deliverInfo.setMemo(obj.getString("memo"));
        return deliverInfo;
    }
}
