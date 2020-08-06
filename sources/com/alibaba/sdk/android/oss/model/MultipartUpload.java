package com.alibaba.sdk.android.oss.model;

import java.util.Date;

public class MultipartUpload {
    private Date initiated;
    private String key;
    private String storageClass;
    private String uploadId;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(String uploadId2) {
        this.uploadId = uploadId2;
    }

    public String getStorageClass() {
        return this.storageClass;
    }

    public void setStorageClass(String storageClass2) {
        this.storageClass = storageClass2;
    }

    public Date getInitiated() {
        return this.initiated;
    }

    public void setInitiated(Date initiated2) {
        this.initiated = initiated2;
    }
}
