package com.taobao.wireless.trade.mcart.sdk.co.biz;

public enum BizIconType {
    ITEM_REGION("S"),
    PRICE_REGION("P");
    
    private String code;

    private BizIconType(String code2) {
        this.code = code2;
    }

    public String getCode() {
        return this.code;
    }
}
