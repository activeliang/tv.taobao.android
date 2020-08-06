package com.ali.auth.third.core.model;

public enum SNSPlatform {
    PLATFORM_ALIPAY3("Alipay3");
    
    private String platform;

    public String getPlatform() {
        return this.platform;
    }

    private SNSPlatform(String p) {
        this.platform = p;
    }
}
