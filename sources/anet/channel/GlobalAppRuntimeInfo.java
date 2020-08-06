package anet.channel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import anet.channel.util.ProxySetting;
import anet.channel.util.Utils;

public class GlobalAppRuntimeInfo {
    private static final String TAG = "awcn.GlobalAppRuntimeInfo";
    private static final String USER_ID = "UserId";
    private static Context context;
    private static String currentProcess = "";
    private static ENV env = ENV.ONLINE;
    public static volatile boolean isBackground = true;
    public static String mConnToken = null;
    private static volatile ProxySetting proxySetting = null;
    private static SharedPreferences sp = null;
    private static String targetProcess = "";
    private static String ttid;
    private static String userId;
    private static String utdid;

    public static void setContext(Context context2) {
        context = context2;
        if (context2 != null) {
            if (TextUtils.isEmpty(currentProcess)) {
                currentProcess = Utils.getProcessName(context2, Process.myPid());
            }
            if (TextUtils.isEmpty(targetProcess)) {
                targetProcess = Utils.getMainProcessName(context2);
            }
            if (sp == null) {
                sp = PreferenceManager.getDefaultSharedPreferences(context2);
                userId = sp.getString(USER_ID, (String) null);
            }
            ALog.e(TAG, "", (String) null, "CurrentProcess", currentProcess, "TargetProcess", targetProcess);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setTargetProcess(String process) {
        targetProcess = process;
    }

    public static boolean isTargetProcess() {
        if (TextUtils.isEmpty(targetProcess) || TextUtils.isEmpty(currentProcess)) {
            return true;
        }
        return targetProcess.equalsIgnoreCase(currentProcess);
    }

    public static String getCurrentProcess() {
        return currentProcess;
    }

    public static void setCurrentProcess(String processName) {
        currentProcess = processName;
    }

    public static void setEnv(ENV env2) {
        env = env2;
    }

    public static ENV getEnv() {
        return env;
    }

    public static void setTtid(String ttid2) {
        ttid = ttid2;
    }

    public static String getTtid() {
        return ttid;
    }

    public static void setUserId(String userId2) {
        if (userId == null || !userId.equals(userId2)) {
            userId = userId2;
            StrategyCenter.getInstance().forceRefreshStrategy(DispatchConstants.getAmdcServerDomain());
            if (sp != null) {
                sp.edit().putString(USER_ID, userId2).apply();
            }
        }
    }

    public static String getUserId() {
        return userId;
    }

    @Deprecated
    public static void setUtdid(String utdid2) {
    }

    public static String getUtdid() {
        if (utdid == null && context != null) {
            utdid = Utils.getDeviceId(context);
        }
        return utdid;
    }

    public static void setBackground(boolean isBackground2) {
        isBackground = isBackground2;
    }

    public static boolean isAppBackground() {
        if (context == null) {
            return true;
        }
        return isBackground;
    }

    public static void setProxySetting(ProxySetting proxySetting2) {
        proxySetting = proxySetting2;
    }

    public static ProxySetting getProxySetting() {
        return proxySetting;
    }
}
