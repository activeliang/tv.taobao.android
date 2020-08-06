package com.taobao.wireless.trade.mcart.sdk.utils;

public enum NetType {
    NET_TYPE_UNKNOW(0, "unknow"),
    NET_TYPE_2G(2, "2g"),
    NET_TYPE_3G(3, "3g"),
    NET_TYPE_WIFI(1, "wifi"),
    NET_TYPE_23G(4, "23g");
    
    private int code;
    private String desc;

    private NetType(int code2, String desc2) {
        this.code = code2;
        this.desc = desc2;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String toString() {
        return this.desc;
    }
}
