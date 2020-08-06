package com.alibaba.sdk.android.oss.model;

import java.util.Map;

public class OSSResult {
    private Long clientCRC;
    private String requestId;
    private Map<String, String> responseHeader;
    private Long serverCRC;
    private int statusCode;

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode2) {
        this.statusCode = statusCode2;
    }

    public Map<String, String> getResponseHeader() {
        return this.responseHeader;
    }

    public void setResponseHeader(Map<String, String> responseHeader2) {
        this.responseHeader = responseHeader2;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId2) {
        this.requestId = requestId2;
    }

    public Long getClientCRC() {
        return this.clientCRC;
    }

    public void setClientCRC(Long clientCRC2) {
        if (clientCRC2 != null && clientCRC2.longValue() != 0) {
            this.clientCRC = clientCRC2;
        }
    }

    public Long getServerCRC() {
        return this.serverCRC;
    }

    public void setServerCRC(Long serverCRC2) {
        if (serverCRC2 != null && serverCRC2.longValue() != 0) {
            this.serverCRC = serverCRC2;
        }
    }

    public String toString() {
        return String.format("OSSResult<%s>: \nstatusCode:%d,\nresponseHeader:%s,\nrequestId:%s", new Object[]{super.toString(), Integer.valueOf(this.statusCode), this.responseHeader.toString(), this.requestId});
    }
}
