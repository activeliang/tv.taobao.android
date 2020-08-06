package com.alibaba.sdk.android.oss.model;

import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import java.io.File;

public class ResumableUploadRequest extends MultipartUploadRequest {
    private Boolean deleteUploadOnCancelling;
    private String recordDirectory;

    public ResumableUploadRequest(String bucketName, String objectKey, String uploadFilePath) {
        this(bucketName, objectKey, uploadFilePath, (ObjectMetadata) null, (String) null);
    }

    public ResumableUploadRequest(String bucketName, String objectKey, String uploadFilePath, ObjectMetadata metadata) {
        this(bucketName, objectKey, uploadFilePath, metadata, (String) null);
    }

    public ResumableUploadRequest(String bucketName, String objectKey, String uploadFilePath, String recordDirectory2) {
        this(bucketName, objectKey, uploadFilePath, (ObjectMetadata) null, recordDirectory2);
    }

    public ResumableUploadRequest(String bucketName, String objectKey, String uploadFilePath, ObjectMetadata metadata, String recordDirectory2) {
        super(bucketName, objectKey, uploadFilePath, metadata);
        this.deleteUploadOnCancelling = true;
        setRecordDirectory(recordDirectory2);
    }

    public String getRecordDirectory() {
        return this.recordDirectory;
    }

    public void setRecordDirectory(String recordDirectory2) {
        if (!OSSUtils.isEmptyString(recordDirectory2)) {
            File file = new File(recordDirectory2);
            if (!file.exists() || !file.isDirectory()) {
                throw new IllegalArgumentException("Record directory must exist, and it should be a directory!");
            }
        }
        this.recordDirectory = recordDirectory2;
    }

    public Boolean deleteUploadOnCancelling() {
        return this.deleteUploadOnCancelling;
    }

    public void setDeleteUploadOnCancelling(Boolean deleteUploadOnCancelling2) {
        this.deleteUploadOnCancelling = deleteUploadOnCancelling2;
    }
}
