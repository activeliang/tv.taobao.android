package com.ali.user.open.oauth;

import java.util.HashMap;
import java.util.Map;

public class OauthPlatformConfig {
    private static Map<String, AppCredential> sPlateformConfigs = new HashMap();

    public static void setOauthConfig(String oauthSite, AppCredential appCredential) {
        sPlateformConfigs.put(oauthSite, appCredential);
    }

    public static AppCredential getOauthConfigByPlatform(String oauthSite) {
        return sPlateformConfigs.get(oauthSite);
    }
}
