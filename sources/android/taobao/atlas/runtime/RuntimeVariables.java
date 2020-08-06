package android.taobao.atlas.runtime;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.PreVerifier;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.taobao.atlas.R;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.FrameworkProperties;
import android.taobao.atlas.runtime.dialog.DefaultProgress;
import android.text.TextUtils;
import android.view.ViewGroup;
import java.io.Serializable;
import java.lang.reflect.Field;

public class RuntimeVariables implements Serializable {
    public static Class FrameworkPropertiesClazz;
    public static Application androidApplication;
    public static DelegateClassLoader delegateClassLoader;
    public static Resources delegateResources;
    private static String launchActivityName;
    public static Resources originalResources;
    public static int patchVersion = 2;
    public static String sApkPath;
    public static long sAppLastUpdateTime;
    public static Atlas.BundleVerifier sBundleVerifier;
    public static boolean sCachePreVersionBundles = false;
    public static String sCurrentProcessName;
    public static Object sDexLoadBooster;
    public static long sInstalledVersionCode;
    public static String sInstalledVersionName;
    public static ClassLoader sRawClassLoader;
    public static String sRealApplicationName;
    public static Atlas.ExternalBundleInstallReminder sReminder;
    public static boolean safeMode = false;

    static {
        if (Boolean.FALSE.booleanValue()) {
            String.valueOf(PreVerifier.class);
        }
    }

    public static Dialog alertDialogUntilBundleProcessed(Activity activity, String bundleName) {
        if (activity == null) {
            return null;
        }
        if (sReminder != null) {
            return sReminder.createReminderDialog(activity, bundleName);
        }
        Dialog dialog = new Dialog(activity, R.style.atlas_default_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        DefaultProgress progress = new DefaultProgress(activity);
        int size = (int) (96.0f * delegateResources.getDisplayMetrics().density);
        dialog.setContentView(progress, new ViewGroup.LayoutParams(size, size));
        return dialog;
    }

    public static boolean verifyBundle(String bundlePath) {
        if (sBundleVerifier == null) {
            return true;
        }
        return sBundleVerifier.verifyBundle(bundlePath);
    }

    public static Object getFrameworkProperty(String fieldName) {
        if (FrameworkPropertiesClazz == null) {
            FrameworkPropertiesClazz = FrameworkProperties.class;
        }
        try {
            Field field = FrameworkPropertiesClazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(FrameworkPropertiesClazz);
        } catch (Throwable th) {
            return null;
        }
    }

    public static String getProcessName(Context context) {
        return sCurrentProcessName;
    }

    public static boolean shouldSyncUpdateInThisProcess() {
        String processName = sCurrentProcessName;
        if (processName == null || (!processName.equals(androidApplication.getPackageName()) && !processName.toLowerCase().contains(":safemode"))) {
            return false;
        }
        return true;
    }

    public static ClassLoader getRawClassLoader() {
        if (sRawClassLoader != null) {
            return sRawClassLoader;
        }
        return RuntimeVariables.class.getClassLoader();
    }

    public static String getLauncherClassName() {
        if (!TextUtils.isEmpty(launchActivityName)) {
            return launchActivityName;
        }
        if (androidApplication == null) {
            return null;
        }
        Intent launchIntentForPackage = androidApplication.getPackageManager().getLaunchIntentForPackage(androidApplication.getPackageName());
        if (launchIntentForPackage != null) {
            launchActivityName = launchIntentForPackage.resolveActivity(androidApplication.getPackageManager()).getClassName();
        }
        return launchActivityName;
    }
}
