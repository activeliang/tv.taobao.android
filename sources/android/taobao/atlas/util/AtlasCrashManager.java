package android.taobao.atlas.util;

import android.taobao.atlas.runtime.RuntimeVariables;
import android.util.Log;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AtlasCrashManager {
    /* access modifiers changed from: private */
    public static Thread.UncaughtExceptionHandler defaultUnCaughtExceptionHandler;

    public static void forceStopAppWhenCrashed() {
        defaultUnCaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    public static class CrashHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                Log.e("CrashManager", "force stop");
                Field mPMField = RuntimeVariables.androidApplication.getPackageManager().getClass().getDeclaredField("mPM");
                mPMField.setAccessible(true);
                Object mPM = mPMField.get(RuntimeVariables.androidApplication.getPackageManager());
                Method setPackageStoppedState = mPM.getClass().getDeclaredMethod("setPackageStoppedState", new Class[]{String.class, Boolean.TYPE, Integer.TYPE});
                setPackageStoppedState.setAccessible(true);
                setPackageStoppedState.invoke(mPM, new Object[]{RuntimeVariables.androidApplication.getPackageName(), true, Integer.valueOf(RuntimeVariables.androidApplication.getApplicationInfo().uid)});
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (AtlasCrashManager.defaultUnCaughtExceptionHandler != null) {
                AtlasCrashManager.defaultUnCaughtExceptionHandler.uncaughtException(thread, ex);
            }
        }
    }
}
