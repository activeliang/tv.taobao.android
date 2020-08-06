package com.alibaba.sdk.android.oss.model;

public class GetBucketInfoResult extends OSSResult {
    private OSSBucketSummary bucket;

    public OSSBucketSummary getBucket() {
        return this.bucket;
    }

    public void setBucket(OSSBucketSummary bucket2) {
        this.bucket = bucket2;
    }

    public String toString() {
        return String.format("GetBucketInfoResult<%s>:\n bucket:%s", new Object[]{super.toString(), this.bucket.toString()});
    }
}
