package com.alibaba.sdk.android.oss.model;

public class GetBucketACLRequest extends OSSRequest {
    private String bucketName;

    public GetBucketACLRequest(String bucketName2) {
        setBucketName(bucketName2);
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }
}
