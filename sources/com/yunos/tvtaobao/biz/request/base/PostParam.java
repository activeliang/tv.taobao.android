package com.yunos.tvtaobao.biz.request.base;

import anetwork.channel.Param;

public class PostParam implements Param {
    private String key = "";
    private String value = "";

    public void setKey(String key2) {
        this.key = key2;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public PostParam(String key2, String value2) {
        this.key = key2;
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
