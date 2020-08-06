package com.loc;

import android.content.Context;
import android.os.Build;
import java.io.ByteArrayOutputStream;

/* compiled from: StatisticsHeaderDataStrategy */
public final class bm extends bo {
    public static int a = 13;
    public static int b = 6;
    private Context e;

    public bm(Context context, bo boVar) {
        super(boVar);
        this.e = context;
    }

    private static byte[] a(Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[0];
        try {
            u.a(byteArrayOutputStream, "1.2." + a + "." + b);
            u.a(byteArrayOutputStream, "Android");
            u.a(byteArrayOutputStream, n.x(context));
            u.a(byteArrayOutputStream, n.m(context));
            u.a(byteArrayOutputStream, n.h(context));
            u.a(byteArrayOutputStream, Build.MANUFACTURER);
            u.a(byteArrayOutputStream, Build.MODEL);
            u.a(byteArrayOutputStream, Build.DEVICE);
            u.a(byteArrayOutputStream, n.A(context));
            u.a(byteArrayOutputStream, k.c(context));
            u.a(byteArrayOutputStream, k.d(context));
            u.a(byteArrayOutputStream, k.f(context));
            byteArrayOutputStream.write(new byte[]{0});
            bArr = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                th = th;
                th.printStackTrace();
                return bArr;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public final byte[] a(byte[] bArr) {
        byte[] a2 = a(this.e);
        byte[] bArr2 = new byte[(a2.length + bArr.length)];
        System.arraycopy(a2, 0, bArr2, 0, a2.length);
        System.arraycopy(bArr, 0, bArr2, a2.length, bArr.length);
        return bArr2;
    }
}
