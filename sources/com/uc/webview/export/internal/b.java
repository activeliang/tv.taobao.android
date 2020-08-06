package com.uc.webview.export.internal;

import android.content.Context;
import com.uc.webview.export.extension.BreakpadConfig;
import com.uc.webview.export.internal.interfaces.IBreakpad;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import java.lang.reflect.Field;

/* compiled from: ProGuard */
public final class b {
    private static boolean a = false;
    private static boolean b = false;
    private static IBreakpad c;

    public static void a(Context context, String str, BreakpadConfig breakpadConfig) {
        if (!a) {
            a = true;
            Class[] clsArr = {Context.class, String.class, String.class, String.class};
            Object[] objArr = {context, str, breakpadConfig.mCrashDir, breakpadConfig.mUploadUrl};
            Class<?> cls = null;
            try {
                cls = Class.forName("com.uc.webview.browser.internal.breakpad.BreakpadImpl", true, d.c);
                if (breakpadConfig.mCrashLogPrefix != null && breakpadConfig.mCrashLogPrefix.trim().length() > 0) {
                    Field declaredField = cls.getDeclaredField("sCrashLogPrefix");
                    declaredField.setAccessible(true);
                    declaredField.set((Object) null, breakpadConfig.mCrashLogPrefix.trim());
                }
            } catch (Throwable th) {
            }
            try {
                ReflectionUtil.invoke(cls, "loadBreakpadLibrary", clsArr, objArr);
                b = true;
            } catch (Throwable th2) {
            }
            if (b && c == null) {
                c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
            }
            if (c != null) {
                if (b && c == null) {
                    c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
                }
                c.setCrashLogFileName(breakpadConfig.mCrashLogFileName);
                try {
                    Class[] clsArr2 = {Boolean.TYPE};
                    Object[] objArr2 = {Boolean.valueOf(breakpadConfig.mEnableEncryptLog)};
                    if (b && c == null) {
                        c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
                    }
                    ReflectionUtil.invoke((Object) c, "enableEncryptLog", clsArr2, objArr2);
                    if (!breakpadConfig.mEnableJavaLog) {
                        Class[] clsArr3 = {Integer.TYPE};
                        Object[] objArr3 = {16};
                        if (b && c == null) {
                            c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
                        }
                        ReflectionUtil.invoke((Object) c, "disableLog", clsArr3, objArr3);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void a() {
        if (b && c == null) {
            c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
        }
        if (c != null) {
            if (b && c == null) {
                c = (IBreakpad) ReflectionUtil.newInstanceNoThrow("com.uc.webview.browser.internal.breakpad.BreakpadImpl");
            }
            c.uploadCrashLogs();
        }
    }
}
