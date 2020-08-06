package com.alibaba.sdk.android.oss.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import okhttp3.Response;

public class ResponseMessage extends HttpMessage {
    private RequestMessage request;
    private Response response;
    private int statusCode;

    public /* bridge */ /* synthetic */ void addHeader(String str, String str2) {
        super.addHeader(str, str2);
    }

    public /* bridge */ /* synthetic */ void close() throws IOException {
        super.close();
    }

    public /* bridge */ /* synthetic */ InputStream getContent() {
        return super.getContent();
    }

    public /* bridge */ /* synthetic */ long getContentLength() {
        return super.getContentLength();
    }

    public /* bridge */ /* synthetic */ Map getHeaders() {
        return super.getHeaders();
    }

    public /* bridge */ /* synthetic */ String getStringBody() {
        return super.getStringBody();
    }

    public /* bridge */ /* synthetic */ void setContent(InputStream inputStream) {
        super.setContent(inputStream);
    }

    public /* bridge */ /* synthetic */ void setContentLength(long j) {
        super.setContentLength(j);
    }

    public /* bridge */ /* synthetic */ void setHeaders(Map map) {
        super.setHeaders(map);
    }

    public /* bridge */ /* synthetic */ void setStringBody(String str) {
        super.setStringBody(str);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode2) {
        this.statusCode = statusCode2;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setResponse(Response response2) {
        this.response = response2;
    }

    public RequestMessage getRequest() {
        return this.request;
    }

    public void setRequest(RequestMessage request2) {
        this.request = request2;
    }
}
