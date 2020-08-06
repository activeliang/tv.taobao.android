package com.alibaba.sdk.android.oss.model;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;

public class UploadPartRequest extends OSSRequest {
    private String bucketName;
    private String md5Digest;
    private String objectKey;
    private byte[] partContent;
    private int partNumber;
    private OSSProgressCallback<UploadPartRequest> progressCallback;
    private String uploadId;

    public UploadPartRequest() {
    }

    public UploadPartRequest(String bucketName2, String objectKey2, String uploadId2, int partNumber2) {
        this.bucketName = bucketName2;
        this.objectKey = objectKey2;
        this.uploadId = uploadId2;
        this.partNumber = partNumber2;
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

    public int getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(int partNumber2) {
        this.partNumber = partNumber2;
    }

    public String getMd5Digest() {
        return this.md5Digest;
    }

    public void setMd5Digest(String md5Digest2) {
        this.md5Digest = md5Digest2;
    }

    public OSSProgressCallback<UploadPartRequest> getProgressCallback() {
        return this.progressCallback;
    }

    public void setProgressCallback(OSSProgressCallback<UploadPartRequest> progressCallback2) {
        this.progressCallback = progressCallback2;
    }

    public byte[] getPartContent() {
        return this.partContent;
    }

    public void setPartContent(byte[] partContent2) {
        this.partContent = partContent2;
    }
}
