package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class RootComponent extends Component {
    public RootComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
        this.engine.setCurrencySymbol(getCurrencySymbol());
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.engine.setCurrencySymbol(getCurrencySymbol());
    }

    public String getJoinId() {
        return this.fields.getString("joinId");
    }

    public boolean isOpenFrontTrace() {
        return this.fields.getBooleanValue("openFrontTrace");
    }

    public String getCurrencySymbol() {
        String tmp = this.fields.getString("currencySymbol");
        return TextUtils.isEmpty(tmp) ? "￥" : tmp;
    }
}
