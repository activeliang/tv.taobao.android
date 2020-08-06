package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class Promotion implements Serializable {
    private static final long serialVersionUID = -3813035539734207655L;
    private String description;
    private String name;
    private String price;
    private String type;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public static Promotion resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Promotion p = new Promotion();
        if (!obj.isNull("description")) {
            p.setDescription(obj.getString("description"));
        }
        if (!obj.isNull("name")) {
            p.setName(obj.getString("name"));
        }
        if (!obj.isNull("price")) {
            p.setPrice(obj.getString("price"));
        }
        if (obj.isNull("type")) {
            return p;
        }
        p.setType(obj.getString("type"));
        return p;
    }
}
