package com.taobao.orange.impl;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.orange.inner.ISign;
import com.taobao.orange.util.OLog;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSign implements ISign {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String TAG = "HmacSign";

    public String sign(Context context, String appKey, String appSecret, String data, String authCode) {
        return hmacSha1(data, appSecret);
    }

    private String hmacSha1(String baseStr, String appSecret) {
        if (TextUtils.isEmpty(baseStr) || TextUtils.isEmpty(appSecret)) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(new SecretKeySpec(appSecret.getBytes("utf-8"), HMAC_SHA1));
            char[] hexChars = encodeHex(mac.doFinal(baseStr.getBytes("utf-8")));
            if (hexChars != null) {
                return new String(hexChars);
            }
            return null;
        } catch (Exception e) {
            OLog.e(TAG, "hmacSha1", e, new Object[0]);
            return null;
        }
    }

    private char[] encodeHex(byte[] data) {
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
}
