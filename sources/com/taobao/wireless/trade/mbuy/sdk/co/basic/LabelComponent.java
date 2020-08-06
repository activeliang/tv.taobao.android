package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class LabelComponent extends Component {
    public LabelComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getValue() {
        return this.fields.getString("value");
    }

    public TextStyle getTextStyle() {
        JSONObject d = this.fields.getJSONObject("css");
        if (d != null) {
            return new TextStyle(d);
        }
        return null;
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String getIcon() {
        return this.fields.getString("icon");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String toString() {
        return super.toString() + " - LabelComponent [value=" + getValue() + " url=" + getUrl() + "]";
    }
}
