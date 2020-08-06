package com.yunos.tvtaobao.uuid;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.yunos.tvtaobao.uuid.TVAppUUIDImpl;
import com.yunos.tvtaobao.uuid.constants.Constants;
import com.yunos.tvtaobao.uuid.utils.Logger;

public class CloudUUID {
    static final String UUID_VERSION = "1.2.32";

    public CloudUUID(Context context) {
    }

    public static void init(Context context, boolean log) {
        Constants.DEBUG = log;
        Logger.log("init called: version: 1.2.32");
        TVAppUUIDImpl._init(context);
    }

    public static TVAppUUIDImpl.UUID_FORM_WHERE isUUIDExist() {
        Logger.log_d("isUUIDExist called");
        return TVAppUUIDImpl._isUUIDExist();
    }

    public static boolean isSystemUUIDExist() {
        return TVAppUUIDImpl._isSystemUUID();
    }

    public static String getCloudUUID() {
        Logger.log_d("getCloudUUID called");
        return TVAppUUIDImpl._getCloudUUID();
    }

    public static void generateUUIDAsyn(IUUIDListener listener, String productName, String ttid) {
        Logger.log_d("generateUUIDAsyn called");
        TVAppUUIDImpl._generateUUIDAsyn(listener, productName, ttid, 0);
    }

    public static void generateUUIDAsyn(IUUIDListener listener, String productName, String ttid, int licenseType) {
        Logger.log_d("generateUUIDAsyn called");
        TVAppUUIDImpl._generateUUIDAsyn(listener, productName, ttid, licenseType);
    }

    public static void setAndroidOnly(boolean only) {
        Logger.log_d("setAndroidOnly called");
        TVAppUUIDImpl.setAndroidOnly(only);
    }

    public static void setUseAndroidID(boolean use) {
        Logger.log_d("setUseAndroidID called");
        TVAppUUIDImpl.setUseAndroidID(use);
    }

    public static void setSMGAuthcode(String newauthcode) {
        TVAppUUIDImpl.setAUTHCODE(newauthcode);
    }

    public static Cursor queryProvider(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        return TVAppUUIDImpl.queryProvider(arg0, arg1, arg2, arg3, arg4);
    }
}
