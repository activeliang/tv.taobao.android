package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class ActivityGift {
    private JSONObject data;
    private BuyEngine engine;

    public ActivityGift(JSONObject data2, BuyEngine engine2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
        this.engine = engine2;
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public String getPictureUrl() {
        return this.data.getString("pictureUrl");
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public String getPrice() {
        return this.data.getString("price");
    }

    public String getQuantity() {
        return this.data.getString("quantity");
    }

    public boolean isValid() {
        return this.data.getBooleanValue("valid");
    }

    public boolean isSelected() {
        return this.data.getBooleanValue("selected");
    }

    public void setSelected(boolean isSelected) {
        if (isValid()) {
            this.data.put("selected", (Object) Boolean.valueOf(isSelected));
        }
    }
}
