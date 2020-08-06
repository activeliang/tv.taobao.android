package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

public class DynamicCrossShopPromotion {
    private JSONObject data;

    public DynamicCrossShopPromotion(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getIcon() {
        return this.data.getString("icon");
    }

    public String getRule() {
        return this.data.getString("rule");
    }

    public String getTitle() {
        return this.data.getString("title");
    }

    public String getColor() {
        return this.data.getString(UpdatePreference.COLOR);
    }

    public String getNextTitle() {
        return this.data.getString("nextTitle");
    }

    public String getUrl() {
        return this.data.getString("url");
    }

    public String toString() {
        return super.toString() + " - DynamicPromotion [icon=" + getIcon() + ",rule=" + getRule() + ",title=" + getTitle() + ",color=" + getColor() + ",nextTitle=" + getNextTitle() + ",url=" + getUrl() + "]";
    }
}
