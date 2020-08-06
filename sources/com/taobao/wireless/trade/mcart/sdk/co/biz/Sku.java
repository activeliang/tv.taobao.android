package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.common.BaseConfig;

public class Sku {
    private JSONObject data;

    public Sku(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getSkuId() {
        return this.data.getString("skuId");
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public String getSkuStatus() {
        return this.data.getString("status");
    }

    public String getInvalidMsg() {
        if (this.data != null) {
            return this.data.getString("invalidMsg");
        }
        return null;
    }

    public boolean isSkuInvalid() {
        if (this.data != null) {
            return this.data.getBooleanValue("skuInvalid");
        }
        return false;
    }

    public String getAreaId() {
        return this.data.getString(BaseConfig.INTENT_KEY_AREAID);
    }

    public boolean isEditable() {
        return this.data.getBooleanValue("editable");
    }

    public String toString() {
        return "Sku [title=" + getTitle() + ",skuId=" + getSkuId() + ",skuStatus=" + getSkuStatus() + ",areaId=" + getAreaId() + ",editable=" + isEditable() + "]";
    }
}
