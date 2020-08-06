package com.taobao.atlas.dexmerge;

public class MergeException extends Exception {
    private transient Throwable throwable;

    public MergeException(String msg, Throwable throwable2) {
        super(msg, throwable2);
        this.throwable = throwable2;
    }

    public MergeException(String msg) {
        super(msg);
        this.throwable = null;
    }

    public Throwable getNestedException() {
        return this.throwable;
    }
}
