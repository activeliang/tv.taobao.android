package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class PromotionBarComponent extends Component {
    public PromotionBarComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getTitle() {
        if (this.fields != null) {
            return this.fields.getString("title");
        }
        return null;
    }

    public String getPic() {
        if (this.fields != null) {
            return this.fields.getString("pic");
        }
        return null;
    }

    public String getUrl() {
        if (this.fields != null) {
            return this.fields.getString("url");
        }
        return null;
    }

    public String getNextTitle() {
        if (this.fields != null) {
            return this.fields.getString("nextTitle");
        }
        return null;
    }

    public String getBackgroundPic() {
        if (this.fields != null) {
            return this.fields.getString("backgroundPic");
        }
        return null;
    }

    public String getBackgroundLeftColor() {
        if (this.fields != null) {
            return this.fields.getString("backgroundLeftColor");
        }
        return null;
    }

    public String getBackgroundRightColor() {
        if (this.fields != null) {
            return this.fields.getString("backgroundRightColor");
        }
        return null;
    }

    public String getCrossShopTitle() {
        if (this.fields != null) {
            return this.fields.getString("crossShopTitle");
        }
        return null;
    }

    public String getFontColor() {
        if (this.fields != null) {
            return this.fields.getString("fontColor");
        }
        return null;
    }

    public String toString() {
        return this.data != null ? this.data.toJSONString() : "";
    }
}
