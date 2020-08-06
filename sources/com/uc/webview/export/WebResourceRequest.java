package com.uc.webview.export;

import android.net.Uri;
import com.uc.webview.export.annotations.Api;
import java.util.HashMap;
import java.util.Map;

@Api
/* compiled from: ProGuard */
public class WebResourceRequest {
    String a;
    Map<String, String> b;
    Uri c;
    boolean d;
    boolean e;

    public WebResourceRequest(String str, HashMap<String, String> hashMap, String str2, boolean z, boolean z2) {
        this.a = str;
        this.b = hashMap;
        this.c = Uri.parse(str2);
        this.d = z;
        this.e = z2;
    }

    public WebResourceRequest(String str, HashMap<String, String> hashMap, Uri uri, boolean z, boolean z2) {
        this.a = str;
        this.b = hashMap;
        this.c = uri;
        this.d = z;
        this.e = z2;
    }

    public WebResourceRequest(String str, String str2, HashMap<String, String> hashMap) {
        this.a = str;
        this.b = hashMap;
        this.c = Uri.parse(str2);
    }

    public WebResourceRequest(String str, HashMap<String, String> hashMap) {
        this("Get", str, hashMap);
    }

    public String getMethod() {
        return this.a;
    }

    public void setMethod(String str) {
        this.a = str;
    }

    public Map<String, String> getRequestHeaders() {
        return this.b;
    }

    public void setRequestHeaders(Map<String, String> map) {
        this.b = map;
    }

    public Uri getUrl() {
        return this.c;
    }

    public void setUrl(String str) {
        this.c = Uri.parse(str);
    }

    public void setUrl(Uri uri) {
        this.c = uri;
    }

    public boolean hasGesture() {
        return this.d;
    }

    public boolean isForMainFrame() {
        return this.e;
    }

    public String toString() {
        return "method=" + this.a + ",header=" + this.b + ",uri=" + this.c + ",hasGesture=" + this.d + ",isForMainFrame=" + this.e;
    }
}
