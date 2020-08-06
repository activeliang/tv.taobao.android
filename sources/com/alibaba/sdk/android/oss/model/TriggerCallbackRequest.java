package com.alibaba.sdk.android.oss.model;

import java.util.Map;

public class TriggerCallbackRequest extends OSSRequest {
    private String mBucketName;
    private Map<String, String> mCallbackParam;
    private Map<String, String> mCallbackVars;
    private String mObjectKey;

    public TriggerCallbackRequest(String bucketName, String objectKey, Map<String, String> callbackParam, Map<String, String> callbackVars) {
        setBucketName(bucketName);
        setObjectKey(objectKey);
        setCallbackParam(callbackParam);
        setCallbackVars(callbackVars);
    }

    public String getBucketName() {
        return this.mBucketName;
    }

    public void setBucketName(String bucketName) {
        this.mBucketName = bucketName;
    }

    public String getObjectKey() {
        return this.mObjectKey;
    }

    public void setObjectKey(String objectKey) {
        this.mObjectKey = objectKey;
    }

    public Map<String, String> getCallbackParam() {
        return this.mCallbackParam;
    }

    public void setCallbackParam(Map<String, String> callbackParam) {
        this.mCallbackParam = callbackParam;
    }

    public Map<String, String> getCallbackVars() {
        return this.mCallbackVars;
    }

    public void setCallbackVars(Map<String, String> callbackVars) {
        this.mCallbackVars = callbackVars;
    }
}
