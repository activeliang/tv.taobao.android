package com.alibaba.sdk.android.oss.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompleteMultipartUploadRequest extends OSSRequest {
    private String bucketName;
    private Map<String, String> callbackParam;
    private Map<String, String> callbackVars;
    private ObjectMetadata metadata;
    private String objectKey;
    private List<PartETag> partETags = new ArrayList();
    private String uploadId;

    public CompleteMultipartUploadRequest(String bucketName2, String objectKey2, String uploadId2, List<PartETag> partETags2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setUploadId(uploadId2);
        setPartETags(partETags2);
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

    public List<PartETag> getPartETags() {
        return this.partETags;
    }

    public void setPartETags(List<PartETag> partETags2) {
        this.partETags = partETags2;
    }

    public Map<String, String> getCallbackParam() {
        return this.callbackParam;
    }

    public void setCallbackParam(Map<String, String> callbackParam2) {
        this.callbackParam = callbackParam2;
    }

    public Map<String, String> getCallbackVars() {
        return this.callbackVars;
    }

    public void setCallbackVars(Map<String, String> callbackVars2) {
        this.callbackVars = callbackVars2;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }
}
