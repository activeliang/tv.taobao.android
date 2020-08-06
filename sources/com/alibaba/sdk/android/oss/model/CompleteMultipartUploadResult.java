package com.alibaba.sdk.android.oss.model;

public class CompleteMultipartUploadResult extends OSSResult {
    private String bucketName;
    private String eTag;
    private String location;
    private String objectKey;
    private String serverCallbackReturnBody;

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getObjectKey() {
        return this.objectKey;
    }

    public void setObjectKey(String objectKey2) {
        this.objectKey = objectKey2;
    }

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String etag) {
        this.eTag = etag;
    }

    public String getServerCallbackReturnBody() {
        return this.serverCallbackReturnBody;
    }

    public void setServerCallbackReturnBody(String serverCallbackReturnBody2) {
        this.serverCallbackReturnBody = serverCallbackReturnBody2;
    }
}
