package com.ta.audid.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;

public class AppInfoUtils {
    public static int getTargetSdkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getAppPackageName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.packageName;
        }
        return "";
    }

    public static String getAppVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isMainProcess(Context context) {
        String curProcessName = getCurProcessName(context);
        String appName = getAppPackageName(context);
        if (TextUtils.isEmpty(curProcessName) || TextUtils.isEmpty(appName) || curProcessName.equals(appName)) {
            return true;
        }
        return false;
    }

    public static boolean isMainLooper() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }

    public static Boolean isBelowMVersion() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return false;
    }
}
