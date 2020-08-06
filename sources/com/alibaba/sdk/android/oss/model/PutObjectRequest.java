package com.alibaba.sdk.android.oss.model;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.callback.OSSRetryCallback;
import java.util.Map;

public class PutObjectRequest extends OSSRequest {
    private String bucketName;
    private Map<String, String> callbackParam;
    private Map<String, String> callbackVars;
    private ObjectMetadata metadata;
    private String objectKey;
    private OSSProgressCallback<PutObjectRequest> progressCallback;
    private OSSRetryCallback retryCallback;
    private byte[] uploadData;
    private String uploadFilePath;

    public PutObjectRequest(String bucketName2, String objectKey2, String uploadFilePath2) {
        this(bucketName2, objectKey2, uploadFilePath2, (ObjectMetadata) null);
    }

    public PutObjectRequest(String bucketName2, String objectKey2, String uploadFilePath2, ObjectMetadata metadata2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setUploadFilePath(uploadFilePath2);
        setMetadata(metadata2);
    }

    public PutObjectRequest(String bucketName2, String objectKey2, byte[] uploadData2) {
        this(bucketName2, objectKey2, uploadData2, (ObjectMetadata) null);
    }

    public PutObjectRequest(String bucketName2, String objectKey2, byte[] uploadData2, ObjectMetadata metadata2) {
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setUploadData(uploadData2);
        setMetadata(metadata2);
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

    public String getUploadFilePath() {
        return this.uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath2) {
        this.uploadFilePath = uploadFilePath2;
    }

    public byte[] getUploadData() {
        return this.uploadData;
    }

    public void setUploadData(byte[] uploadData2) {
        this.uploadData = uploadData2;
    }

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }

    public OSSProgressCallback<PutObjectRequest> getProgressCallback() {
        return this.progressCallback;
    }

    public void setProgressCallback(OSSProgressCallback<PutObjectRequest> progressCallback2) {
        this.progressCallback = progressCallback2;
    }

    public OSSRetryCallback getRetryCallback() {
        return this.retryCallback;
    }

    public void setRetryCallback(OSSRetryCallback retryCallback2) {
        this.retryCallback = retryCallback2;
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
}
