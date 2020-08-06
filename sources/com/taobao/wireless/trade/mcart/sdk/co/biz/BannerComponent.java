package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class BannerComponent extends Component {
    public BannerComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getPic() {
        return this.fields.getString("pic");
    }

    public String getIconUrl() {
        return this.fields.getString("iconUrl");
    }

    public String getText() {
        return this.fields.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
    }

    public String getTextColor() {
        return this.fields.getString("textColor");
    }

    public String getTextBgColor() {
        return this.fields.getString("textBgColor");
    }

    public long getTextDisturbCloseSeconds() {
        return this.fields.getLongValue("textDisturbCloseSeconds");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String toString() {
        return super.toString() + " - BannerComponent [pic=" + getPic() + ",text=" + getText() + ",textColor=" + getTextColor() + ",textBgColor=" + getTextBgColor() + ",url=" + getUrl() + ",textDisturbCloseSeconds=" + getTextDisturbCloseSeconds() + "]";
    }
}
