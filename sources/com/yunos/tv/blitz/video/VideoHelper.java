package com.yunos.tv.blitz.video;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.yunos.tv.blitz.video.data.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONObject;

public class VideoHelper {
    private static final String TAG = "VideoHelper";

    public static String getSystemProperties(String key) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls.newInstance(), new Object[]{key});
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return null;
        } catch (InstantiationException e4) {
            e4.printStackTrace();
            return null;
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
            return null;
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
            return null;
        } catch (ClassNotFoundException e7) {
            e7.printStackTrace();
            return null;
        }
    }

    public static String getMediaParams() {
        String strProp = getSystemProperties("ro.media.ability");
        if (strProp == null || TextUtils.isEmpty(strProp)) {
            Log.w("media", "=====strProp null====");
            return "";
        }
        Log.d("media", "=====strProp====" + strProp);
        return strProp;
    }

    public static String getDeviceMedia(Context context) {
        if (TextUtils.isEmpty(Config.mDeviceMedia)) {
            Config.mDeviceMedia = getMediaParams() + ",drm";
            try {
                if (Config.getPlayerType() == 2 && !TextUtils.isEmpty(isAdoH265(context))) {
                    Config.mDeviceMedia += "," + isAdoH265(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "VideoHelper.getDeviceMedia value=" + Config.mDeviceMedia + ", playerType=" + Config.getPlayerType());
        }
        return Config.mDeviceMedia;
    }

    private static String isAdoH265(Context context) {
        try {
            return context.getApplicationContext().createPackageContext("com.yunos.adoplayer.service", 2).getSharedPreferences("adoplayer_ability_sharedpreferences", 1).getString("adoplayer.ability.h265.soft", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getSystemPlayerType() {
        String configFile = Config.getVideoSystemConfigName();
        int playerType = 1;
        InputStream inputStream = null;
        try {
            File file = new File(configFile);
            if (file.exists()) {
                Log.i(TAG, "readJson system config exit!" + configFile);
                try {
                    inputStream = new FileInputStream(file);
                } catch (Exception e) {
                    Log.e(TAG, "getAssets().open exception", e);
                    return 1;
                }
            } else {
                Log.i(TAG, "readJson fail! system config not exit!" + configFile);
            }
            if (inputStream != null) {
                JSONObject json = new JSONObject(IOUtils.readString(inputStream));
                Log.d(TAG, "readJson performance json content:" + json.toString());
                playerType = json.optInt("player_type", 2);
                return playerType;
            }
            Log.e(TAG, "readJson fail! inputStream is null!");
            return playerType;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
