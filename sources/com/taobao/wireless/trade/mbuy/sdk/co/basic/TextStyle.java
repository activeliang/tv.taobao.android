package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

public class TextStyle {
    private JSONObject data;

    public TextStyle(JSONObject data2) {
        this.data = data2;
    }

    public String getColor() {
        return this.data.getString(UpdatePreference.COLOR);
    }

    public boolean isBold() {
        return this.data.getBooleanValue("bold");
    }

    public boolean isItalic() {
        return this.data.getBooleanValue("italic");
    }

    public boolean isStrikeThrough() {
        return this.data.getBooleanValue("strikeThrough");
    }

    public String getBackgroundColor() {
        return this.data.getString(TuwenConstants.PARAMS.BACKGROUND_COLOR);
    }

    public String getHighlightColor() {
        return this.data.getString("highlightColor");
    }
}
