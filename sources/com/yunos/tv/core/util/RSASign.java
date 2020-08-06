package com.yunos.tv.core.util;

import android.util.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSASign {
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey, String charset) {
        try {
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0)));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            return Base64.encodeToString(signature.sign(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptWithPubkey(byte[] pubKeyBytes, String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyBytes));
        byte[] byteData = plain.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, publicKey);
        return Base64.encodeToString(cipher.doFinal(byteData), 0);
    }

    public static String decryptWithPrivatekey(byte[] privateKeyBytes, String encoded) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, privateKey);
        return new String(cipher.doFinal(Base64.decode(encoded, 0)));
    }
}
