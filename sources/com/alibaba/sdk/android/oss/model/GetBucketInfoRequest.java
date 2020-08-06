package com.alibaba.sdk.android.oss.model;

public class GetBucketInfoRequest extends OSSRequest {
    private String bucketName;

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public GetBucketInfoRequest(String bucketName2) {
        this.bucketName = bucketName2;
    }
}
