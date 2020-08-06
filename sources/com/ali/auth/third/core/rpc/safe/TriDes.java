package com.ali.auth.third.core.rpc.safe;

import com.ali.auth.third.core.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TriDes {
    private static String TriDes = "DESede/ECB/PKCS5Padding";

    public static String encrypt(String keyString, String content) {
        try {
            Key key = new SecretKeySpec(keyString.getBytes(), "DESede");
            Cipher c1 = Cipher.getInstance(TriDes);
            c1.init(1, key);
            return Base64.encode(c1.doFinal(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String keyString, String content) {
        String content2;
        String content3 = content.trim();
        int contentLength = Integer.valueOf(content3.substring(0, 8), 16).intValue();
        if (contentLength == content3.length() - 8) {
            content2 = content3.substring(8);
        } else {
            content2 = content3.substring(8, contentLength + 8);
        }
        try {
            Key key = new SecretKeySpec(keyString.getBytes(), "DESede");
            Cipher c1 = Cipher.getInstance(TriDes);
            c1.init(2, key);
            return new String(c1.doFinal(Base64.decode(content2)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
