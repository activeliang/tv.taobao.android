package com.alibaba.sdk.android.oss.exception;

import java.io.IOException;

public class InconsistentException extends IOException {
    private Long clientChecksum;
    private String requestId;
    private Long serverChecksum;

    public InconsistentException(Long clientChecksum2, Long serverChecksum2, String requestId2) {
        this.clientChecksum = clientChecksum2;
        this.serverChecksum = serverChecksum2;
        this.requestId = requestId2;
    }

    public String getMessage() {
        return "InconsistentException: inconsistent object\n[RequestId]: " + this.requestId + "\n[ClientChecksum]: " + this.clientChecksum + "\n[ServerChecksum]: " + this.serverChecksum;
    }
}
