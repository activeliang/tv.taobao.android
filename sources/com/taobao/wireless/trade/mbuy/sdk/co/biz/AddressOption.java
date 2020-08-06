package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class AddressOption {
    private JSONObject data;

    public AddressOption(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getId() {
        return this.data.getString("deliveryAddressId");
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

    public String getAddressDetail() {
        return this.data.getString("addressDetail");
    }

    public String getFullName() {
        return this.data.getString("fullName");
    }

    public String getTele() {
        return this.data.getString("tele");
    }

    public String getMobile() {
        return this.data.getString("mobile");
    }

    public String getAgencyReceiveDesc() {
        return this.data.getString("agencyReceiveDesc");
    }

    public boolean isEnableStation() {
        return this.data.getBooleanValue("enableStation");
    }

    public boolean isEnableMDZT() {
        return this.data.getBooleanValue("enableMDZT");
    }
}
