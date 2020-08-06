package com.taobao.wireless.trade.mcart.sdk.co.biz;

public enum CartQueryType {
    QUERYTYPE_ALL(0, "all"),
    QUERYTYPE_STOCK(4, "stock"),
    QUERYTYPE_REDUCE(2, "reduce");
    
    private int code;
    private String desc;

    private CartQueryType(int code2, String desc2) {
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
