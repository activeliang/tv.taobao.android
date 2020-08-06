package com.taobao.wireless.trade.mbuy.sdk.co;

import java.util.HashMap;
import java.util.Map;

public enum ComponentType {
    BIZ(0, "biz"),
    SYNTHETIC(1, "synthetic"),
    DYNAMIC(2, "dynamic"),
    LABEL(3, "label"),
    INPUT(4, "input"),
    SELECT(5, "select"),
    TOGGLE(6, "toggle"),
    MULTISELECT(7, "multiSelect"),
    TABLE(8, "table"),
    TIPS(9, "tips"),
    DATEPICKER(10, "datePicker"),
    CASCADE(11, "cascade"),
    BRIDGE(12, "bridge"),
    EXPAND(13, "expand"),
    FLOATTIPS(14, "floatTips"),
    VERIFICATION_CODE(15, "verificationCode"),
    RICHSELECT(16, "richSelect"),
    CARDDECK(18, "cardDeck"),
    UNKNOWN(17, "unknown");
    
    private static Map<String, ComponentType> m;
    public String desc;
    public int index;

    static {
        int i;
        m = new HashMap();
        for (ComponentType type : values()) {
            m.put(type.desc, type);
        }
    }

    public static ComponentType getComponentTypeByDesc(String desc2) {
        ComponentType type = m.get(desc2);
        return type != null ? type : UNKNOWN;
    }

    public static int size() {
        return values().length;
    }

    private ComponentType(int index2, String desc2) {
        this.index = index2;
        this.desc = desc2;
    }

    public String toString() {
        return this.desc;
    }
}
