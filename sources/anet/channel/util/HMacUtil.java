package anet.channel.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMacUtil {
    private static final String TAG = "awcn.HMacUtil";

    public static String hmacSha1Hex(byte[] key, byte[] valueToDigest) {
        try {
            return StringUtils.bytesToHexString(hmacSha1(key, valueToDigest));
        } catch (Throwable t) {
            ALog.e(TAG, "hmacSha1Hex", (String) null, "result", "", t);
            return "";
        }
    }

    private static byte[] hmacSha1(byte[] key, byte[] valueToDigest) {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(valueToDigest);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            return null;
        }
    }
}
