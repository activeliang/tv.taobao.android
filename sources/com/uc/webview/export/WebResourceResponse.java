package com.uc.webview.export;

import com.uc.webview.export.annotations.Api;
import java.io.InputStream;

@Api
/* compiled from: ProGuard */
public class WebResourceResponse {
    private String a;
    private String b;
    private InputStream c;

    public WebResourceResponse(String str, String str2, InputStream inputStream) {
        this.a = str;
        this.b = str2;
        this.c = inputStream;
    }

    public void setMimeType(String str) {
        this.a = str;
    }

    public String getMimeType() {
        return this.a;
    }

    public void setEncoding(String str) {
        this.b = str;
    }

    public String getEncoding() {
        return this.b;
    }

    public void setData(InputStream inputStream) {
        this.c = inputStream;
    }

    public InputStream getData() {
        return this.c;
    }
}
