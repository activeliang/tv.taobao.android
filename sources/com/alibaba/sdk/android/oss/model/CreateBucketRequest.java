package com.alibaba.sdk.android.oss.model;

public class CreateBucketRequest extends OSSRequest {
    public static final String TAB_LOCATIONCONSTRAINT = "LocationConstraint";
    public static final String TAB_STORAGECLASS = "StorageClass";
    private CannedAccessControlList bucketACL;
    private String bucketName;
    private StorageClass bucketStorageClass = StorageClass.Standard;
    private String locationConstraint;

    public CreateBucketRequest(String bucketName2) {
        setBucketName(bucketName2);
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    @Deprecated
    public String getLocationConstraint() {
        return this.locationConstraint;
    }

    @Deprecated
    public void setLocationConstraint(String locationConstraint2) {
        this.locationConstraint = locationConstraint2;
    }

    public CannedAccessControlList getBucketACL() {
        return this.bucketACL;
    }

    public void setBucketACL(CannedAccessControlList bucketACL2) {
        this.bucketACL = bucketACL2;
    }

    public StorageClass getBucketStorageClass() {
        return this.bucketStorageClass;
    }

    public void setBucketStorageClass(StorageClass storageClass) {
        this.bucketStorageClass = storageClass;
    }
}
