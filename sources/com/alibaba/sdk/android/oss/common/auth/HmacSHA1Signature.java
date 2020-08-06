package com.alibaba.sdk.android.oss.common.auth;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA1Signature {
    private static final String ALGORITHM = "HmacSHA1";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final Object LOCK = new Object();
    private static final String VERSION = "1";
    private static Mac macInstance;

    public String getAlgorithm() {
        return ALGORITHM;
    }

    public String getVersion() {
        return "1";
    }

    public String computeSignature(String key, String data) {
        OSSLog.logDebug(getAlgorithm(), false);
        OSSLog.logDebug(getVersion(), false);
        try {
            return BinaryUtil.toBase64String(sign(key.getBytes("UTF-8"), data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported algorithm: UTF-8");
        }
    }

    private byte[] sign(byte[] key, byte[] data) {
        Mac mac;
        try {
            if (macInstance == null) {
                synchronized (LOCK) {
                    if (macInstance == null) {
                        macInstance = Mac.getInstance(getAlgorithm());
                    }
                }
            }
            try {
                mac = (Mac) macInstance.clone();
            } catch (CloneNotSupportedException e) {
                mac = Mac.getInstance(getAlgorithm());
            }
            mac.init(new SecretKeySpec(key, getAlgorithm()));
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Unsupported algorithm: HmacSHA1");
        } catch (InvalidKeyException e3) {
            throw new RuntimeException("key must not be null");
        }
    }
}
