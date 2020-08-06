package com.taobao.wireless.trade.mbuy.sdk.co.misc;

import com.alibaba.fastjson.JSONObject;

public class ServiceAddressItem {
    private JSONObject data;

    public ServiceAddressItem(JSONObject data2) {
        if (data2 == null) {
            throw new IllegalStateException();
        }
        this.data = data2;
    }

    public String getPic() {
        return this.data.getString("pic");
    }

    public String getTitle() {
        return this.data.getString("title");
    }
}
