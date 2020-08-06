package com.alibaba.sdk.android.oss;

import com.alibaba.sdk.android.oss.common.OSSLog;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 430933593095358673L;
    private String errorCode;
    private String hostId;
    private String partEtag;
    private String partNumber;
    private String rawMessage;
    private String requestId;
    private int statusCode;

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(String partNumber2) {
        this.partNumber = partNumber2;
    }

    public String getPartEtag() {
        return this.partEtag;
    }

    public void setPartEtag(String partEtag2) {
        this.partEtag = partEtag2;
    }

    public ServiceException(int statusCode2, String message, String errorCode2, String requestId2, String hostId2, String rawMessage2) {
        super(message);
        this.statusCode = statusCode2;
        this.errorCode = errorCode2;
        this.requestId = requestId2;
        this.hostId = hostId2;
        this.rawMessage = rawMessage2;
        OSSLog.logThrowable2Local(this);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getHostId() {
        return this.hostId;
    }

    public String toString() {
        return "[StatusCode]: " + this.statusCode + ", [Code]: " + getErrorCode() + ", [Message]: " + getMessage() + ", [Requestid]: " + getRequestId() + ", [HostId]: " + getHostId() + ", [RawMessage]: " + getRawMessage();
    }

    public String getRawMessage() {
        return this.rawMessage;
    }
}
