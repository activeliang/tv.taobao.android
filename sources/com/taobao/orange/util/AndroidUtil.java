package com.taobao.orange.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.detail.domain.tuwen.TuwenConstants;

public class AndroidUtil {
    private static final String TAG = "AndroidUtil";
    private static final String TAOBAO_PACKAGE_NAME = "com.taobao.taobao";
    private static final String TMALL_PACKAGE_NAME = "com.tmall.wireless";
    private static String currentProcess = "";
    private static String mainProcess = "";

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return true;
        }
        try {
            if (TextUtils.isEmpty(mainProcess)) {
                mainProcess = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.processName;
                OLog.d(TAG, "isMainProcess", "mainProcessName", mainProcess);
            }
            if (TextUtils.isEmpty(currentProcess)) {
                currentProcess = getProcessName(context, Process.myPid());
                OLog.d(TAG, "isMainProcess", "currentProcessName", currentProcess);
            }
            if (TextUtils.isEmpty(mainProcess) || TextUtils.isEmpty(currentProcess)) {
                return true;
            }
            return mainProcess.equalsIgnoreCase(currentProcess);
        } catch (Throwable t) {
            OLog.e(TAG, "isMainProcess", t, new Object[0]);
            return true;
        }
    }

    private static String getProcessName(Context context, int pID) {
        String processName = "";
        for (ActivityManager.RunningAppProcessInfo info : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public static void setThreadPriority() {
        try {
            Process.setThreadPriority(2);
        } catch (Throwable t) {
            OLog.e(TAG, "setThreadPriority", t, new Object[0]);
        }
    }

    public static boolean isTaobaoPackage(Context context) {
        if (context == null) {
            return false;
        }
        String packageName = context.getPackageName();
        if (TAOBAO_PACKAGE_NAME.equals(packageName) || TMALL_PACKAGE_NAME.equals(packageName)) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            try {
                NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            } catch (Exception e) {
            }
        }
        return false;
    }
}
