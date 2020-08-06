package com.uc.webview.export.internal.utility;

import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.uc.webview.export.annotations.Interface;

@Interface
/* compiled from: ProGuard */
public class Log {
    public static boolean sPrintLog = false;

    private Log() {
    }

    public static void d(String str, String str2) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "d", new Class[]{String.class, String.class}, new Object[]{str, str2});
            } catch (Exception e) {
            }
        }
    }

    public static void d(String str, String str2, Throwable th) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "d", new Class[]{String.class, String.class, Throwable.class}, new Object[]{str, str2, th});
            } catch (Exception e) {
            }
        }
    }

    public static void e(String str, String str2) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "e", new Class[]{String.class, String.class}, new Object[]{str, str2});
            } catch (Exception e) {
            }
        }
    }

    public static void e(String str, String str2, Throwable th) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "e", new Class[]{String.class, String.class, Throwable.class}, new Object[]{str, str2, th});
            } catch (Exception e) {
            }
        }
    }

    public static void i(String str, String str2) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", UploadQueueMgr.MSGTYPE_INTERVAL, new Class[]{String.class, String.class}, new Object[]{str, str2});
            } catch (Exception e) {
            }
        }
    }

    public static void i(String str, String str2, Throwable th) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", UploadQueueMgr.MSGTYPE_INTERVAL, new Class[]{String.class, String.class, Throwable.class}, new Object[]{str, str2, th});
            } catch (Exception e) {
            }
        }
    }

    public static void w(String str, String str2) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "w", new Class[]{String.class, String.class}, new Object[]{str, str2});
            } catch (Exception e) {
            }
        }
    }

    public static void w(String str, String str2, Throwable th) {
        if (sPrintLog) {
            try {
                ReflectionUtil.invoke("android.util.Log", "w", new Class[]{String.class, String.class, Throwable.class}, new Object[]{str, str2, th});
            } catch (Exception e) {
            }
        }
    }
}
