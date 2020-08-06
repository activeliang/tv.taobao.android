package com.taobao.wireless.trade.mcart.sdk.co;

import java.util.HashMap;
import java.util.Map;

public enum ComponentTag {
    UNKOWN(0, "unkown"),
    BANNER(1, "banner"),
    ALL_ITEM(2, "allItemv2"),
    BUNDLE(3, "bundlev2"),
    PROMOTION(4, "promotion"),
    SHOP(5, "shopv2"),
    GROUP(7, "group"),
    ITEM(8, "itemv2"),
    FOOTER(9, "footer"),
    EXHIBITIONBAR(10, "exhibitionbar"),
    LABEL(11, "label"),
    PROMOTIONBAR(12, "promotionBar"),
    FOLDINGBAR(13, "foldingBar");
    
    private static Map<String, ComponentTag> m;
    private int code;
    private String desc;

    static {
        int i;
        m = new HashMap();
        for (ComponentTag tag : values()) {
            m.put(tag.desc, tag);
        }
    }

    public static ComponentTag getComponentTagByDesc(String desc2) {
        ComponentTag tag = m.get(desc2);
        return tag != null ? tag : UNKOWN;
    }

    public static int size() {
        return m.size();
    }

    private ComponentTag(int code2, String desc2) {
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
