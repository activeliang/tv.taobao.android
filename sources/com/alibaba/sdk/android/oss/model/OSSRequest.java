package com.alibaba.sdk.android.oss.model;

public class OSSRequest {
    private Enum CRC64 = CRC64Config.NULL;
    private boolean isAuthorizationRequired = true;

    public enum CRC64Config {
        NULL,
        YES,
        NO
    }

    public boolean isAuthorizationRequired() {
        return this.isAuthorizationRequired;
    }

    public void setIsAuthorizationRequired(boolean isAuthorizationRequired2) {
        this.isAuthorizationRequired = isAuthorizationRequired2;
    }

    public Enum getCRC64() {
        return this.CRC64;
    }

    public void setCRC64(Enum CRC642) {
        this.CRC64 = CRC642;
    }
}
