package com.taobao.wireless.trade.mbuy.sdk.co.basic;

public enum TipsType {
    UNKNOWN("unknown"),
    URL("url"),
    HTML("html"),
    IMG("img");
    
    public String desc;

    public static TipsType getTipsTypeByDesc(String desc2) {
        if (desc2 == null) {
            return UNKNOWN;
        }
        if (desc2.equals(HTML.desc)) {
            return HTML;
        }
        if (desc2.equals(URL.desc)) {
            return URL;
        }
        if (desc2.equals(IMG.desc)) {
            return IMG;
        }
        return UNKNOWN;
    }

    private TipsType(String desc2) {
        this.desc = desc2;
    }

    public String toString() {
        return this.desc;
    }
}
