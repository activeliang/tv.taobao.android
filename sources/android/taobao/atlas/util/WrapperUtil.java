package android.taobao.atlas.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class WrapperUtil {
    static final String TAG = "Utils";
    static HashMap<String, Object> keyPointLog = new HashMap<>();

    public static boolean isDebugMode(Application application) {
        return (application.getApplicationInfo().flags & 2) != 0;
    }

    public static PackageInfo getPackageInfo(Application mApplication) {
        try {
            return mApplication.getPackageManager().getPackageInfo(mApplication.getPackageName(), 0);
        } catch (Exception e) {
            Log.e(TAG, "Error to get PackageInfo >>>", e);
            PackageInfo packageInfo = new PackageInfo();
            packageInfo.versionName = "";
            packageInfo.versionCode = 1;
            return packageInfo;
        }
    }

    public static void persisitKeyPointLog(String newVersion) {
        RuntimeVariables.androidApplication.getSharedPreferences("atlas_log", 0).edit().putString(newVersion, keyPointLog.toString()).commit();
    }

    public static String getLastDDUpdateKeyPointLog(String currentVersion) {
        return RuntimeVariables.androidApplication.getSharedPreferences("atlas_log", 0).getString(currentVersion, "");
    }

    public static void clearLastDDUpdateKeyPointLog(String currentVersion) {
        SharedPreferences preferences = RuntimeVariables.androidApplication.getSharedPreferences("atlas_log", 0);
        preferences.edit().clear();
        preferences.edit().commit();
    }

    public static void appendLog(String key, Object value) {
        keyPointLog.put(key, value);
    }

    public static Map<String, Object> getKeyPointLog() {
        return keyPointLog;
    }
}
