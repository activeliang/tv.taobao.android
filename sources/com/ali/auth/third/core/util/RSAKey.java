package com.ali.auth.third.core.util;

import android.text.TextUtils;

public class RSAKey {
    private static final String DEFAULT_RSA_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8H6Gp7XP6UvEQzvUgGnt9nPX4exn1aNlmeyloMl6g2rEggeTNMp7I3iLPzQDbt6yedCru971fducKc2DgF/y2CcwAdqaKdxm0dSI2Zs4QLNYbKwWJ65wkgUh8+TJBnk+PGTgoxZ2wzvhJyRGjGhsFvLmZkUYPPxAPSNfjB3+/4wIDAQAB";
    private static final String LOGIN_RSA_PUBKEY = "LOGIN_RSA_PUBKEY";
    private static String rsaPubKey;

    public static String getRsaPubkey() {
        if (TextUtils.isEmpty(rsaPubKey)) {
            rsaPubKey = DEFAULT_RSA_KEY;
        }
        return rsaPubKey;
    }
}
