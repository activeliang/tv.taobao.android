package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class FloatTipsComponent extends Component {
    private TextStyle style;

    public FloatTipsComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.style = null;
    }

    public String getContent() {
        return this.fields.getString("content");
    }

    public String getTips() {
        return this.fields.getString("tips");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public TextStyle getTextStyle() {
        if (this.style == null) {
            this.style = new TextStyle(this.fields.getJSONObject("css"));
        }
        return this.style;
    }
}
