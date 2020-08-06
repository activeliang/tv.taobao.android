package com.loc;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* compiled from: MD5 */
public final class r {
    public static String a(String str) {
        if (str == null) {
            return null;
        }
        return u.e(c(str));
    }

    public static String a(byte[] bArr) {
        return u.e(a(bArr, "MD5"));
    }

    public static byte[] a(byte[] bArr, String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            instance.update(bArr);
            return instance.digest();
        } catch (Throwable th) {
            y.a(th, "MD5", "gmb");
            return null;
        }
    }

    public static String b(String str) {
        return u.f(d(str));
    }

    private static byte[] c(String str) {
        try {
            return e(str);
        } catch (Throwable th) {
            y.a(th, "MD5", "gmb");
            return new byte[0];
        }
    }

    private static byte[] d(String str) {
        try {
            return e(str);
        } catch (Throwable th) {
            th.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] e(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(u.a(str));
        return instance.digest();
    }
}
