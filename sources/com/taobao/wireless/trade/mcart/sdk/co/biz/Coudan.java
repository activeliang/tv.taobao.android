package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;

public class Coudan {
    private JSONObject data;

    public Coudan(JSONObject data2) {
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

    public String getSubTitle() {
        return this.data.getString("subTitle");
    }

    public String getUrl() {
        return this.data.getString("url");
    }

    public String getSubUrl() {
        return this.data.getString("subTitleUrl");
    }

    public String getNextTitle() {
        return this.data.getString("nextTitle");
    }

    public void updateKey(String key, Object value) {
        if (value != null) {
            this.data.put(key, value);
        } else {
            this.data.remove(key);
        }
    }

    public String toString() {
        return "Coudan [title=" + getTitle() + ", url=" + getUrl() + ", nextTitle=" + getNextTitle() + "]";
    }
}
