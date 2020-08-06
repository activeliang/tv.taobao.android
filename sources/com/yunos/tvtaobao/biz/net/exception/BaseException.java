package com.yunos.tvtaobao.biz.net.exception;

import android.content.Context;

public abstract class BaseException extends Exception {
    protected int code;
    protected String errorMessage;

    public abstract boolean handle(Context context);

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(int code2, String errorMessage2) {
        this.code = code2;
        this.errorMessage = errorMessage2;
    }

    public BaseException(String errorMessage2, Throwable throwable) {
        super(throwable);
        this.errorMessage = errorMessage2;
    }

    public BaseException(int code2, String errorMessage2, Throwable throwable) {
        super(throwable);
        this.code = code2;
        this.errorMessage = errorMessage2;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code2) {
        this.code = code2;
    }

    public String getErrorMessage() {
        if (this.errorMessage != null) {
            return this.errorMessage;
        }
        return super.getMessage();
    }

    public void setErrorMessage(String errorMessage2) {
        this.errorMessage = errorMessage2;
    }
}
