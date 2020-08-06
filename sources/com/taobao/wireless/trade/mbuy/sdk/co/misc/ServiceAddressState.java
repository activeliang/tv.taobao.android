package com.taobao.wireless.trade.mbuy.sdk.co.misc;

public enum ServiceAddressState {
    NOT_SELECTED(0, "请选择安装地址"),
    SAME_ADDRESS(1, "与收货地址一致"),
    DIFF_ADDRESS(2, ""),
    PARTLY_SUPPORTED(3, "仅对支持使用上门安装服务的商品生效");
    
    private int code;
    private String desc;

    private ServiceAddressState(int code2, String desc2) {
        this.code = code2;
        this.desc = desc2;
    }

    public static ServiceAddressState getStateByCode(int code2) {
        switch (code2) {
            case 0:
                return NOT_SELECTED;
            case 1:
                return SAME_ADDRESS;
            case 2:
                return DIFF_ADDRESS;
            case 3:
                return PARTLY_SUPPORTED;
            default:
                return NOT_SELECTED;
        }
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
