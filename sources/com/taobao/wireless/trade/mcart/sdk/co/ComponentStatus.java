package com.taobao.wireless.trade.mcart.sdk.co;

public enum ComponentStatus {
    NORMAL,
    DISABLE,
    HIDDE;

    public static ComponentStatus getComponentStatusByDesc(String desc) {
        if (desc == null || desc.isEmpty()) {
            return NORMAL;
        }
        if ("disable".equals(desc)) {
            return DISABLE;
        }
        if ("hidden".equals(desc)) {
            return HIDDE;
        }
        return NORMAL;
    }
}
