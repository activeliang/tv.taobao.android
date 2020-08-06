package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.bftv.fui.constantplugin.Constant;

public class ServiceAddressOption {
    protected JSONObject data;

    public ServiceAddressOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getId() {
        return this.data.getString("serviceAddressId");
    }

    public String getDivisionCode() {
        return this.data.getString("divisionCode");
    }

    public String getCountryName() {
        return this.data.getString("countryName");
    }

    public String getProvinceName() {
        return this.data.getString("provinceName");
    }

    public String getCityName() {
        return this.data.getString("cityName");
    }

    public String getAreaName() {
        return this.data.getString("areaName");
    }

    public String getTownName() {
        return this.data.getString("townName");
    }

    public String getTownDivisionId() {
        return this.data.getString("townDivisionId");
    }

    public String getAddressDetail() {
        return this.data.getString("addressDetail");
    }

    public String getFullName() {
        return this.data.getString("fullName");
    }

    public String getMobile() {
        return this.data.getString("mobile");
    }

    public boolean isDefaultAddress() {
        try {
            return this.data.getBooleanValue("defaultAddress");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsable() {
        try {
            return this.data.getBooleanValue("useable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder("");
        if (!TextUtils.isEmpty(getProvinceName()) && !getProvinceName().trim().equalsIgnoreCase(Constant.NULL)) {
            fullAddress.append(getProvinceName().trim());
        }
        if (!TextUtils.isEmpty(getCityName()) && !getCityName().trim().equalsIgnoreCase(Constant.NULL)) {
            fullAddress.append(" ").append(getCityName().trim());
        }
        if (!TextUtils.isEmpty(getAreaName()) && !getAreaName().trim().equalsIgnoreCase(Constant.NULL)) {
            fullAddress.append(" ").append(getAreaName().trim());
        }
        if (!TextUtils.isEmpty(getAddressDetail()) && !getAddressDetail().trim().equalsIgnoreCase(Constant.NULL)) {
            fullAddress.append(" ").append(getAddressDetail().trim());
        }
        return fullAddress.toString();
    }
}
