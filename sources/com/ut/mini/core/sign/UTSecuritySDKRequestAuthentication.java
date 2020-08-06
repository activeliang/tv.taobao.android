package com.ut.mini.core.sign;

public class UTSecuritySDKRequestAuthentication extends UTSecurityThridRequestAuthentication {
    public UTSecuritySDKRequestAuthentication(String appkey) {
        super(appkey, "");
    }

    public UTSecuritySDKRequestAuthentication(String appkey, String authCode) {
        super(appkey, authCode);
    }
}
