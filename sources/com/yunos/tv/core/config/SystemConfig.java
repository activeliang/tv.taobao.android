package com.yunos.tv.core.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.edge.pcdnpar.BuildConfig;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemConfig {
    public static String APP_VERSION = null;
    public static Integer APP_VERSION_NUMBER = null;
    public static Float DENSITY = null;
    public static Integer DENSITY_DPI = null;
    public static final String HTTP_PARAMS_ENCODING = "UTF-8";
    public static Integer SCREEN_HEIGHT;
    public static Integer SCREEN_WIDTH;
    public static String SYSTEM_VERSION;
    public static boolean SYSTEM_YUNOS_4_0;
    private static String TAG = "SystemConfig";
    private static Boolean hasInited = false;

    public static void init(Context context) {
        if (!hasInited.booleanValue()) {
            if (context == null) {
                throw new IllegalArgumentException("The context was null");
            }
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            DENSITY = Float.valueOf(dm.density);
            DENSITY_DPI = Integer.valueOf(dm.densityDpi);
            SCREEN_WIDTH = Integer.valueOf(dm.widthPixels);
            SCREEN_HEIGHT = Integer.valueOf(dm.heightPixels);
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                APP_VERSION_NUMBER = Integer.valueOf(info.versionCode);
                APP_VERSION = info.versionName;
            } catch (Exception e) {
                ZpLogger.e("SystemConfig-getAppVersion", "读取版本号异常: " + e.toString());
            }
            SYSTEM_YUNOS_4_0 = isYunOS40System();
            SYSTEM_VERSION = getSystemVersion();
            ZpLogger.v(TAG, TAG + ".init.versionCode = " + APP_VERSION_NUMBER + ", versionName = " + APP_VERSION + ", DisplayMetrics = " + dm + ", SYSTEM_YUNOS_4_0 = " + SYSTEM_YUNOS_4_0 + ",SYSTEM_VERSION = " + SYSTEM_VERSION);
            hasInited = true;
        }
    }

    public static boolean isYunOS40System() {
        String res_version = getSystemProp("android.os.SystemProperties", "ro.yunos.version");
        ZpLogger.i(TAG, TAG + ".isYunOS40System.res_version = " + res_version);
        if (TextUtils.isEmpty(res_version) || res_version.compareToIgnoreCase(BuildConfig.VERSION_NAME) < 0) {
            return false;
        }
        return true;
    }

    public static String getSystemVersion() {
        String res_version = getSystemProp("android.os.SystemProperties", "ro.yunos.build.version.release");
        if (TextUtils.isEmpty(res_version)) {
            res_version = Build.VERSION.RELEASE;
        }
        if (TextUtils.isEmpty(res_version)) {
            res_version = getSystemProp("android.os.SystemProperties", "ro.build.version.release");
        }
        if (TextUtils.isEmpty(res_version)) {
            res_version = getSystemProp("android.os.SystemProperties", "ro.yunos.version");
        }
        if (!TextUtils.isEmpty(res_version)) {
            return res_version;
        }
        ZpLogger.e(TAG, TAG + "getSystemVersion.res_version = " + "");
        return "";
    }

    public static String getSystemProp(String className, String propName) {
        Method method;
        try {
            Class<?> props = Class.forName(className);
            if (props == null || (method = props.getMethod("get", new Class[]{String.class})) == null) {
                return null;
            }
            return (String) method.invoke(props.newInstance(), new Object[]{propName});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        } catch (InstantiationException e6) {
            e6.printStackTrace();
            return null;
        } catch (Throwable e7) {
            e7.printStackTrace();
            return null;
        }
    }
}
