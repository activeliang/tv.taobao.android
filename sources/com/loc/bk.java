package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* compiled from: HeaderAddStrategy */
public final class bk extends bo {
    private Context a;
    private String b;
    private ao e;
    private Object[] f;

    public bk(Context context, bo boVar, ao aoVar, String str, Object... objArr) {
        super(boVar);
        this.a = context;
        this.b = str;
        this.e = aoVar;
        this.f = objArr;
    }

    private String b() {
        try {
            return String.format(u.c(this.b), this.f);
        } catch (Throwable th) {
            th.printStackTrace();
            ab.b(th, "ofm", "gpj");
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public final byte[] a(byte[] bArr) throws CertificateException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String a2 = u.a(bArr);
        if (TextUtils.isEmpty(a2)) {
            return null;
        }
        String a3 = u.a(this.e.b(u.a(b())));
        StringBuilder sb = new StringBuilder();
        sb.append("{\"pinfo\":\"").append(a3).append("\",\"els\":[");
        sb.append(a2);
        sb.append("]}");
        return u.a(sb.toString());
    }
}
