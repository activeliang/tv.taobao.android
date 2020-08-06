package com.yunos.tv.blitz.global;

import com.taobao.wireless.detail.DetailConfig;

public enum BzEnvEnum {
    ONLINE(0, DetailConfig.ONLINE),
    PRE(1, DetailConfig.WAPA),
    DAILY(2, DetailConfig.WAPTEST);
    
    private int key;
    private String value;

    private BzEnvEnum(int key2, String value2) {
        this.key = key2;
        this.value = value2;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key2) {
        this.key = key2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
