package com.alibaba.sdk.android.oss.model;

public class PutSymlinkRequest extends OSSRequest {
    private String bucketName;
    private ObjectMetadata metadata;
    private String objectKey;
    private String targetObjectName;

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

    public String getTargetObjectName() {
        return this.targetObjectName;
    }

    public void setTargetObjectName(String targetObjectName2) {
        this.targetObjectName = targetObjectName2;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }
}
