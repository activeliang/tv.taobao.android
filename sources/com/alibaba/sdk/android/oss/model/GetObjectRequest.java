package com.alibaba.sdk.android.oss.model;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import java.util.Map;

public class GetObjectRequest extends OSSRequest {
    private String bucketName;
    private String objectKey;
    private OSSProgressCallback progressListener;
    private Range range;
    private Map<String, String> requestHeaders;
    private String xOssProcess;

    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders2) {
        this.requestHeaders = requestHeaders2;
    }

    public GetObjectRequest(String bucketName2, String objectKey2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
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

    public Range getRange() {
        return this.range;
    }

    public void setRange(Range range2) {
        this.range = range2;
    }

    public String getxOssProcess() {
        return this.xOssProcess;
    }

    public void setxOssProcess(String xOssProcess2) {
        this.xOssProcess = xOssProcess2;
    }

    public OSSProgressCallback getProgressListener() {
        return this.progressListener;
    }

    public void setProgressListener(OSSProgressCallback<GetObjectRequest> progressListener2) {
        this.progressListener = progressListener2;
    }
}
