package com.alibaba.sdk.android.oss.model;

import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import java.util.Map;

public class MultipartUploadRequest<T extends MultipartUploadRequest> extends OSSRequest {
    protected String bucketName;
    protected Map<String, String> callbackParam;
    protected Map<String, String> callbackVars;
    protected ObjectMetadata metadata;
    protected String objectKey;
    protected long partSize;
    protected OSSProgressCallback<T> progressCallback;
    protected String uploadFilePath;
    protected String uploadId;

    public MultipartUploadRequest(String bucketName2, String objectKey2, String uploadFilePath2) {
        this(bucketName2, objectKey2, uploadFilePath2, (ObjectMetadata) null);
    }

    public MultipartUploadRequest(String bucketName2, String objectKey2, String uploadFilePath2, ObjectMetadata metadata2) {
        this.partSize = PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
        setBucketName(bucketName2);
        setObjectKey(objectKey2);
        setUploadFilePath(uploadFilePath2);
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

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }

    public OSSProgressCallback<T> getProgressCallback() {
        return this.progressCallback;
    }

    public void setProgressCallback(OSSProgressCallback<T> progressCallback2) {
        this.progressCallback = progressCallback2;
    }

    public long getPartSize() {
        return this.partSize;
    }

    public void setPartSize(long partSize2) {
        this.partSize = partSize2;
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

    public String getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(String uploadId2) {
        this.uploadId = uploadId2;
    }
}
