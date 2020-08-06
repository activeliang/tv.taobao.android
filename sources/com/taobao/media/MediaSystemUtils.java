package com.taobao.media;

import android.app.Application;
import android.util.Log;

public class MediaSystemUtils {
    public static Application sApplication;
    public static boolean sIsApkDebug = true;
    public static boolean sIsSupportWeex = false;
    public static int sNoWifiNotice = 0;

    public static boolean isApkDebuggable() {
        boolean z;
        if (sApplication == null || !sIsApkDebug) {
            return false;
        }
        try {
            if ((sApplication.getApplicationInfo().flags & 2) != 0) {
                z = true;
            } else {
                z = false;
            }
            sIsApkDebug = z;
            return sIsApkDebug;
        } catch (Exception e) {
            Log.e("", e.getMessage());
            return false;
        }
    }
}
