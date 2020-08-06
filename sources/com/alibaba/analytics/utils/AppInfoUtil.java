package com.alibaba.analytics.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.util.HashMap;
import java.util.Map;

public class AppInfoUtil {
    private static final String TAG = "AppInfoUtil";
    private static String mAppkey;
    private static String mChannle = "";
    static Map<String, String> preInfoMap = null;

    public static String getLongLoginUsernick() {
        if (Variables.getInstance().getContext() == null) {
            return "";
        }
        try {
            String encodeNick = Variables.getInstance().getContext().getSharedPreferences("UTCommon", 0).getString("_lun", "");
            if (!TextUtils.isEmpty(encodeNick)) {
                return new String(Base64.decode(encodeNick.getBytes(), 2), "UTF-8");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLongLoingUserid() {
        if (Variables.getInstance().getContext() == null) {
            return "";
        }
        try {
            String encodeUid = Variables.getInstance().getContext().getSharedPreferences("UTCommon", 0).getString("_luid", "");
            if (!TextUtils.isEmpty(encodeUid)) {
                return new String(Base64.decode(encodeUid.getBytes(), 2), "UTF-8");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getChannel() {
        return Variables.getInstance().getChannel();
    }

    public static String getUsernick() {
        return "";
    }

    public static String getUserid() {
        return "";
    }

    public static String getAppkey() {
        return Variables.getInstance().getAppkey();
    }

    public static boolean isAppOnForeground(Context context) {
        if (context == null) {
            return false;
        }
        try {
            PowerManager pm = (PowerManager) context.getSystemService("power");
            String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
                if (appProcess.processName.equals(packageName)) {
                    if (appProcess.importance != 100 || !pm.isScreenOn()) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public static String getCurProcessName(Context context) {
        if (context == null) {
            return "";
        }
        int pid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String getString(Context context, String name) {
        int id;
        if (context == null) {
            return null;
        }
        try {
            Resources r = context.getResources();
            if (r == null || (id = r.getIdentifier(name, "string", context.getPackageName())) == 0) {
                return null;
            }
            return context.getString(id);
        } catch (Throwable th) {
            return null;
        }
    }

    public static Map<String, String> getInfoForPreApk(Context context) {
        if (preInfoMap != null) {
            return preInfoMap;
        }
        if (context == null) {
            return null;
        }
        preInfoMap = new HashMap();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("manufacture_config", 0);
            boolean preLoad = sharedPreferences.getBoolean("preLoad", false);
            String preLoad_VersionName = sharedPreferences.getString("preLoad_VersionName", "");
            String preLoad_Channel1 = sharedPreferences.getString("preLoad_Channel1", "");
            String preLoad_Channel2 = sharedPreferences.getString("preLoad_Channel2", "");
            if (preLoad) {
                preInfoMap.put("preLoad", "true");
                preInfoMap.put("preLoad_VersionName", preLoad_VersionName);
                preInfoMap.put("preLoad_Channel1", preLoad_Channel1);
                preInfoMap.put("preLoad_Channel2", preLoad_Channel2);
            }
        } catch (Exception e) {
        }
        return preInfoMap;
    }

    public static String getChannle2ForPreLoadApk(Context context) {
        Map<String, String> map = getInfoForPreApk(context);
        if (map != null) {
            return map.get("preLoad_Channel2");
        }
        return null;
    }
}
