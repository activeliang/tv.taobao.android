package com.loc;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import java.net.Proxy;
import java.util.Map;

/* compiled from: Request */
public abstract class az {
    int c = 20000;
    int d = 20000;
    Proxy e = null;

    private String a(String str) {
        Map<String, String> b_;
        byte[] d2 = d();
        if (d2 == null || d2.length == 0 || (b_ = b_()) == null) {
            return str;
        }
        String a = ay.a(b_);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str).append(WVUtils.URL_DATA_CHAR).append(a);
        return stringBuffer.toString();
    }

    public final void a(int i) {
        this.c = i;
    }

    public final void a(Proxy proxy) {
        this.e = proxy;
    }

    public abstract Map<String, String> b();

    public final void b(int i) {
        this.d = i;
    }

    public abstract Map<String, String> b_();

    public abstract String c();

    public byte[] d() {
        return null;
    }

    public boolean g() {
        return false;
    }

    public String h() {
        return c();
    }

    /* access modifiers changed from: package-private */
    public final String l() {
        return a(c());
    }

    /* access modifiers changed from: package-private */
    public final String m() {
        return a(h());
    }

    /* access modifiers changed from: protected */
    public String n() {
        return "";
    }

    /* access modifiers changed from: protected */
    public final boolean o() {
        return !TextUtils.isEmpty(n());
    }
}
