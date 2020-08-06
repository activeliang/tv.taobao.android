package android.taobao.windvane.webview;

import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;
import java.lang.reflect.Method;

public class WVRenderPolicy {
    public static boolean shouldDisableHardwareRenderInLayer() {
        boolean isSamsungGs4;
        boolean isJbMr2;
        if (Build.MODEL == null || !Build.MODEL.contains("GT-I95") || Build.MANUFACTURER == null || !Build.MANUFACTURER.equals("samsung")) {
            isSamsungGs4 = false;
        } else {
            isSamsungGs4 = true;
        }
        if (Build.VERSION.SDK_INT == 18) {
            isJbMr2 = true;
        } else {
            isJbMr2 = false;
        }
        if (!isSamsungGs4 || !isJbMr2) {
            return false;
        }
        return true;
    }

    public static void disableAccessibility(Context context) {
        if (Build.VERSION.SDK_INT == 17 && context != null) {
            try {
                AccessibilityManager am = (AccessibilityManager) context.getSystemService("accessibility");
                if (am.isEnabled()) {
                    Method setState = am.getClass().getDeclaredMethod("setState", new Class[]{Integer.TYPE});
                    setState.setAccessible(true);
                    setState.invoke(am, new Object[]{0});
                }
            } catch (Throwable th) {
            }
        }
    }
}
