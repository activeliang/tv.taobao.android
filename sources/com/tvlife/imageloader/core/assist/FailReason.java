package com.tvlife.imageloader.core.assist;

public class FailReason {
    private final Throwable cause;
    private final FailType type;

    public enum FailType {
        IO_ERROR,
        DECODING_ERROR,
        NETWORK_DENIED,
        OUT_OF_MEMORY,
        THREADPOOL_REJECTED,
        BITMAP_RECYCLE,
        UNKNOWN
    }

    public FailReason(FailType type2, Throwable cause2) {
        this.type = type2;
        this.cause = cause2;
    }

    public FailType getType() {
        return this.type;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
