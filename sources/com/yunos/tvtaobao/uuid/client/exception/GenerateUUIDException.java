package com.yunos.tvtaobao.uuid.client.exception;

public class GenerateUUIDException extends Exception {
    private int mErrorCode;

    public GenerateUUIDException(int error, String errMsg) {
        super(errMsg);
        this.mErrorCode = error;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }
}
