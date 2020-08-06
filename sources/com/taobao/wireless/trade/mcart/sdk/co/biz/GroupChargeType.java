package com.taobao.wireless.trade.mcart.sdk.co.biz;

public enum GroupChargeType {
    BC("B&C", "普通商品", 0),
    SM("SM", "天猫超市", 1),
    HK("HK", "天猫国际", 2),
    HKDF("HKDF", "天猫国际免税店", 3),
    YY("YY", "医药馆", 4),
    ALITRIP("ALITRIP", "飞猪商品", 5);
    
    private String code;
    private int priority;
    private String title;

    private GroupChargeType(String code2, String title2, int priority2) {
        this.code = code2;
        this.title = title2;
        this.priority = priority2;
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPriority() {
        return this.priority;
    }
}
