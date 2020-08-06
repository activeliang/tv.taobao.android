package com.loc;

import com.ali.auth.third.core.rpc.safe.AESCrypto;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: Encrypt */
public final class ec {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final byte[] b = {61, 61, 81, 65, 65, 69, 119, 65, 67, 48, 74, 80, 115, 116, 54, 75, 104, 76, 122, 97, 88, 99, 53, 71, 49, 122, 68, 70, 79, 104, 113, 113, 65, 97, 76, 54, 65, 66, 87, 53, 103, 85, 84, 113, 71, 68, 69, 76, 80, 82, 106, 51, 66, 75, 75, 69, 98, 55, 84, 108, 115, 122, 51, 106, 76, 55, 88, 122, 70, 121, 73, 75, 52, 50, 43, 101, 70, 121, 56, 105, 115, 105, 89, 120, 117, 112, 53, 48, 76, 81, 70, 86, 108, 110, 73, 65, 66, 74, 65, 83, 119, 65, 119, 83, 68, 65, 81, 66, 66, 69, 81, 65, 78, 99, 118, 104, 73, 90, 111, 75, 74, 89, 81, 68, 119, 119, 70, 77};
    private static final byte[] c = {0, 1, 1, 2, 3, 5, 8, 13, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final IvParameterSpec d = new IvParameterSpec(c);

    public static String a(String str) {
        if (str == null) {
            return null;
        }
        try {
            if (str.length() == 0) {
                return null;
            }
            return a("MD5", a("SHA1", str) + str);
        } catch (Throwable th) {
            en.a(th, "Encrypt", "generatorKey");
            return null;
        }
    }

    public static String a(String str, String str2) {
        if (str2 == null) {
            return null;
        }
        try {
            byte[] a2 = r.a(str2.getBytes("UTF-8"), str);
            int length = a2.length;
            StringBuilder sb = new StringBuilder(length * 2);
            for (int i = 0; i < length; i++) {
                sb.append(a[(a2[i] >> 4) & 15]);
                sb.append(a[a2[i] & 15]);
            }
            return sb.toString();
        } catch (Throwable th) {
            en.a(th, "Encrypt", "encode");
            return null;
        }
    }

    public static byte[] a(byte[] bArr) throws Exception {
        PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(dp.a(new StringBuilder(new String(b)).reverse().toString().getBytes())));
        Cipher instance = Cipher.getInstance(u.c("WUlNBL0VDQi9PQUVQV0lUSFNIQS0xQU5ETUdGMVBBRERJTkc"));
        instance.init(1, generatePublic);
        return instance.doFinal(bArr);
    }

    public static synchronized byte[] a(byte[] bArr, String str) throws Exception {
        byte[] byteArray;
        int i = 0;
        synchronized (ec.class) {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(o.b(str)));
            Cipher instance = Cipher.getInstance(u.c("CUlNBL0VDQi9QS0NTMVBhZGRpbmc"));
            instance.init(1, generatePrivate);
            int length = bArr.length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i2 = 0;
            while (length - i > 0) {
                byte[] doFinal = length - i > 245 ? instance.doFinal(bArr, i, 245) : instance.doFinal(bArr, i, length - i);
                byteArrayOutputStream.write(doFinal, 0, doFinal.length);
                int i3 = i2 + 1;
                int i4 = i3;
                i = i3 * 245;
                i2 = i4;
            }
            byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        }
        return byteArray;
    }

    private static SecretKeySpec b(String str) {
        byte[] bArr = null;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (Throwable th) {
            en.a(th, "Encrypt", "createKey");
        }
        return new SecretKeySpec(bArr, AESCrypto.ALGORITHM);
    }

    public static byte[] b(byte[] bArr) {
        int i = 0;
        try {
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[(bArr.length - 16)];
            System.arraycopy(bArr, 0, bArr2, 0, 16);
            System.arraycopy(bArr, 16, bArr3, 0, bArr.length - 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, AESCrypto.ALGORITHM);
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, secretKeySpec, new IvParameterSpec(u.c()));
            return instance.doFinal(bArr3);
        } catch (Throwable th) {
            if (bArr != null) {
                i = bArr.length;
            }
            en.a(th, "Encrypt", "decryptRsponse length = " + i);
            return null;
        }
    }

    public static byte[] b(byte[] bArr, String str) {
        try {
            SecretKeySpec b2 = b(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(u.c());
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(1, b2, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Throwable th) {
            en.a(th, "Encrypt", "aesEncrypt");
            return null;
        }
    }

    public static byte[] c(byte[] bArr, String str) {
        try {
            SecretKeySpec b2 = b(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(u.c());
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, b2, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Throwable th) {
            en.a(th, "Encrypt", "aesDecrypt");
            return null;
        }
    }
}
