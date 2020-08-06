package com.alibaba.sdk.android.oss.model;

public class ListPartsRequest extends OSSRequest {
    private String bucketName;
    private Integer maxParts;
    private String objectKey;
    private Integer partNumberMarker;
    private String uploadId;

    public ListPartsRequest(String bucketName2, String objectKey2, String uploadId2) {
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

    public Integer getMaxParts() {
        return this.maxParts;
    }

    public void setMaxParts(int maxParts2) {
        this.maxParts = Integer.valueOf(maxParts2);
    }

    public Integer getPartNumberMarker() {
        return this.partNumberMarker;
    }

    public void setPartNumberMarker(Integer partNumberMarker2) {
        this.partNumberMarker = partNumberMarker2;
    }
}
