package mtopsdk.security.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSha1Utils {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String HMAC_SHA1 = "HmacSha1";

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[(l << 1)];
        int j = 0;
        for (int i = 0; i < l; i++) {
            int j2 = j + 1;
            out[j] = DIGITS[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = DIGITS[data[i] & 15];
        }
        return out;
    }

    public static String hmacSha1Hex(String baseStr, String appSecret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(new SecretKeySpec(appSecret.getBytes("utf-8"), HMAC_SHA1));
            return new String(encodeHex(mac.doFinal(baseStr.getBytes("utf-8"))));
        } catch (Exception e) {
            return null;
        }
    }
}
