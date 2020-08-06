package com.ta.audid.upload;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import com.alibaba.analytics.core.device.Constants;
import com.ta.audid.Variables;
import com.ta.audid.permission.PermissionUtils;
import com.ta.audid.utils.FileUtils;
import com.ta.audid.utils.UtUtils;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.AESUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UtdidKeyFile {
    private static final String AUDID_FILE_DIR = (Constants.PERSISTENT_CONFIG_DIR + File.separator + "Global");
    private static final String AUDID_FILE_NAME = "cec06585501c9775";
    private static final String SDCARD_DEVICE_MODLE_FILE_NAME = "322a309482c4dae6";
    private static final String UTDID_FILE_APPS = "c3de653fbca500f9";
    private static final String UTDID_FILE_APPUTDID = "4635b664f789000d";
    private static final String UTDID_FILE_DIR = ".7934039a7252be16";
    private static final String UTDID_FILE_LOCK = "9983c160aa044115";
    private static final String UTDID_FILE_OLDMODE = "719893c6fa359335";
    private static final String UTDID_FILE_UPLOADLOCK = "a325712a39bd320a";
    private static final String UTDID_FILE_UTDID = "7934039a7252be16";

    private static String getAudidFilePath() {
        if (PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return getUtdidSdcardRootFileDir() + File.separator + AUDID_FILE_NAME;
        }
        return null;
    }

    public static String readAudidFile() {
        try {
            String filePath = getAudidFilePath();
            if (TextUtils.isEmpty(filePath)) {
                return null;
            }
            String audid = FileUtils.readFile(filePath);
            if (TextUtils.isEmpty(audid) || audid.length() == 32 || audid.length() == 36) {
                return audid;
            }
            Map<String, String> pro = new HashMap<>();
            pro.put("len", "" + audid.length());
            pro.put("type", "read");
            UtUtils.sendUtdidMonitorEvent("audid", pro);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeAudidFile(String audid) {
        try {
            UtdidLogger.sd("", "audid:" + audid);
            String filePath = getAudidFilePath();
            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            if (TextUtils.isEmpty(audid) || audid.length() == 32 || audid.length() == 36) {
                FileUtils.saveFile(filePath, audid);
                return;
            }
            Map<String, String> pro = new HashMap<>();
            pro.put("len", "" + audid.length());
            pro.put("type", "write");
            UtUtils.sendUtdidMonitorEvent("audid", pro);
        } catch (Exception e) {
        }
    }

    private static String getSdcardUtdidFilePath() {
        if (PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return getUtdidSdcardRootFileDir() + File.separator + UTDID_FILE_UTDID;
        }
        return null;
    }

    public static String readSdcardUtdidFile() {
        try {
            String filePath = getSdcardUtdidFilePath();
            if (!TextUtils.isEmpty(filePath)) {
                return FileUtils.readFile(filePath);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeSdcardUtdidFile(String utdid) {
        try {
            String filePath = getSdcardUtdidFilePath();
            if (!TextUtils.isEmpty(filePath)) {
                FileUtils.saveFile(filePath, utdid);
            }
        } catch (Exception e) {
        }
    }

    private static String getSdcardDeviceModleFilePath() {
        if (PermissionUtils.checkStoragePermissionGranted(Variables.getInstance().getContext())) {
            return getUtdidSdcardRootFileDir() + File.separator + SDCARD_DEVICE_MODLE_FILE_NAME;
        }
        return null;
    }

    public static String readSdcardDeviceModleFile() {
        try {
            String filePath = getSdcardDeviceModleFilePath();
            if (!TextUtils.isEmpty(filePath)) {
                return FileUtils.readFile(filePath);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeSdcardDeviceModleFile(String sdcardDeviceModle) {
        try {
            String filePath = getSdcardDeviceModleFilePath();
            if (!TextUtils.isEmpty(filePath)) {
                FileUtils.saveFile(filePath, sdcardDeviceModle);
            }
        } catch (Exception e) {
        }
    }

    private static String getAppUtdidFilePath() {
        String appUtdidFilePath = getUtdidAppRootFileDir(Variables.getInstance().getContext()) + File.separator + UTDID_FILE_APPUTDID;
        UtdidLogger.sd("", appUtdidFilePath);
        return appUtdidFilePath;
    }

    private static String getAppsFilePath() {
        String appUtdidFilePath = getUtdidAppRootFileDir(Variables.getInstance().getContext()) + File.separator + UTDID_FILE_APPS;
        UtdidLogger.sd("", appUtdidFilePath);
        return appUtdidFilePath;
    }

    public static String getFileLockPath() {
        return getUtdidAppRootFileDir(Variables.getInstance().getContext()) + File.separator + UTDID_FILE_LOCK;
    }

    public static String getUploadFileLockPath() {
        return getUtdidAppRootFileDir(Variables.getInstance().getContext()) + File.separator + UTDID_FILE_UPLOADLOCK;
    }

    public static String getOldModeFilePath() {
        return getUtdidAppRootFileDir(Variables.getInstance().getContext()) + File.separator + UTDID_FILE_OLDMODE;
    }

    public static String readAppUtdidFile() {
        try {
            return FileUtils.readFile(getAppUtdidFilePath());
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeAppUtdidFile(String appUtdid) {
        try {
            UtdidLogger.d();
            FileUtils.saveFile(getAppUtdidFilePath(), appUtdid);
        } catch (Throwable th) {
        }
    }

    public static String readAppsFile() {
        try {
            return AESUtils.decrypt(FileUtils.readFile(getAppsFilePath()));
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeAppsFile(String apps) {
        try {
            UtdidLogger.sd("", apps);
            if (TextUtils.isEmpty(apps)) {
                new File(getAppsFilePath()).delete();
            } else {
                FileUtils.saveFile(getAppsFilePath(), AESUtils.encrypt(apps));
            }
        } catch (Throwable th) {
        }
    }

    private static String getUtdidSdcardRootFileDir() {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + AUDID_FILE_DIR;
        UtdidLogger.sd("", "SdcardRoot dir:" + dir);
        FileUtils.isDirExist(dir);
        return dir;
    }

    private static String getUtdidAppRootFileDir(Context context) {
        String dir = context.getFilesDir().getAbsolutePath() + File.separator + UTDID_FILE_DIR;
        UtdidLogger.sd("", "UtdidAppRoot dir:" + dir);
        FileUtils.isDirExist(dir);
        return dir;
    }

    public static void writeUtdidToSettings(Context context, String utdid) {
        String data = null;
        try {
            data = Settings.System.getString(context.getContentResolver(), UTDID_FILE_UTDID);
        } catch (Exception e) {
        }
        if (!utdid.equals(data)) {
            try {
                Settings.System.putString(context.getContentResolver(), UTDID_FILE_UTDID, utdid);
            } catch (Exception e2) {
            }
        }
    }

    public static String getUtdidFromSettings(Context context) {
        try {
            return Settings.System.getString(context.getContentResolver(), UTDID_FILE_UTDID);
        } catch (Exception e) {
            return null;
        }
    }
}
