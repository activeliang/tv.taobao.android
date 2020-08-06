package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.SelectOption;

public class DeliveryMethodOption extends SelectOption {
    private DeliveryMethodDatePicker datePicker;

    public DeliveryMethodOption(JSONObject data, Component component) throws Exception {
        super(data);
        JSONObject datePickerData = data.getJSONObject("datePicker");
        if (datePickerData != null) {
            try {
                this.datePicker = new DeliveryMethodDatePicker(datePickerData, component);
            } catch (IllegalArgumentException e) {
                this.datePicker = null;
            }
        }
    }

    public String getId() {
        return this.data.getString("id");
    }

    public String getName() {
        return this.data.getString("message");
    }

    public String getPrice() {
        return this.data.getString("fare");
    }

    public boolean enableDatePicker() {
        return this.datePicker != null;
    }

    public DeliveryMethodDatePicker getDatePicker() {
        return this.datePicker;
    }

    public String getTip() {
        return this.data.getString("signText");
    }

    public String getServiceIcon() {
        return this.data.getString("serviceIcon");
    }

    public String getProtocolShipIcon() {
        return this.data.getString("protocolShipIcon");
    }
}
