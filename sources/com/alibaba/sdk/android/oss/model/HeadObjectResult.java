package com.alibaba.sdk.android.oss.model;

public class HeadObjectResult extends OSSResult {
    private ObjectMetadata metadata = new ObjectMetadata();

    public ObjectMetadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(ObjectMetadata metadata2) {
        this.metadata = metadata2;
    }

    public String toString() {
        return String.format("HeadObjectResult<%s>:\n metadata:%s", new Object[]{super.toString(), this.metadata.toString()});
    }
}
