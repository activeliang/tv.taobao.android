package com.alibaba.sdk.android.oss.model;

import java.util.ArrayList;
import java.util.List;

public class ListBucketsResult extends OSSResult {
    private List<OSSBucketSummary> buckets = new ArrayList();
    private boolean isTruncated;
    private String marker;
    private int maxKeys;
    private String nextMarker;
    private String ownerDisplayName;
    private String ownerId;
    private String prefix;

    public void addBucket(OSSBucketSummary bucket) {
        this.buckets.add(bucket);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix2) {
        this.prefix = prefix2;
    }

    public String getMarker() {
        return this.marker;
    }

    public void setMarker(String marker2) {
        this.marker = marker2;
    }

    public int getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys(int maxKeys2) {
        this.maxKeys = maxKeys2;
    }

    public boolean getTruncated() {
        return this.isTruncated;
    }

    public void setTruncated(boolean isTruncated2) {
        this.isTruncated = isTruncated2;
    }

    public String getNextMarker() {
        return this.nextMarker;
    }

    public void setNextMarker(String nextMarker2) {
        this.nextMarker = nextMarker2;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId2) {
        this.ownerId = ownerId2;
    }

    public String getOwnerDisplayName() {
        return this.ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName2) {
        this.ownerDisplayName = ownerDisplayName2;
    }

    public List<OSSBucketSummary> getBuckets() {
        return this.buckets;
    }

    public void setBuckets(List<OSSBucketSummary> buckets2) {
        this.buckets = buckets2;
    }

    public void clearBucketList() {
        this.buckets.clear();
    }
}
