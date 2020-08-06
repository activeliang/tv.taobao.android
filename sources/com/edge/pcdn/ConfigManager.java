package com.edge.pcdn;

import android.content.Context;
import android.content.SharedPreferences;
import com.taobao.ju.track.constants.Constants;

public class ConfigManager {
    public static final String CONFIG_LIVE_ACC_LOGSWITCH = "config_live_acc_logswitch";
    public static final String CONFIG_LIVE_ACC_START = "config_live_acc_start";
    public static final String CONFIG_LIVE_ACC_VERSION = "config_live_acc_version";
    public static final String CONFIG_LIVE_CHECKTIMESTAMP = "config_live_checktimestamp";
    public static final String CONFIG_LIVE_NEXTCHECK = "config_live_nextcheck";
    public static final String CONFIG_VOD_ACC_LOGSWITCH = "config_vod_acc_logswitch";
    public static final String CONFIG_VOD_ACC_START = "config_vod_acc_start";
    public static final String CONFIG_VOD_ACC_VERSION = "config_vod_acc_version";
    public static final String CONFIG_VOD_CHECKTIMESTAMP = "config_vod_checktimestamp";
    public static final String CONFIG_VOD_NEXTCHECK = "config_vod_nextcheck";
    public static final String REPORT_LOG_URL = "http://pss.alicdn.com/iku/log/acc?";
    public static final String UPGRADE_URL = "https://pns.alicdn.com/pcdn/s/check?os_name=android&ttype=android-mobile";
    private static SharedPreferences config;

    public static void init(Context context) {
        if (config == null) {
            config = context.getSharedPreferences("pcdnconfigs", 0);
        }
    }

    public static long getVodNextCheck() {
        if (config != null) {
            return config.getLong(CONFIG_VOD_NEXTCHECK, 0);
        }
        return 0;
    }

    public static void setVodNextCheck(long nextcheck) {
        SharedPreferences.Editor editor = config.edit();
        editor.putLong(CONFIG_VOD_NEXTCHECK, nextcheck);
        editor.apply();
    }

    public static long getVodCheckTimestamp() {
        if (config != null) {
            return config.getLong(CONFIG_VOD_CHECKTIMESTAMP, 0);
        }
        return 0;
    }

    public static void setVodCheckTimestamp(long timestamp) {
        SharedPreferences.Editor editor = config.edit();
        editor.putLong(CONFIG_VOD_CHECKTIMESTAMP, timestamp);
        editor.apply();
    }

    public static int getVodAccStart() {
        if (config != null) {
            return config.getInt(CONFIG_VOD_ACC_START, 1);
        }
        return 1;
    }

    public static void setVodAccStart(int start) {
        SharedPreferences.Editor editor = config.edit();
        editor.putInt(CONFIG_VOD_ACC_START, start);
        editor.apply();
    }

    public static String getVodAccVersion() {
        if (config != null) {
            return config.getString(CONFIG_VOD_ACC_VERSION, Constants.PARAM_OUTER_SPM_NONE);
        }
        return Constants.PARAM_OUTER_SPM_NONE;
    }

    public static void setVodAccVersion(String version) {
        SharedPreferences.Editor editor = config.edit();
        editor.putString(CONFIG_VOD_ACC_VERSION, version);
        editor.apply();
    }

    public static int getVodAccLogSwitch() {
        if (config != null) {
            return config.getInt(CONFIG_VOD_ACC_LOGSWITCH, 1);
        }
        return 1;
    }

    public static void setVodAccLogSwitch(int code) {
        SharedPreferences.Editor editor = config.edit();
        editor.putInt(CONFIG_VOD_ACC_LOGSWITCH, code);
        editor.apply();
    }

    public static long getLiveNextCheck() {
        if (config != null) {
            return config.getLong(CONFIG_LIVE_NEXTCHECK, 0);
        }
        return 0;
    }

    public static void setLiveNextCheck(long nextcheck) {
        SharedPreferences.Editor editor = config.edit();
        editor.putLong(CONFIG_LIVE_NEXTCHECK, nextcheck);
        editor.apply();
    }

    public static long getLiveCheckTimestamp() {
        if (config != null) {
            return config.getLong(CONFIG_LIVE_CHECKTIMESTAMP, 0);
        }
        return 0;
    }

    public static void setLiveCheckTimestamp(long timestamp) {
        SharedPreferences.Editor editor = config.edit();
        editor.putLong(CONFIG_LIVE_CHECKTIMESTAMP, timestamp);
        editor.apply();
    }

    public static int getLiveAccStart() {
        if (config != null) {
            return config.getInt(CONFIG_LIVE_ACC_START, 1);
        }
        return 1;
    }

    public static void setLiveAccStart(int start) {
        SharedPreferences.Editor editor = config.edit();
        editor.putInt(CONFIG_LIVE_ACC_START, start);
        editor.apply();
    }

    public static String getLiveAccVersion() {
        if (config != null) {
            return config.getString(CONFIG_LIVE_ACC_VERSION, Constants.PARAM_OUTER_SPM_NONE);
        }
        return Constants.PARAM_OUTER_SPM_NONE;
    }

    public static void setLiveAccVersion(String version) {
        SharedPreferences.Editor editor = config.edit();
        editor.putString(CONFIG_LIVE_ACC_VERSION, version);
        editor.apply();
    }

    public static int getLiveAccLogSwitch() {
        if (config != null) {
            return config.getInt(CONFIG_LIVE_ACC_LOGSWITCH, 1);
        }
        return 0;
    }

    public static void setLiveAccLogSwitch(int code) {
        SharedPreferences.Editor editor = config.edit();
        editor.putInt(CONFIG_LIVE_ACC_LOGSWITCH, code);
        editor.apply();
    }
}
