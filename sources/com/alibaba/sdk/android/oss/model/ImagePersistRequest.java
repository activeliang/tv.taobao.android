package com.alibaba.sdk.android.oss.model;

public class ImagePersistRequest extends OSSRequest {
    public String mAction;
    public String mFromBucket;
    public String mFromObjectkey;
    public String mToBucketName;
    public String mToObjectKey;

    public ImagePersistRequest(String fromBucket, String fromObjectKey, String toBucketName, String mToObjectKey2, String action) {
        this.mFromBucket = fromBucket;
        this.mFromObjectkey = fromObjectKey;
        this.mToBucketName = toBucketName;
        this.mToObjectKey = mToObjectKey2;
        this.mAction = action;
    }
}
