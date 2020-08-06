package com.taobao.wireless.trade.mbuy.sdk.co.basic;

public enum InputComponentAttr {
    MASK("mask"),
    REQUIRED("required");
    
    public String desc;

    private InputComponentAttr(String desc2) {
        this.desc = desc2;
    }

    public static InputComponentAttr getInputComponentAttrByDesc(String desc2) {
        if (MASK.desc.equals(desc2)) {
            return MASK;
        }
        if (REQUIRED.desc.equals(desc2)) {
            return REQUIRED;
        }
        return null;
    }

    public String toString() {
        return this.desc;
    }
}
