package com.yunos.tv.tvsdk.media.error;

public enum ErrorType {
    AUTH_ERROR(0),
    DATA_ERROR(1),
    NETWORK_ERROR(7),
    SERVER_ERROR(8),
    NATIVE_PLAYER_ERROR(2),
    SYSTEM_PLAYER_ERROR(3),
    ADO_PLAYER_ERROR(6),
    ACCOUNT_ERROR(4),
    SPECIAL_PLAYER_EVENT(5);
    
    private int mErrorType;

    private ErrorType(int type) {
        this.mErrorType = type;
    }

    public int getErrorType() {
        return this.mErrorType;
    }
}
