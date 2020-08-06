package android.taobao.windvane.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.file.FileManager;
import android.text.TextUtils;
import java.io.File;
import java.nio.ByteBuffer;

public class ConfigStorage {
    public static final long DEFAULT_MAX_AGE = 21600000;
    public static final long DEFAULT_SMALL_MAX_AGE = 1800000;
    public static final String KEY_DATA = "wv-data";
    public static final String KEY_TIME = "wv-time";
    public static final String ROOT_WINDVANE_CONFIG_DIR = "windvane/config";
    /* access modifiers changed from: private */
    public static String TAG = "ConfigStorage";

    public static synchronized boolean initDirs() {
        boolean exists;
        synchronized (ConfigStorage.class) {
            if (GlobalConfig.context == null) {
                exists = false;
            } else {
                File fileDir = FileManager.createFolder(GlobalConfig.context, ROOT_WINDVANE_CONFIG_DIR);
                TaoLog.d(TAG, "createDir: dir[" + fileDir.getAbsolutePath() + "]:" + fileDir.exists());
                exists = fileDir.exists();
            }
        }
        return exists;
    }

    /* access modifiers changed from: private */
    public static String getFileAbsolutePath(String filename) {
        if (GlobalConfig.context == null) {
            return "";
        }
        StringBuilder path = new StringBuilder(128);
        path.append(GlobalConfig.context.getFilesDir().getAbsolutePath());
        path.append(File.separator);
        path.append(ROOT_WINDVANE_CONFIG_DIR);
        path.append(filename == null ? "" : File.separator + filename);
        return path.toString();
    }

    public static synchronized void putStringVal(final String spName, final String configKey, final String configVal) {
        synchronized (ConfigStorage.class) {
            if (!(spName == null || configKey == null)) {
                AsyncTask.execute(new Runnable() {
                    public void run() {
                        String nameTag = ConfigStorage.getConfigKey(spName, configKey);
                        try {
                            FileAccesser.write(ConfigStorage.getFileAbsolutePath(nameTag), ByteBuffer.wrap(configVal.getBytes()));
                        } catch (Exception e) {
                            TaoLog.e(ConfigStorage.TAG, "can not sava file : " + nameTag + " value : " + configVal);
                        }
                    }
                });
            }
        }
    }

    public static synchronized void putLongVal(final String spName, final String configKey, final long configVal) {
        synchronized (ConfigStorage.class) {
            if (!(spName == null || configKey == null)) {
                AsyncTask.execute(new Runnable() {
                    public void run() {
                        String nameTag = ConfigStorage.getConfigKey(spName, configKey);
                        String path = ConfigStorage.getFileAbsolutePath(nameTag);
                        try {
                            ByteBuffer buffer = ByteBuffer.allocate(8);
                            buffer.putLong(configVal);
                            FileAccesser.write(path, buffer);
                        } catch (Exception e) {
                            TaoLog.e(ConfigStorage.TAG, "can not sava file : " + nameTag + " value : " + configVal);
                        }
                    }
                });
            }
        }
    }

    public static String getStringVal(String spName, String configKey) {
        String nameTag = getConfigKey(spName, configKey);
        String value = "";
        try {
            if (new File(getFileAbsolutePath(nameTag)).exists()) {
                String value2 = new String(FileAccesser.read(new File(getFileAbsolutePath(nameTag))));
                try {
                    TaoLog.d(TAG, "read " + nameTag + " by file : " + value2);
                    value = value2;
                } catch (Exception e) {
                    value = value2;
                    TaoLog.e(TAG, "can not read file : " + nameTag);
                    return value;
                }
            } else {
                SharedPreferences sp = getSharedPreference();
                if (sp == null) {
                    return "";
                }
                value = sp.getString(nameTag, "");
                putStringVal(spName, configKey, value);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(nameTag);
                editor.commit();
                TaoLog.i(TAG, "read " + nameTag + " by sp : " + value);
            }
        } catch (Exception e2) {
            TaoLog.e(TAG, "can not read file : " + nameTag);
            return value;
        }
        return value;
    }

    public static long getLongVal(String spName, String configKey) {
        String nameTag = getConfigKey(spName, configKey);
        long value = 0;
        try {
            File file = new File(getFileAbsolutePath(nameTag));
            if (file.exists()) {
                byte[] data = FileAccesser.read(file);
                ByteBuffer buffer = ByteBuffer.allocate(8);
                buffer.put(data);
                buffer.flip();
                value = buffer.getLong();
                TaoLog.d(TAG, "read " + nameTag + " by file : " + value);
            } else {
                SharedPreferences sp = getSharedPreference();
                if (sp == null) {
                    return 0;
                }
                value = sp.getLong(nameTag, 0);
                putLongVal(spName, configKey, value);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(nameTag);
                editor.commit();
                TaoLog.i(TAG, "read " + nameTag + " by sp : " + value);
            }
        } catch (Exception e) {
            TaoLog.e(TAG, "can not read file : " + nameTag);
        }
        return value;
    }

    public static String getStringVal(String spName, String configKey, String defaultVal) {
        try {
            String value = getStringVal(spName, configKey);
            return TextUtils.isEmpty(value) ? defaultVal : value;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    public static long getLongVal(String spName, String configKey, long defaultVal) {
        try {
            Long value = Long.valueOf(getLongVal(spName, configKey));
            if (value.longValue() == 0) {
                return defaultVal;
            }
            return value.longValue();
        } catch (ClassCastException e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    /* access modifiers changed from: private */
    public static String getConfigKey(String spName, String configKey) {
        return "WindVane_" + spName + configKey;
    }

    private static SharedPreferences getSharedPreference() {
        if (GlobalConfig.context == null) {
            return null;
        }
        return PreferenceManager.getDefaultSharedPreferences(GlobalConfig.context);
    }
}
