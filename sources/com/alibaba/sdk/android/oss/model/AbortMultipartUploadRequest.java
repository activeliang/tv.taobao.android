package com.alibaba.sdk.android.oss.model;

public class AbortMultipartUploadRequest extends OSSRequest {
    private String bucketName;
    private String objectKey;
    private String uploadId;

    public AbortMultipartUploadRequest(String bucketName2, String objectKey2, String uploadId2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setUploadId(uploadId2);
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

    public String getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(String uploadId2) {
        this.uploadId = uploadId2;
    }
}
