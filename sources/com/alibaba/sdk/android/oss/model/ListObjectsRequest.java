package com.alibaba.sdk.android.oss.model;

public class ListObjectsRequest extends OSSRequest {
    private static final int MAX_RETURNED_KEYS_LIMIT = 1000;
    private String bucketName;
    private String delimiter;
    private String encodingType;
    private String marker;
    private Integer maxKeys;
    private String prefix;

    public ListObjectsRequest() {
        this((String) null);
    }

    public ListObjectsRequest(String bucketName2) {
        this(bucketName2, (String) null, (String) null, (String) null, (Integer) null);
    }

    public ListObjectsRequest(String bucketName2, String prefix2, String marker2, String delimiter2, Integer maxKeys2) {
        setBucketName(bucketName2);
        setPrefix(prefix2);
        setMarker(marker2);
        setDelimiter(delimiter2);
        if (maxKeys2 != null) {
            setMaxKeys(maxKeys2);
        }
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

    public Integer getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys(Integer maxKeys2) {
        if (maxKeys2.intValue() < 0 || maxKeys2.intValue() > 1000) {
            throw new IllegalArgumentException("Maxkeys should less can not exceed 1000.");
        }
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
}
