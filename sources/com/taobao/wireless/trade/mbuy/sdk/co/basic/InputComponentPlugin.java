package com.taobao.wireless.trade.mbuy.sdk.co.basic;

public enum InputComponentPlugin {
    EMPTY("empty"),
    CONTACTS("contacts");
    
    public String desc;

    private InputComponentPlugin(String desc2) {
        this.desc = desc2;
    }

    public static InputComponentPlugin getInputComponentPluginByDesc(String desc2) {
        if (CONTACTS.desc.equals(desc2)) {
            return CONTACTS;
        }
        return EMPTY;
    }

    public String toString() {
        return this.desc;
    }
}
