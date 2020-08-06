package com.ali.auth.third.core.trace;

import android.content.Context;
import android.util.Log;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.message.Message;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SDKLogger {
    private static final String TAG_PREFIX = "AuthSDK_";

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

    public static void log(String tag, Message message, Throwable stackTrace) {
        if (isDebugEnabled()) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("***********************************************************\n");
            messageBuilder.append("错误编码 = ").append(message.code).append("\n");
            messageBuilder.append("错误消息 = ").append(message.message).append("\n");
            messageBuilder.append("解决建议 = ").append(message.action).append("\n");
            if (stackTrace != null) {
                messageBuilder.append("错误堆栈 = ").append(Log.getStackTraceString(stackTrace)).append("\n");
            }
            messageBuilder.append("***********************************************************\n");
            String type = message.type;
            if ("D".equals(type)) {
                d(tag, messageBuilder.toString());
            } else if ("E".equals(type)) {
                e(tag, messageBuilder.toString());
            } else if ("W".equals(type)) {
                w(tag, messageBuilder.toString());
            } else {
                i(tag, messageBuilder.toString());
            }
        }
    }

    public static void log(String tag, Message message) {
        log(tag, message, (Throwable) null);
    }

    private static String appendCurrentTime() {
        return "\n" + "time =" + getDateStringByMill();
    }

    private static String getDateStringByMill() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(System.currentTimeMillis()));
    }
}
