package com.ta.audid.permission;

import android.content.Context;
import android.os.Build;
import com.ta.audid.utils.AppInfoUtils;

public class PermissionUtils {
    public static boolean checkStoragePermissionGranted(Context context) {
        return selfPermissionGranted(context, "android.permission.WRITE_EXTERNAL_STORAGE");
    }

    public static boolean checkReadPhoneStatePermissionGranted(Context context) {
        return selfPermissionGranted(context, "android.permission.READ_PHONE_STATE");
    }

    public static boolean checkWifiStatePermissionGranted(Context context) {
        return selfPermissionGranted(context, "android.permission.ACCESS_WIFI_STATE");
    }

    public static boolean selfPermissionGranted(Context context, String permission) {
        boolean result = true;
        if (context == null) {
            return false;
        }
        int targetSdkVersion = AppInfoUtils.getTargetSdkVersion(context);
        if (Build.VERSION.SDK_INT >= 23) {
            if (targetSdkVersion >= 23) {
                if (context.checkSelfPermission(permission) != 0) {
                    result = false;
                }
            } else if (PermissionChecker.checkSelfPermission(context, permission) != 0) {
                result = false;
            }
        } else if (PermissionChecker.checkSelfPermission(context, permission) != 0) {
            result = false;
        }
        return result;
    }
}
