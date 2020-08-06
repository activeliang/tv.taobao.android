package com.ali.auth.third.core.rpc.safe;

import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.Base64;
import com.ali.auth.third.core.util.Base64URLSafe;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
    public static final String ALGORITHM = "AES";
    private static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
    private static final String ENCODE_UTF_8 = "UTF-8";
    private static final String SECRETKEYSPEC_NAME = "AES";
    public static final String TAG = "auth.AESCrypto";
    private static final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static ThreadLocal<AESCrypto> threadLocal = new ThreadLocal<>();
    private Cipher cipher = null;
    private IvParameterSpec ivParameterSpec = null;

    public static AESCrypto instance() {
        if (threadLocal.get() == null) {
            threadLocal.set(new AESCrypto());
        }
        return threadLocal.get();
    }

    private AESCrypto() {
        generateCipher();
    }

    private void generateCipher() {
        if (this.cipher == null) {
            try {
                this.cipher = Cipher.getInstance(CIPHER_NAME);
                this.ivParameterSpec = new IvParameterSpec(iv);
            } catch (NoSuchAlgorithmException e) {
                SDKLogger.e(TAG, "AES:generateCipher:generate cipher error", e);
            } catch (NoSuchPaddingException e2) {
                SDKLogger.e(TAG, "AES:generateCipher:generate cipher error", e2);
            }
        }
    }

    public String encrypt(String plainText, String rawKey) {
        if (plainText == null || rawKey == null) {
            return null;
        }
        try {
            byte[] result = encrypt(plainText.getBytes("UTF-8"), rawKey.getBytes("UTF-8"));
            if (result != null) {
                return Base64.encode(result);
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            SDKLogger.e(TAG, "AES:encrypt:" + plainText + ":String to Byte Array Error", e);
            return null;
        }
    }

    public byte[] encrypt(byte[] plainByte, byte[] keyByte) {
        if (plainByte == null || keyByte == null) {
            return null;
        }
        try {
            this.cipher.init(1, new SecretKeySpec(keyByte, "AES"), this.ivParameterSpec);
            return this.cipher.doFinal(plainByte);
        } catch (IllegalBlockSizeException e) {
            SDKLogger.e(TAG, "AES:encrypt:" + plainByte + ":encrypt data error", e);
            return null;
        } catch (BadPaddingException e2) {
            SDKLogger.e(TAG, "AES:encrypt:" + plainByte + ":encrypt data error", e2);
            return null;
        } catch (InvalidKeyException e3) {
            SDKLogger.e(TAG, "AES:encrypt:" + plainByte + ":encrypt data error", e3);
            return null;
        } catch (InvalidAlgorithmParameterException e4) {
            SDKLogger.e(TAG, "AES:encrypt:" + plainByte + ":encrypt data error", e4);
            return null;
        }
    }

    public String decryptWrapper(String content, String rawKey) {
        String content2;
        String content3 = content.trim();
        int contentLength = Integer.valueOf(content3.substring(0, 8), 16).intValue();
        if (contentLength == content3.length() - 8) {
            content2 = content3.substring(8);
        } else {
            content2 = content3.substring(8, contentLength + 8);
        }
        int padding = content2.length() % 4;
        if (padding == 1) {
            content2 = content2 + "===";
        } else if (padding == 2) {
            content2 = content2 + "==";
        } else if (padding == 3) {
            content2 = content2 + "=";
        }
        return decrypt(content2, rawKey);
    }

    public String decrypt(String plainText, String rawKey) {
        if (plainText == null || rawKey == null) {
            return null;
        }
        try {
            byte[] result = decrypt(Base64URLSafe.decode(plainText), rawKey.getBytes("UTF-8"));
            if (result != null) {
                return new String(result, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            SDKLogger.e(TAG, "AES:decrypt:" + plainText + ":String to Byte Array Error", e);
        }
        return null;
    }

    public byte[] decrypt(byte[] plainByte, byte[] keyByte) {
        if (plainByte == null) {
            return null;
        }
        try {
            this.cipher.init(2, new SecretKeySpec(keyByte, "AES"), this.ivParameterSpec);
            return this.cipher.doFinal(plainByte);
        } catch (IllegalBlockSizeException e) {
            SDKLogger.e(TAG, "AES:decrypt:" + plainByte + ":decrypt data error", e);
            return null;
        } catch (BadPaddingException e2) {
            SDKLogger.e(TAG, "AES:decrypt:" + plainByte + ":decrypt data error", e2);
            return null;
        } catch (InvalidKeyException e3) {
            SDKLogger.e(TAG, "AES:decrypt:" + plainByte + ":decrypt data error", e3);
            return null;
        } catch (InvalidAlgorithmParameterException e4) {
            SDKLogger.e(TAG, "AES:decrypt:" + plainByte + ":decrypt data error", e4);
            return null;
        }
    }
}
