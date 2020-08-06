package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceUnits implements Serializable {
    private static final long serialVersionUID = -1164868570081118066L;
    private String display;
    private String name;
    private String price;
    private Boolean valid;

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display2) {
        this.display = display2;
    }

    public Boolean getValid() {
        return this.valid;
    }

    public void setValid(Boolean valid2) {
        this.valid = valid2;
    }

    public static PriceUnits resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        PriceUnits priceUnits = new PriceUnits();
        if (!obj.isNull("name")) {
            priceUnits.setName(obj.getString("name"));
        }
        if (!obj.isNull("price")) {
            priceUnits.setPrice(obj.getString("price"));
        }
        if (!obj.isNull("display")) {
            priceUnits.setDisplay(obj.getString("display"));
        }
        if (obj.isNull("valid")) {
            return priceUnits;
        }
        priceUnits.setValid(Boolean.valueOf(obj.getBoolean("valid")));
        return priceUnits;
    }
}
