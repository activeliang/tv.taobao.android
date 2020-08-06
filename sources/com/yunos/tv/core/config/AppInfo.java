package com.yunos.tv.core.config;

import android.content.pm.PackageInfo;
import com.yunos.tv.core.CoreApplication;
import com.zhiping.dev.android.logger.ZpLogger;

public class AppInfo {
    public static final String HTTP_PARAMS_ENCODING = "UTF-8";
    private static String appVersionName;
    private static Integer appVersionNum;
    private static String packageName;

    public static String getPackageName() {
        if (packageName == null) {
            try {
                packageName = CoreApplication.getApplication().getPackageName();
            } catch (Exception e) {
                return null;
            }
        }
        return packageName;
    }

    public static int getAppVersionNum() {
        if (appVersionNum == null) {
            try {
                PackageInfo info = CoreApplication.getApplication().getPackageManager().getPackageInfo(CoreApplication.getApplication().getPackageName(), 0);
                appVersionNum = Integer.valueOf(info.versionCode);
                appVersionName = info.versionName;
            } catch (Exception e) {
                ZpLogger.e("SystemConfig-getAppVersion", "读取版本号异常: " + e.toString());
            }
        }
        return appVersionNum.intValue();
    }

    public static String getAppVersionName() {
        if (appVersionNum == null) {
            try {
                PackageInfo info = CoreApplication.getApplication().getPackageManager().getPackageInfo(CoreApplication.getApplication().getPackageName(), 0);
                appVersionNum = Integer.valueOf(info.versionCode);
                appVersionName = info.versionName;
            } catch (Exception e) {
                ZpLogger.e("SystemConfig-getAppVersion", "读取版本号异常: " + e.toString());
            }
        }
        return appVersionName;
    }
}
