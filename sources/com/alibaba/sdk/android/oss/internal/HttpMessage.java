package com.alibaba.sdk.android.oss.internal;

import com.alibaba.sdk.android.oss.common.utils.CaseInsensitiveHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

abstract class HttpMessage {
    private InputStream content;
    private long contentLength;
    private Map<String, String> headers = new CaseInsensitiveHashMap();
    private String stringBody;

    HttpMessage() {
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers2) {
        if (this.headers == null) {
            this.headers = new CaseInsensitiveHashMap();
        }
        if (this.headers != null && this.headers.size() > 0) {
            this.headers.clear();
        }
        this.headers.putAll(headers2);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public InputStream getContent() {
        return this.content;
    }

    public void setContent(InputStream content2) {
        this.content = content2;
    }

    public String getStringBody() {
        return this.stringBody;
    }

    public void setStringBody(String stringBody2) {
        this.stringBody = stringBody2;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public void setContentLength(long contentLength2) {
        this.contentLength = contentLength2;
    }

    public void close() throws IOException {
        if (this.content != null) {
            this.content.close();
            this.content = null;
        }
    }
}
