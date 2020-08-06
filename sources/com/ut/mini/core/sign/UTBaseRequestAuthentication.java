package com.ut.mini.core.sign;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MD5Utils;
import com.alibaba.analytics.utils.RC4;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UTBaseRequestAuthentication implements IUTRequestAuthentication {
    private String mAppSecret = null;
    private String mAppkey = null;
    private byte[] mDefaultAppAppSecret = null;
    private boolean mEncode = false;

    public String getAppkey() {
        return this.mAppkey;
    }

    public String getAppSecret() {
        return this.mAppSecret;
    }

    public UTBaseRequestAuthentication(String aAppkey, String aAppSecret) {
        this.mAppkey = aAppkey;
        this.mAppSecret = aAppSecret;
    }

    public UTBaseRequestAuthentication(String aAppkey, String aAppSecret, boolean isEncode) {
        this.mAppkey = aAppkey;
        this.mAppSecret = aAppSecret;
        this.mEncode = isEncode;
    }

    public boolean isEncode() {
        return this.mEncode;
    }

    public String getSign(String toBeSignedStr) {
        if (this.mAppkey == null || this.mAppSecret == null) {
            Logger.e("UTBaseRequestAuthentication", "There is no appkey,please check it!");
            return null;
        } else if (toBeSignedStr == null) {
            return null;
        } else {
            try {
                if (this.mEncode) {
                    return calcHmac(this.mAppSecret.getBytes(), toBeSignedStr.getBytes());
                }
                return calcHmac(getDefaultAppAppSecret(), toBeSignedStr.getBytes());
            } catch (Exception e) {
                return "";
            }
        }
    }

    private byte[] getDefaultAppAppSecret() {
        if (this.mDefaultAppAppSecret == null) {
            this.mDefaultAppAppSecret = RC4.rc4(new byte[]{66, 37, 42, -119, 118, -104, -30, 4, -95, 15, -26, -12, -75, -102, 71, 23, -3, -120, -1, -57, 42, 99, -16, -101, 103, -74, 93, -114, 112, -26, -24, -24});
        }
        return this.mDefaultAppAppSecret;
    }

    public static String calcHmac(byte[] key, byte[] src) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key, mac.getAlgorithm()));
        return MD5Utils.toHexString(mac.doFinal(src));
    }
}
