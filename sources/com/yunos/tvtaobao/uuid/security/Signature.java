package com.yunos.tvtaobao.uuid.security;

import android.util.Base64;
import com.yunos.tvtaobao.uuid.utils.Logger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import org.apache.commons.codec.CharEncoding;

public class Signature {
    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public String sign(String priKey, String str) {
        Logger.log_d("SIGN: " + str);
        try {
            PrivateKey myprikey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(getPK(priKey)));
            java.security.Signature signet = java.security.Signature.getInstance("SHA1withRSA");
            signet.initSign(myprikey);
            signet.update(str.getBytes(CharEncoding.ISO_8859_1));
            return Base64.encodeToString(signet.sign(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] getPK(String pk) {
        return hexStrToBytes(pk);
    }

    private byte[] hexStrToBytes(String s) {
        byte[] bytes = new byte[(s.length() / 2)];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16);
        }
        return bytes;
    }

    private String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);
        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 15]);
            s.append(bcdLookup[bcd[i] & 15]);
        }
        return s.toString();
    }
}
