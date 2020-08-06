package com.uc.webview.export.cyclone;

import android.content.Context;
import android.os.Build;
import dalvik.system.BaseDexClassLoader;
import java.io.FileOutputStream;
import java.util.HashSet;

/* compiled from: ProGuard */
public class UCLibrary {
    private static final HashSet<String> a = new HashSet<>(2);

    private UCLibrary() {
    }

    public static void load(Context context, String str, ClassLoader classLoader) {
        FileOutputStream fileOutputStream;
        if (UCCyclone.loadLibraryCallback != null) {
            UCCyclone.loadLibraryCallback.onReceiveValue(str);
        } else if (Build.VERSION.SDK_INT < 23 || !"N".equalsIgnoreCase(Build.VERSION.RELEASE)) {
            System.load(str);
        } else if (UCLibrary.class.getClassLoader() instanceof BaseDexClassLoader) {
            System.load(str);
        } else {
            try {
                System.load(str);
            } catch (Throwable th) {
                throw new UCKnownException(6016, th);
            }
        }
    }
}
