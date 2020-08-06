package com.taobao.wireless.trade.mbuy.sdk.co;

public enum ComponentStatus {
    NORMAL,
    DISABLE,
    HIDDEN;

    public static ComponentStatus getComponentStatusByDesc(String desc) {
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
