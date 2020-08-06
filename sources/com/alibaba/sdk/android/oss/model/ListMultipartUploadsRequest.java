package com.alibaba.sdk.android.oss.model;

public class ListMultipartUploadsRequest extends OSSRequest {
    private String bucketName;
    private String delimiter;
    private String encodingType;
    private String keyMarker;
    private Integer maxUploads;
    private String prefix;
    private String uploadIdMarker;

    public ListMultipartUploadsRequest(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public Integer getMaxUploads() {
        return this.maxUploads;
    }

    public void setMaxUploads(Integer maxUploads2) {
        this.maxUploads = maxUploads2;
    }

    public String getKeyMarker() {
        return this.keyMarker;
    }

    public void setKeyMarker(String keyMarker2) {
        this.keyMarker = keyMarker2;
    }

    public String getUploadIdMarker() {
        return this.uploadIdMarker;
    }

    public void setUploadIdMarker(String uploadIdMarker2) {
        this.uploadIdMarker = uploadIdMarker2;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter2) {
        this.delimiter = delimiter2;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix2) {
        this.prefix = prefix2;
    }

    public String getEncodingType() {
        return this.encodingType;
    }

    public void setEncodingType(String encodingType2) {
        this.encodingType = encodingType2;
    }
}
