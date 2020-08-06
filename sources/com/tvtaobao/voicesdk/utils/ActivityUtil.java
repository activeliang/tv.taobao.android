package com.tvtaobao.voicesdk.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.AppInfo;
import java.lang.ref.WeakReference;

public class ActivityUtil {
    private static WeakReference<Activity> mActivity;
    private static WeakReference<Dialog> mDialog;

    public static boolean isTopActivity(Context context, Class<?> activity) {
        ActivityManager am = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
        String className = "";
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.RunningAppProcessInfo topAppProcess = am.getRunningAppProcesses().get(0);
            if (topAppProcess != null && topAppProcess.importance == 100) {
                if (AppInfo.getPackageName().equals(topAppProcess.processName) && isRunningForeground(context) && mActivity != null && mActivity.get() != null) {
                    className = ((Activity) mActivity.get()).getClass().getName();
                }
            }
        } else {
            className = am.getRunningTasks(1).get(0).topActivity.getClassName();
        }
        LogPrint.d("ActivityUtil", "TopActivity.ClassName : " + className);
        LogPrint.d("ActivityUtil", "Activity.ClassName : " + activity.getName());
        return className.contains(activity.getName());
    }

    public static boolean isRunningForeground(Context context) {
        return CoreApplication.getApplication().getMyLifecycleHandler().isApplicationInForeground();
    }

    public static void addTopActivity(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    public static Context getTopActivity() {
        if (mActivity == null || mActivity.get() == null) {
            return null;
        }
        return (Context) mActivity.get();
    }

    public static void addVoiceDialog(Dialog dialog) {
        mDialog = new WeakReference<>(dialog);
    }

    public static Dialog getVoiceDialog() {
        if (mDialog == null || mDialog.get() == null) {
            return null;
        }
        return (Dialog) mDialog.get();
    }
}
