package com.alibaba.sdk.android.oss.model;

public class PutObjectResult extends OSSResult {
    private String eTag;
    private String serverCallbackReturnBody;

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag2) {
        this.eTag = eTag2;
    }

    public String getServerCallbackReturnBody() {
        return this.serverCallbackReturnBody;
    }

    public void setServerCallbackReturnBody(String serverCallbackReturnBody2) {
        this.serverCallbackReturnBody = serverCallbackReturnBody2;
    }
}
