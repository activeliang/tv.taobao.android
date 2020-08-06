package com.taobao.wireless.trade.mbuy.sdk.co.misc;

public enum OptionStatus {
    NORMAL,
    DISABLE,
    HIDDEN;

    public static OptionStatus getOptionStatusByDesc(String desc) {
        if (desc == null || desc.isEmpty()) {
            return NORMAL;
        }
        if ("disable".equals(desc)) {
            return DISABLE;
        }
        if ("hidden".equals(desc)) {
            return HIDDEN;
        }
        return NORMAL;
    }
}
