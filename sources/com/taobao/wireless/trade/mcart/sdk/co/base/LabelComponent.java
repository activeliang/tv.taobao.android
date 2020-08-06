package com.taobao.wireless.trade.mcart.sdk.co.base;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class LabelComponent extends Component {
    public LabelComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String getLeftIcon() {
        return this.fields.getString("leftIcon");
    }

    public String getRightIcon() {
        return this.fields.getString("rightIcon");
    }

    public String toString() {
        return super.toString() + "-LabelComponent  [title = " + getTitle() + ", url = " + getUrl() + ", icon = " + getLeftIcon() + "]";
    }
}
