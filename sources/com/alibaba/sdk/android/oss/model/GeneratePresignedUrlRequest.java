package com.alibaba.sdk.android.oss.model;

import com.alibaba.sdk.android.oss.common.HttpMethod;
import java.util.HashMap;
import java.util.Map;

public class GeneratePresignedUrlRequest {
    private String bucketName;
    private String contentMD5;
    private String contentType;
    private long expiration;
    private String key;
    private HttpMethod method;
    private String process;
    private Map<String, String> queryParam;

    public GeneratePresignedUrlRequest(String bucketName2, String key2) {
        this(bucketName2, key2, 3600);
    }

    public GeneratePresignedUrlRequest(String bucketName2, String key2, long expiration2) {
        this(bucketName2, key2, 3600, HttpMethod.GET);
    }

    public GeneratePresignedUrlRequest(String bucketName2, String key2, long expiration2, HttpMethod method2) {
        this.queryParam = new HashMap();
        this.bucketName = bucketName2;
        this.key = key2;
        this.expiration = expiration2;
        this.method = method2;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType2) {
        this.contentType = contentType2;
    }

    public String getContentMD5() {
        return this.contentMD5;
    }

    public void setContentMD5(String contentMD52) {
        this.contentMD5 = contentMD52;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(HttpMethod method2) {
        if (method2 == HttpMethod.GET || method2 == HttpMethod.PUT) {
            this.method = method2;
            return;
        }
        throw new IllegalArgumentException("Only GET or PUT is supported!");
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(String bucketName2) {
        this.bucketName = bucketName2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public void setExpiration(long expiration2) {
        this.expiration = expiration2;
    }

    public Map<String, String> getQueryParameter() {
        return this.queryParam;
    }

    public void setQueryParameter(Map<String, String> queryParam2) {
        if (queryParam2 == null) {
            throw new NullPointerException("The argument 'queryParameter' is null.");
        }
        if (this.queryParam != null && this.queryParam.size() > 0) {
            this.queryParam.clear();
        }
        this.queryParam.putAll(queryParam2);
    }

    public void addQueryParameter(String key2, String value) {
        this.queryParam.put(key2, value);
    }

    public String getProcess() {
        return this.process;
    }

    public void setProcess(String process2) {
        this.process = process2;
    }
}
