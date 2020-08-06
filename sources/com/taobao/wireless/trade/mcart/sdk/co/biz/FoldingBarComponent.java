package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class FoldingBarComponent extends Component {
    public FoldingBarComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getTitle() {
        if (this.fields != null) {
            return this.fields.getString("title");
        }
        return null;
    }

    public String getNextTitle() {
        if (this.fields != null) {
            return this.fields.getString("nextTitle");
        }
        return null;
    }

    public boolean isFolded() {
        return Boolean.parseBoolean(this.fields != null ? this.fields.getString("folded") : null);
    }

    public String toString() {
        return this.data != null ? this.data.toJSONString() : "";
    }
}
