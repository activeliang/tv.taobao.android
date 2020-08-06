package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

public class PreSellAddressComponent extends Component {
    public PreSellAddressComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getTips() {
        return this.fields.getString("tips");
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public String getColor() {
        return this.fields.getString(UpdatePreference.COLOR);
    }

    public String getBgColor() {
        return this.fields.getString("bgColor");
    }

    public String getTipsColor() {
        return this.fields.getString("tipsColor");
    }

    public String getIconUrl() {
        return this.fields.getString("iconUrl");
    }
}
