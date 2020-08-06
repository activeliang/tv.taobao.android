package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.util.Map;

/* compiled from: LocationRequest */
public final class ej extends ax {
    Map<String, String> f = null;
    String g = "";
    String h = "";
    byte[] i = null;
    byte[] j = null;
    boolean k = false;
    String l = null;
    Map<String, String> m = null;
    boolean n = false;
    private String o = "";

    public ej(Context context, t tVar) {
        super(context, tVar);
    }

    public final void a(String str) {
        this.g = str;
    }

    public final byte[] a_() {
        return this.i;
    }

    public final Map<String, String> b() {
        return this.f;
    }

    public final void b(String str) {
        this.h = str;
    }

    public final Map<String, String> b_() {
        return this.m;
    }

    public final String c() {
        return this.g;
    }

    public final void c(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.o = str;
        } else {
            this.o = "";
        }
    }

    public final byte[] e() {
        return this.j;
    }

    public final String h() {
        return this.h;
    }

    public final boolean i() {
        return this.k;
    }

    public final String j() {
        return this.l;
    }

    /* access modifiers changed from: protected */
    public final boolean k() {
        return this.n;
    }

    /* access modifiers changed from: protected */
    public final String n() {
        return this.o;
    }
}
