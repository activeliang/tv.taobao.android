package com.alibaba.sdk.android.oss.model;

import java.util.Date;

public class OSSObjectSummary {
    private String bucketName;
    private String eTag;
    private String key;
    private Date lastModified;
    private Owner owner;
    private long size;
    private String storageClass;
    private String type;

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag2) {
        this.eTag = eTag2;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size2) {
        this.size = size2;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(Date lastModified2) {
        this.lastModified = lastModified2;
    }

    public String getStorageClass() {
        return this.storageClass;
    }

    public void setStorageClass(String storageClass2) {
        this.storageClass = storageClass2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner2) {
        this.owner = owner2;
    }
}
