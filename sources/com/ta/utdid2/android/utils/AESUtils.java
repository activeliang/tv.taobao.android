package com.ta.utdid2.android.utils;

import com.ali.auth.third.core.rpc.safe.AESCrypto;
import com.ta.audid.utils.RC4;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static String encrypt(String clearText) {
        byte[] result = null;
        try {
            result = encrypt(getRawKey(), clearText.getBytes());
        } catch (Exception e) {
        }
        if (result != null) {
            return toHex(result);
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            return new String(decrypt(getRawKey(), toByte(encrypted)));
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] getRawKey() throws Exception {
        return RC4.rc4(new byte[]{33, 83, -50, -89, -84, -114, 80, 99, 10, 63, 22, -65, -11, 30, 101, -118});
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AESCrypto.ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AESCrypto.ALGORITHM);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(encrypted);
    }

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte appendHex : buf) {
            appendHex(result, appendHex);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append("0123456789ABCDEF".charAt((b >> 4) & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }
}
