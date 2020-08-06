package com.yunos.tvtaobao.uuid.client;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    public static byte[] encodeHmacSha1(String data, String key, String encode) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] dataBytes = null;
        byte[] keyBytes = null;
        try {
            dataBytes = data.getBytes(encode);
            keyBytes = key.getBytes(encode);
        } catch (UnsupportedEncodingException e) {
        }
        if (dataBytes == null || keyBytes == null) {
            return null;
        }
        return encodeHmacSha1(dataBytes, keyBytes);
    }

    public static byte[] encodeHmacSha1(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key, "HMACSHA1");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }
}
