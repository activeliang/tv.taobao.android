package com.taobao.wireless.trade.mbuy.sdk.co.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class BridgeComponent extends Component {
    public BridgeComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public String getValidateContent() {
        String value = null;
        JSONObject info = getInfo();
        if (info != null) {
            value = info.getString("value");
        }
        return value != null ? value : "";
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSubtitle() {
        return this.fields.getString("subtitle");
    }

    public String getUrl() {
        return this.fields.getString("url");
    }

    public String getDesc() {
        return this.fields.getString("desc");
    }

    private void setDesc(String desc) {
        this.fields.put("desc", (Object) desc);
    }

    public String getIcon() {
        return this.fields.getString("icon");
    }

    public void setInfo(String rawInfoJSONString) {
        if (rawInfoJSONString != null && !rawInfoJSONString.isEmpty()) {
            JSONObject root = null;
            try {
                root = JSON.parseObject(rawInfoJSONString);
            } catch (Exception e) {
            }
            if (root != null) {
                JSONObject info = root.getJSONObject("info");
                String desc = root.getString("desc");
                if (info != null) {
                    setInfo(info);
                    if (desc == null) {
                        desc = "";
                    }
                    setDesc(desc);
                    notifyLinkageDelegate();
                }
            }
        }
    }

    private JSONObject getInfo() {
        return this.fields.getJSONObject("info");
    }

    private void setInfo(JSONObject info) {
        this.fields.put("info", (Object) info);
    }

    public String getPostData() {
        return this.data.toJSONString();
    }
}
