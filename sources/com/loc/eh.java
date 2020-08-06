package com.loc;

import android.text.TextUtils;
import java.util.Map;

/* compiled from: HttpRequest */
public final class eh extends q {
    Map<String, String> a = null;
    Map<String, String> b = null;
    String f = "";
    byte[] g = null;
    private String h = null;

    public final void a(String str) {
        this.f = str;
    }

    public final void a(Map<String, String> map) {
        this.a = map;
    }

    public final void a(byte[] bArr) {
        this.g = bArr;
    }

    public final Map<String, String> b() {
        return this.a;
    }

    public final void b(String str) {
        this.h = str;
    }

    public final void b(Map<String, String> map) {
        this.b = map;
    }

    public final Map<String, String> b_() {
        return this.b;
    }

    public final String c() {
        return this.f;
    }

    public final byte[] d() {
        return this.g;
    }

    public final String h() {
        return !TextUtils.isEmpty(this.h) ? this.h : super.h();
    }
}
