package com.ali.user.open.core.trace;

import android.content.Context;
import android.util.Log;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDKLogger {
    private static final String TAG_PREFIX = "AliMemberSDK_";

    public static void i(String tag, String msg) {
        try {
            if (isDebugEnabled()) {
                Log.i(TAG_PREFIX + tag, msg + appendCurrentTime());
            }
        } catch (Throwable th) {
        }
    }

    public static void d(String tag, String msg) {
        try {
            if (isDebugEnabled()) {
                Log.d(TAG_PREFIX + tag, msg + appendCurrentTime());
            }
        } catch (Throwable th) {
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        try {
            if (isDebugEnabled()) {
                Log.d(TAG_PREFIX + tag, msg + appendCurrentTime(), e);
            }
        } catch (Throwable th) {
        }
    }

    public static void w(String tag, String msg) {
        try {
            if (isDebugEnabled()) {
                Log.w(TAG_PREFIX + tag, msg + appendCurrentTime());
            }
        } catch (Throwable th) {
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        try {
            if (isDebugEnabled()) {
                Log.e(TAG_PREFIX + tag, msg + appendCurrentTime(), e);
            }
        } catch (Throwable th) {
        }
    }

    public static void e(String tag, String msg) {
        try {
            if (isDebugEnabled()) {
                Log.e(TAG_PREFIX + tag, msg + appendCurrentTime());
            }
        } catch (Throwable th) {
        }
    }

    public static boolean isDebugEnabled() {
        return ConfigManager.DEBUG || isApkDebugable(KernelContext.getApplicationContext());
    }

    public static boolean isApkDebugable(Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static String appendCurrentTime() {
        return "\n" + "time =" + getDateStringByMill();
    }

    private static String getDateStringByMill() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(System.currentTimeMillis()));
    }
}
