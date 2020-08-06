package android.taobao.windvane.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class DigestUtils {
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String SHA256 = "SHA-256";

    public static String md5ToHex(String input) {
        if (input == null) {
            return null;
        }
        try {
            return digest(input.getBytes("utf-8"), "MD5");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String md5ToHex(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        return digest(input, "MD5");
    }

    public static String sha256ToHex(byte[] input) {
        if (input == null) {
            return null;
        }
        return digest(input, "SHA-256");
    }

    public static String sha1ToHex(byte[] input) {
        if (input == null) {
            return null;
        }
        return digest(input, "SHA-1");
    }

    public static byte[] sha1ToByte(byte[] input) {
        if (input == null) {
            return null;
        }
        return digest2byte(input, "SHA-1");
    }

    private static byte[] digest2byte(byte[] input, String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm).digest(input);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }

    private static String digest(byte[] input, String algorithm) {
        return HexUtil.bytesToHexString(digest2byte(input, algorithm));
    }

    private static String digest(InputStream input, String algorithm) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int read = input.read(buffer, 0, 1024);
            while (read > -1) {
                messageDigest.update(buffer, 0, read);
                read = input.read(buffer, 0, 1024);
            }
            return HexUtil.bytesToHexString(messageDigest.digest());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Security exception", e);
        }
    }
}
