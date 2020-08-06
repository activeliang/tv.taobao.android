package com.yunos.tv.blitz.video.data;

import android.util.Log;
import com.yunos.tv.blitz.video.VideoHelper;

public class Config {
    private static final String TAG = "Config";
    private static boolean debug = true;
    public static String mDeviceMedia;
    private static int mPlayerType = 1;
    private static String videoSystemConfigName = "/system/media/yingshi/yingshi_performance_config.json";

    static {
        initPlayerType();
    }

    public static void initPlayerType() {
        mPlayerType = VideoHelper.getSystemPlayerType();
        switch (mPlayerType) {
            case 1:
            case 2:
                break;
            default:
                mPlayerType = 1;
                break;
        }
        Log.i(TAG, "Config.initPlayerType mPlayerType=" + mPlayerType);
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug2) {
        debug = debug2;
    }

    public static String getVideoSystemConfigName() {
        return videoSystemConfigName;
    }

    public static void setVideoSystemConfigName(String videoSystemConfigName2) {
        videoSystemConfigName = videoSystemConfigName2;
    }

    public static int getPlayerType() {
        return mPlayerType;
    }

    public static void setPlayerType(int playerType) {
        mPlayerType = playerType;
    }
}
