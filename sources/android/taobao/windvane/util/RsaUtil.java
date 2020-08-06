package android.taobao.windvane.util;

import android.util.Base64;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaUtil {
    private static final String encryptMode = "RSA/ECB/PKCS1Padding";
    private static final String keyMode = "RSA";

    public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(keyMode).generatePublic(new X509EncodedKeySpec(Base64.decode(key, 0)));
    }

    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(keyMode).generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key, 0)));
    }

    public static String getKeyString(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Base64.encodeToString(key.getEncoded(), 0);
    }

    public static String encryptData(String data, Key key) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return Base64.encodeToString(encryptData(data.getBytes(), key), 0);
    }

    public static String decryptData(String data, Key key) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return new String(decryptData(Base64.decode(data, 0), key));
    }

    public static byte[] encryptData(byte[] data, Key key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(encryptMode);
        cipher.init(1, key);
        return cipher.doFinal(data);
    }

    public static byte[] decryptData(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(encryptMode);
        cipher.init(2, key);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(keyMode);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = getKeyString(publicKey);
        String privateKeyString = getKeyString((RSAPrivateKey) keyPair.getPrivate());
        Cipher cipher = Cipher.getInstance(keyMode);
        byte[] plainText = "我们都很好！邮件：@sina.com".getBytes();
        cipher.init(1, publicKey);
        byte[] enBytes = cipher.doFinal(plainText);
        PublicKey publicKey2 = getPublicKey(publicKeyString);
        PrivateKey privateKey = getPrivateKey(privateKeyString);
        cipher.init(2, privateKey);
        byte[] deBytes = cipher.doFinal(enBytes);
        String publicKeyString2 = getKeyString(publicKey2);
        String privateKeyString2 = getKeyString(privateKey);
        new String(deBytes);
    }
}
