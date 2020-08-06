package com.alibaba.analytics.version;

public class UTBuildInfo implements IUTBuildInfo {
    private static UTBuildInfo s_instance = null;
    private static String sdk_version = "6.5.6.33";

    private UTBuildInfo() {
    }

    public static synchronized UTBuildInfo getInstance() {
        UTBuildInfo uTBuildInfo;
        synchronized (UTBuildInfo.class) {
            if (s_instance == null) {
                s_instance = new UTBuildInfo();
            }
            uTBuildInfo = s_instance;
        }
        return uTBuildInfo;
    }

    public String getBuildID() {
        return "";
    }

    public String getGitCommitID() {
        return "";
    }

    public String getShortSDKVersion() {
        return sdk_version;
    }

    public String getFullSDKVersion() {
        return sdk_version;
    }

    public boolean isTestMode() {
        return false;
    }
}
