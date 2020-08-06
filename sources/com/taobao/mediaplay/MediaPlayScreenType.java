package com.taobao.mediaplay;

public enum MediaPlayScreenType {
    NORMAL("normal"),
    PORTRAIT_FULL_SCREEN("portrait"),
    LANDSCAPE_FULL_SCREEN("landscape");
    
    private String value;

    private MediaPlayScreenType(String name) {
        this.value = name;
    }

    public String getValue() {
        return this.value;
    }
}
