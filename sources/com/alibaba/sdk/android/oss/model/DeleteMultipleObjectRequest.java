package com.alibaba.sdk.android.oss.model;

import java.util.List;

public class DeleteMultipleObjectRequest extends OSSRequest {
    private String bucketName;
    private boolean isQuiet;
    private List<String> objectKeys;

    public DeleteMultipleObjectRequest(String bucketName2, List<String> objectKeys2, Boolean isQuiet2) {
        setBucketName(bucketName2);
        setObjectKeys(objectKeys2);
        setQuiet(isQuiet2);
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public List<String> getObjectKeys() {
        return this.objectKeys;
    }

    public void setObjectKeys(List<String> objectKeys2) {
        this.objectKeys = objectKeys2;
    }

    public Boolean getQuiet() {
        return Boolean.valueOf(this.isQuiet);
    }

    public void setQuiet(Boolean isQuiet2) {
        this.isQuiet = isQuiet2.booleanValue();
    }
}
