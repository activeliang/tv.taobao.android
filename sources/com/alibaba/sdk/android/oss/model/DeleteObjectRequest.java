package com.alibaba.sdk.android.oss.model;

public class DeleteObjectRequest extends OSSRequest {
    private String bucketName;
    private String objectKey;

    public DeleteObjectRequest(String bucketName2, String objectKey2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
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
}
