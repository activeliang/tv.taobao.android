package com.taobao.utils;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Keep
public final class Global {
    public static final long StartupTime = SystemClock.uptimeMillis();
    private static Application sApplication;
    private static String sPackageName;
    private static String sVersionName;

    public static synchronized Application getApplication() {
        Application application;
        synchronized (Global.class) {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
            application = sApplication;
        }
        return application;
    }

    public static String getPackageName() {
        if (TextUtils.isEmpty(sPackageName) && getApplication() != null) {
            sPackageName = getApplication().getPackageName();
        }
        return sPackageName;
    }

    private static Application getSystemApp() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            return (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(sVersionName)) {
            try {
                sVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (Exception e) {
                Log.e("VersionInfo", "Exception", e);
            }
        }
        return sVersionName;
    }

    public static synchronized void setApplication(Application application) {
        synchronized (Global.class) {
            sApplication = application;
        }
    }
}
