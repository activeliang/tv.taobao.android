package com.taobao.atlas.update.exception;

public class MergeException extends Exception {
    public MergeException() {
    }

    public MergeException(String detailMessage) {
        super(detailMessage);
    }

    public MergeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MergeException(Throwable throwable) {
        super(throwable);
    }
}
