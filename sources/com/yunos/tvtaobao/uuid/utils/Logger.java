package com.yunos.tvtaobao.uuid.utils;

import android.util.Log;
import com.yunos.tvtaobao.uuid.constants.Constants;

public class Logger {
    public static String TAG = "APPUUID";

    public static void log(String str) {
        Log.d(TAG, str);
    }

    public static void log_d(String str) {
        if (Constants.DEBUG) {
            Log.d(TAG, str);
        }
    }

    public static void loge(String str) {
        loge(str, (Throwable) null);
    }

    public static void loge(String str, Throwable tr) {
        Log.e(TAG, str, tr);
    }
}
