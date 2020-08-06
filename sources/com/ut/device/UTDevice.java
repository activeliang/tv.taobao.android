package com.ut.device;

import android.content.Context;
import com.ta.audid.Variables;

public class UTDevice {
    public static String getUtdid(Context context) {
        return com.ta.utdid2.device.UTDevice.getUtdid(context);
    }

    @Deprecated
    public static String getUtdidForUpdate(Context context) {
        return com.ta.utdid2.device.UTDevice.getUtdidForUpdate(context);
    }

    public static void setAppKey(String appKey) {
        Variables.getInstance().setAppkey(appKey);
    }

    public static void setAppChannel(String appChannel) {
        Variables.getInstance().setAppChannel(appChannel);
    }

    @Deprecated
    public static void setDebug(boolean isDebug) {
        Variables.getInstance().setDebug(isDebug);
    }

    @Deprecated
    public static void setOldMode(Context context, boolean oldMode) {
        if (context != null) {
            Variables.getInstance().initContext(context);
            Variables.getInstance().setOldMode(oldMode);
        }
    }

    @Deprecated
    public static String getAid(String appName, String token, Context context) {
        return "";
    }

    @Deprecated
    public static void getAidAsync(String appName, String token, Context context, AidCallback callback) {
    }
}
