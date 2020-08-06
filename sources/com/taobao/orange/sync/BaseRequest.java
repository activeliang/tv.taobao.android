package com.taobao.orange.sync;

public abstract class BaseRequest<T> {
    public static final int CODE_INVAILD = -4;
    public static final int CODE_MD5 = -2;
    public static final int CODE_PRASE = -3;
    public static final int CODE_UNKNOW = -1;
    protected int code = -1;
    protected String message;

    public abstract T syncRequest();

    public void setCode(int code2) {
        this.code = code2;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getCode() {
        return String.valueOf(this.code);
    }

    public String getMessage() {
        return this.message;
    }
}
