package com.tvtaobao.voicesdk.utils;

import com.zhiping.dev.android.logger.ZpLogger;

public class LogPrint {
    private static String TAG_TVTAO = "TVTao_";

    public static void i(String tag, String msg) {
        ZpLogger.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        ZpLogger.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        ZpLogger.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        ZpLogger.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        ZpLogger.w(tag, msg);
    }
}
