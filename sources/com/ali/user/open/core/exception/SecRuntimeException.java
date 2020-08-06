package com.ali.user.open.core.exception;

public class SecRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -2429061914258524220L;
    private int secCode;

    public SecRuntimeException(int secCode2, Throwable t) {
        super(t);
        this.secCode = secCode2;
    }

    public int getErrorCode() {
        return this.secCode;
    }

    public String getMessage() {
        return super.getMessage() + " secCode = " + this.secCode;
    }
}
