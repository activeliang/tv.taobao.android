package com.alibaba.sdk.android.oss.model;

public class ListBucketsRequest extends OSSRequest {
    private static final int MAX_RETURNED_KEYS_LIMIT = 1000;
    private String marker;
    private Integer maxKeys;
    private String prefix;

    public ListBucketsRequest() {
    }

    public ListBucketsRequest(String prefix2) {
        this(prefix2, (String) null);
    }

    public ListBucketsRequest(String prefix2, String marker2) {
        this(prefix2, marker2, 100);
    }

    public ListBucketsRequest(String prefix2, String marker2, Integer maxKeys2) {
        this.prefix = prefix2;
        this.marker = marker2;
        this.maxKeys = maxKeys2;
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
        this.maxKeys = maxKeys2;
    }
}
