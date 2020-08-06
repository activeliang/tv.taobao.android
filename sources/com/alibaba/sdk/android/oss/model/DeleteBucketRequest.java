package com.alibaba.sdk.android.oss.model;

public class DeleteBucketRequest extends OSSRequest {
    private String bucketName;

    public DeleteBucketRequest(String bucketName2) {
        setBucketName(bucketName2);
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }
}
