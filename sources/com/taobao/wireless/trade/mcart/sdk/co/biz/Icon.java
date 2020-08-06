package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;

public class Icon {
    private JSONObject data;

    public Icon(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getText() {
        return this.data.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
    }

    public String getTextColor() {
        return this.data.getString(UpdatePreference.COLOR);
    }

    public String getBackgroundColor() {
        return this.data.getString("bgColor");
    }

    public String getPic() {
        return this.data.getString("pic");
    }

    public String getIconIDEnum() {
        return this.data.getString("iconIDEnum");
    }

    public String toString() {
        return "[text=" + getText() + ", textColor=" + getTextColor() + ", backgroundColor=" + getBackgroundColor() + ", pic=" + getPic() + ", iconIDEnum=" + getIconIDEnum() + "]";
    }
}
