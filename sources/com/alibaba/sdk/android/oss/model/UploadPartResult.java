package com.alibaba.sdk.android.oss.model;

public class UploadPartResult extends OSSResult {
    private String eTag;

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag2) {
        this.eTag = eTag2;
    }
}
