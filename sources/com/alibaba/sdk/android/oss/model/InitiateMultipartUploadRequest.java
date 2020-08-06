package com.alibaba.sdk.android.oss.model;

public class InitiateMultipartUploadRequest extends OSSRequest {
    private String bucketName;
    public boolean isSequential;
    private ObjectMetadata metadata;
    private String objectKey;

    public InitiateMultipartUploadRequest(String bucketName2, String objectKey2) {
        this(bucketName2, objectKey2, (ObjectMetadata) null);
    }

    public InitiateMultipartUploadRequest(String bucketName2, String objectKey2, ObjectMetadata metadata2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setMetadata(metadata2);
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

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }
}
