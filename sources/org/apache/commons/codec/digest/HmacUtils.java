package org.apache.commons.codec.digest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public final class HmacUtils {
    private static final int STREAM_BUFFER_LENGTH = 1024;
    private final Mac mac;

    public static boolean isAvailable(String name) {
        try {
            Mac.getInstance(name);
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public static boolean isAvailable(HmacAlgorithms name) {
        try {
            Mac.getInstance(name.getName());
            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Deprecated
    public static Mac getHmacMd5(byte[] key) {
        return getInitializedMac(HmacAlgorithms.HMAC_MD5, key);
    }

    @Deprecated
    public static Mac getHmacSha1(byte[] key) {
        return getInitializedMac(HmacAlgorithms.HMAC_SHA_1, key);
    }

    @Deprecated
    public static Mac getHmacSha256(byte[] key) {
        return getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key);
    }

    @Deprecated
    public static Mac getHmacSha384(byte[] key) {
        return getInitializedMac(HmacAlgorithms.HMAC_SHA_384, key);
    }

    @Deprecated
    public static Mac getHmacSha512(byte[] key) {
        return getInitializedMac(HmacAlgorithms.HMAC_SHA_512, key);
    }

    public static Mac getInitializedMac(HmacAlgorithms algorithm, byte[] key) {
        return getInitializedMac(algorithm.getName(), key);
    }

    public static Mac getInitializedMac(String algorithm, byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("Null key");
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac2 = Mac.getInstance(algorithm);
            mac2.init(keySpec);
            return mac2;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        } catch (InvalidKeyException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    @Deprecated
    public static byte[] hmacMd5(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacMd5(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacMd5(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmac(valueToDigest);
    }

    @Deprecated
    public static String hmacMd5Hex(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacMd5Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacMd5Hex(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha1(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha1(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha1(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmac(valueToDigest);
    }

    @Deprecated
    public static String hmacSha1Hex(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha1Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha1Hex(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha256(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha256(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha256(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmac(valueToDigest);
    }

    @Deprecated
    public static String hmacSha256Hex(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha256Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha256Hex(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha384(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha384(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha384(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmac(valueToDigest);
    }

    @Deprecated
    public static String hmacSha384Hex(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha384Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha384Hex(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha512(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha512(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmac(valueToDigest);
    }

    @Deprecated
    public static byte[] hmacSha512(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmac(valueToDigest);
    }

    @Deprecated
    public static String hmacSha512Hex(byte[] key, byte[] valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha512Hex(byte[] key, InputStream valueToDigest) throws IOException {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(valueToDigest);
    }

    @Deprecated
    public static String hmacSha512Hex(String key, String valueToDigest) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(valueToDigest);
    }

    public static Mac updateHmac(Mac mac2, byte[] valueToDigest) {
        mac2.reset();
        mac2.update(valueToDigest);
        return mac2;
    }

    public static Mac updateHmac(Mac mac2, InputStream valueToDigest) throws IOException {
        mac2.reset();
        byte[] buffer = new byte[1024];
        int read = valueToDigest.read(buffer, 0, 1024);
        while (read > -1) {
            mac2.update(buffer, 0, read);
            read = valueToDigest.read(buffer, 0, 1024);
        }
        return mac2;
    }

    public static Mac updateHmac(Mac mac2, String valueToDigest) {
        mac2.reset();
        mac2.update(StringUtils.getBytesUtf8(valueToDigest));
        return mac2;
    }

    @Deprecated
    public HmacUtils() {
        this((Mac) null);
    }

    private HmacUtils(Mac mac2) {
        this.mac = mac2;
    }

    public HmacUtils(String algorithm, byte[] key) {
        this(getInitializedMac(algorithm, key));
    }

    public HmacUtils(String algorithm, String key) {
        this(algorithm, StringUtils.getBytesUtf8(key));
    }

    public HmacUtils(HmacAlgorithms algorithm, String key) {
        this(algorithm.getName(), StringUtils.getBytesUtf8(key));
    }

    public HmacUtils(HmacAlgorithms algorithm, byte[] key) {
        this(algorithm.getName(), key);
    }

    public byte[] hmac(byte[] valueToDigest) {
        return this.mac.doFinal(valueToDigest);
    }

    public String hmacHex(byte[] valueToDigest) {
        return Hex.encodeHexString(hmac(valueToDigest));
    }

    public byte[] hmac(String valueToDigest) {
        return this.mac.doFinal(StringUtils.getBytesUtf8(valueToDigest));
    }

    public String hmacHex(String valueToDigest) {
        return Hex.encodeHexString(hmac(valueToDigest));
    }

    public byte[] hmac(ByteBuffer valueToDigest) {
        this.mac.update(valueToDigest);
        return this.mac.doFinal();
    }

    public String hmacHex(ByteBuffer valueToDigest) {
        return Hex.encodeHexString(hmac(valueToDigest));
    }

    public byte[] hmac(InputStream valueToDigest) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = valueToDigest.read(buffer, 0, 1024);
            if (read <= -1) {
                return this.mac.doFinal();
            }
            this.mac.update(buffer, 0, read);
        }
    }

    public String hmacHex(InputStream valueToDigest) throws IOException {
        return Hex.encodeHexString(hmac(valueToDigest));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
        if (r0 != null) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0025, code lost:
        if (r2 != null) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002a, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002b, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002c, code lost:
        r2.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0030, code lost:
        r0.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] hmac(java.io.File r5) throws java.io.IOException {
        /*
            r4 = this;
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream
            java.io.FileInputStream r1 = new java.io.FileInputStream
            r1.<init>(r5)
            r0.<init>(r1)
            r2 = 0
            byte[] r1 = r4.hmac((java.io.InputStream) r0)     // Catch:{ Throwable -> 0x0020 }
            if (r0 == 0) goto L_0x0016
            if (r2 == 0) goto L_0x001c
            r0.close()     // Catch:{ Throwable -> 0x0017 }
        L_0x0016:
            return r1
        L_0x0017:
            r3 = move-exception
            r2.addSuppressed(r3)
            goto L_0x0016
        L_0x001c:
            r0.close()
            goto L_0x0016
        L_0x0020:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0022 }
        L_0x0022:
            r1 = move-exception
            if (r0 == 0) goto L_0x002a
            if (r2 == 0) goto L_0x0030
            r0.close()     // Catch:{ Throwable -> 0x002b }
        L_0x002a:
            throw r1
        L_0x002b:
            r3 = move-exception
            r2.addSuppressed(r3)
            goto L_0x002a
        L_0x0030:
            r0.close()
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.digest.HmacUtils.hmac(java.io.File):byte[]");
    }

    public String hmacHex(File valueToDigest) throws IOException {
        return Hex.encodeHexString(hmac(valueToDigest));
    }
}
