package com.ali.auth.third.core.storage.aes;

import com.ali.auth.third.core.util.CommonUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class MD5 {
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return CommonUtils.getHashString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSHA256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            digest.update(content.getBytes());
            return CommonUtils.getHashString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
