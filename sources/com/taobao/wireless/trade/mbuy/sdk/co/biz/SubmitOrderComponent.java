package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.TextStyle;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class SubmitOrderComponent extends Component {
    public SubmitOrderComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getSubmitTitle() {
        return this.fields.getString("submitTitle");
    }

    public TextStyle getTextStyle() {
        JSONObject d = this.fields.getJSONObject("css");
        if (d != null) {
            return new TextStyle(d);
        }
        return null;
    }

    public String getCurrencySymbol() {
        return this.engine.getCurrencySymbol();
    }

    public String getTipsMsg() {
        return this.fields.getString("tipsMsg");
    }

    public String getTipsMore() {
        return this.fields.getString("tipsMore");
    }

    public String getTipsUrl() {
        return this.fields.getString("tipsUrl");
    }
}
