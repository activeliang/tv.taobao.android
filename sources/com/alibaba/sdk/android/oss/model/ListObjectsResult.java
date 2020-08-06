package com.alibaba.sdk.android.oss.model;

import java.util.ArrayList;
import java.util.List;

public class ListObjectsResult extends OSSResult {
    private String bucketName;
    private List<String> commonPrefixes = new ArrayList();
    private String delimiter;
    private String encodingType;
    private boolean isTruncated;
    private String marker;
    private int maxKeys;
    private String nextMarker;
    private List<OSSObjectSummary> objectSummaries = new ArrayList();
    private String prefix;

    public List<OSSObjectSummary> getObjectSummaries() {
        return this.objectSummaries;
    }

    public void addObjectSummary(OSSObjectSummary objectSummary) {
        this.objectSummaries.add(objectSummary);
    }

    public void clearObjectSummaries() {
        this.objectSummaries.clear();
    }

    public List<String> getCommonPrefixes() {
        return this.commonPrefixes;
    }

    public void addCommonPrefix(String commonPrefix) {
        this.commonPrefixes.add(commonPrefix);
    }

    public void clearCommonPrefixes() {
        this.commonPrefixes.clear();
    }

    public String getNextMarker() {
        return this.nextMarker;
    }

    public void setNextMarker(String nextMarker2) {
        this.nextMarker = nextMarker2;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
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

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter2) {
        this.delimiter = delimiter2;
    }

    public String getEncodingType() {
        return this.encodingType;
    }

    public void setEncodingType(String encodingType2) {
        this.encodingType = encodingType2;
    }

    public boolean isTruncated() {
        return this.isTruncated;
    }

    public void setTruncated(boolean isTruncated2) {
        this.isTruncated = isTruncated2;
    }
}
