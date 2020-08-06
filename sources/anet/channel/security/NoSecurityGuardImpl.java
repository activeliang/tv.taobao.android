package anet.channel.security;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.HMacUtil;

class NoSecurityGuardImpl implements ISecurity {
    private String appSecret = null;

    NoSecurityGuardImpl(String appSecret2) {
        this.appSecret = appSecret2;
    }

    public String sign(Context context, String signAlgorithm, String appkey, String data) {
        if (!TextUtils.isEmpty(this.appSecret) && ISecurity.SIGN_ALGORITHM_HMAC_SHA1.equalsIgnoreCase(signAlgorithm)) {
            return HMacUtil.hmacSha1Hex(this.appSecret.getBytes(), data.getBytes());
        }
        return null;
    }

    public byte[] decrypt(Context context, String mode, String key, byte[] needProcessValue) {
        return null;
    }

    public boolean saveBytes(Context context, String key, byte[] value) {
        return false;
    }

    public byte[] getBytes(Context context, String key) {
        return null;
    }

    public boolean isSecOff() {
        return true;
    }
}
