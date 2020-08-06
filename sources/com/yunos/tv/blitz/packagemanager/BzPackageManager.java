package com.yunos.tv.blitz.packagemanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.RemoteException;
import android.taobao.windvane.config.WVConfigManager;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.blitz.account.BzDebugLog;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BzPackageManager {
    private static final String RESULT_FAIL_KEY = "error";
    private static final String RESULT_SUCC_KEY = "result";
    private static final String TAG = "BzPackageManager";

    public static byte[] getAppIcon(String packageName) {
        BzDebugLog.i(TAG, "getAppIcon, " + packageName);
        try {
            Bitmap my_bitmap = ((BitmapDrawable) BzAppConfig.context.getContext().getApplicationContext().getPackageManager().getApplicationIcon(packageName)).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            my_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return stream.toByteArray();
        } catch (Exception e) {
            BzDebugLog.e(TAG, "getAppIcon error: " + e.toString());
            return null;
        }
    }

    public static String checkPermission(String param) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            String packageName = data.getString(CommonData.KEY_PACKAGE_NAME);
            resultJson.put("result", BzAppConfig.context.getContext().getApplicationContext().getPackageManager().checkPermission(data.getString("permissionName"), packageName));
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    public static String checkSignatures(String param) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            resultJson.put("result", BzAppConfig.context.getContext().getApplicationContext().getPackageManager().checkSignatures(data.getString("packageNameA"), data.getString("packageNameB")));
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    public static String getAppInfo(String param) {
        JSONObject resultJson = new JSONObject();
        PackageManager pm = BzAppConfig.context.getContext().getApplicationContext().getPackageManager();
        JSONObject infoJson = new JSONObject();
        try {
            String packageName = new JSONObject(param).optString(CommonData.KEY_PACKAGE_NAME);
            if (packageName.isEmpty()) {
                packageName = BzAppConfig.context.getContext().getPackageName();
            }
            PackageInfo info = pm.getPackageInfo(packageName, 0);
            infoJson.putOpt("firstInstallTime", Long.valueOf(info.firstInstallTime));
            infoJson.putOpt("lastUpdateTime", Long.valueOf(info.lastUpdateTime));
            infoJson.putOpt(CommonData.KEY_PACKAGE_NAME, info.packageName);
            infoJson.putOpt(WVConfigManager.CONFIGNAME_PACKAGE, info.packageName);
            infoJson.putOpt("versionCode", Integer.valueOf(info.versionCode));
            infoJson.putOpt("versionName", info.versionName);
            if (info.applicationInfo != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                infoJson.putOpt("dataDir", appInfo.dataDir);
                infoJson.putOpt("enabled", Boolean.valueOf(appInfo.enabled));
                infoJson.putOpt("label", pm.getApplicationLabel(appInfo));
            }
            infoJson.putOpt("icon", "file:///blitz_app_icon/" + info.packageName);
            resultJson.put("result", infoJson);
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    public static String getInstalledApps(String param) {
        JSONObject resultJson = new JSONObject();
        PackageManager pm = BzAppConfig.context.getContext().getApplicationContext().getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        JSONArray infoJsonArray = new JSONArray();
        try {
            for (PackageInfo info : packageInfos) {
                JSONObject infoJson = new JSONObject();
                infoJson.putOpt("firstInstallTime", Long.valueOf(info.firstInstallTime));
                infoJson.putOpt("lastUpdateTime", Long.valueOf(info.lastUpdateTime));
                infoJson.putOpt(CommonData.KEY_PACKAGE_NAME, info.packageName);
                infoJson.putOpt(WVConfigManager.CONFIGNAME_PACKAGE, info.packageName);
                infoJson.putOpt("versionCode", Integer.valueOf(info.versionCode));
                infoJson.putOpt("versionName", info.versionName);
                if (info.applicationInfo != null) {
                    ApplicationInfo appInfo = info.applicationInfo;
                    infoJson.putOpt("dataDir", appInfo.dataDir);
                    infoJson.putOpt("enabled", Boolean.valueOf(appInfo.enabled));
                    infoJson.putOpt("label", pm.getApplicationLabel(appInfo));
                    infoJson.putOpt("launcher", Boolean.valueOf((appInfo.flags & 1) <= 0));
                    infoJson.putOpt("flags", Integer.valueOf(appInfo.flags));
                }
                infoJson.putOpt("icon", "file:///blitz_app_icon/" + info.packageName);
                infoJsonArray.put(infoJson);
            }
            resultJson.put("result", infoJsonArray);
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    public static String install(String param) {
        BzDebugLog.i(TAG, "param = " + param);
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            String path = data.getString(TuwenConstants.PARAMS.SKU_PATH);
            boolean silently = data.optBoolean("silent", true);
            int installFlags = data.optInt("flag", 0);
            String dataPth = BzAppConfig.context.getContext().getFilesDir().getParent();
            File file = new File(path);
            if (!file.exists() || file.length() == 0) {
                path = dataPth + path;
                File file2 = new File(path);
                if (!file2.exists() || file2.length() == 0) {
                    resultJson.put(RESULT_FAIL_KEY, "installSilence :" + path + " , but file not exist");
                    return resultJson.toString();
                }
            }
            if (silently) {
                installSilence(path, installFlags, resultJson);
            } else {
                installByIntent(path, resultJson);
            }
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    private static void installByIntent(String path, JSONObject resultJson) throws JSONException {
        Intent intent = new Intent("android.intent.action.VIEW");
        BzDebugLog.i(TAG, "install path = " + path);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        BzAppConfig.context.getContext().startActivity(intent);
        resultJson.put("result", BlitzServiceUtils.CSUCCESS);
    }

    private static synchronized void installSilence(String apkPath, int installFlags, JSONObject resultJson) throws JSONException {
        synchronized (BzPackageManager.class) {
            String dataPth = BzAppConfig.context.getContext().getFilesDir().getParent();
            BzDebugLog.i(TAG, "installSilence apkPath = " + apkPath + ", dataPath = " + dataPth);
            File file = new File(apkPath);
            if (apkPath.startsWith(dataPth)) {
                file.setReadable(true, false);
            }
            Uri mPackageURI = Uri.fromFile(file);
            PackageManager pm = BzAppConfig.context.getContext().getApplicationContext().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, 1);
            if (info != null) {
                try {
                    if (pm.getPackageInfo(info.packageName, 8192) != null) {
                        installFlags |= 2;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    BzDebugLog.v(TAG, "AppOperator.installSilence," + info.packageName + " not installed before.");
                }
                try {
                    pm.installPackage(mPackageURI, new PackageInstallObserver(), installFlags, info.packageName);
                    resultJson.put("result", BlitzServiceUtils.CSUCCESS);
                } catch (Exception e2) {
                    BzDebugLog.e(TAG, "installPackage " + info.packageName + e2.toString());
                    resultJson.put(RESULT_FAIL_KEY, e2.toString());
                }
            }
        }
        return;
    }

    public static String uninstall(String param) {
        BzDebugLog.i(TAG, "param = " + param);
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject data = new JSONObject(param);
            String packageName = data.getString(CommonData.KEY_PACKAGE_NAME);
            if (data.optBoolean("silent", true)) {
                uninstallSilence(packageName, resultJson);
            } else {
                uninstallByIntent(packageName, resultJson);
            }
        } catch (Exception e) {
            try {
                resultJson.put(RESULT_FAIL_KEY, e.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        BzDebugLog.i(TAG, "result = " + resultJson.toString());
        return resultJson.toString();
    }

    private static void uninstallByIntent(String packageName, JSONObject resultJson) throws JSONException {
        Intent intent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + packageName));
        intent.setFlags(268435456);
        BzAppConfig.context.getContext().startActivity(intent);
        resultJson.put("result", BlitzServiceUtils.CSUCCESS);
    }

    private static void uninstallSilence(String packageName, JSONObject resultJson) throws JSONException {
        BzDebugLog.i(TAG, "unInstallSilence :" + packageName);
        if (packageName.isEmpty()) {
            resultJson.put(RESULT_FAIL_KEY, "empty packageName.");
            return;
        }
        try {
            BzAppConfig.context.getContext().getApplicationContext().getPackageManager().deletePackage(packageName, new PackageDeleteObserver(), 0);
            resultJson.put("result", "succes");
        } catch (Exception e) {
            BzDebugLog.w(TAG, "SecurityException when pm.deletePackage ." + packageName);
            resultJson.put(RESULT_FAIL_KEY, e.toString());
        }
    }

    static class PackageInstallObserver extends IPackageInstallObserver.Stub {
        boolean finished;
        int result;

        PackageInstallObserver() {
        }

        public void packageInstalled(String name, int status) {
            synchronized (this) {
                this.finished = true;
                this.result = status;
                notifyAll();
            }
        }
    }

    static class PackageDeleteObserver extends IPackageDeleteObserver.Stub {
        boolean finished;
        int result;

        PackageDeleteObserver() {
        }

        public void packageDeleted(String name, int status) throws RemoteException {
            synchronized (this) {
                this.finished = true;
                this.result = status;
                notifyAll();
            }
        }
    }
}
