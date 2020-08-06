package com.loc;

import android.content.Context;
import android.os.Build;
import java.util.Arrays;

/* compiled from: AESMD5Util */
public final class v {
    static byte[] a = null;
    static byte[] b = null;
    private static byte[] c = "FDF1F436161AEF5B".getBytes();
    private static byte[] d = "0102030405060708".getBytes();
    private static int e = 6;

    private static byte[] a(Context context) {
        if (context == null) {
            return new byte[0];
        }
        if (a != null && a.length > 0) {
            return a;
        }
        byte[] bytes = k.f(context).getBytes();
        a = bytes;
        return bytes;
    }

    public static byte[] a(Context context, byte[] bArr) {
        try {
            return o.b(a(context), bArr, b(context));
        } catch (Throwable th) {
            return new byte[0];
        }
    }

    private static byte[] b(Context context) {
        if (b != null && b.length > 0) {
            return b;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            b = Arrays.copyOfRange(a(context), 0, a(context).length / 2);
        } else {
            b = new byte[(a(context).length / 2)];
            for (int i = 0; i < b.length; i++) {
                b[i] = a(context)[i];
            }
        }
        return b;
    }

    public static byte[] b(Context context, byte[] bArr) {
        try {
            return o.a(a(context), bArr, b(context));
        } catch (Exception e2) {
            return new byte[0];
        }
    }
}
