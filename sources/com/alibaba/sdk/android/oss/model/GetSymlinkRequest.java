package com.alibaba.sdk.android.oss.model;

public class GetSymlinkRequest extends OSSRequest {
    private String bucketName;
    private String objectKey;

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
}
