package com.taobao.muniontaobaosdk.util;

import android.support.annotation.Keep;
import android.util.Log;

@Keep
public final class TaoLog {
    private static a sCopyLogger = null;

    public interface a {
        void a(String str, String str2);

        void b(String str, String str2);

        void c(String str, String str2);
    }

    public static void Logd(String str, String str2) {
        Log.d(str, str2);
        if (sCopyLogger != null) {
            sCopyLogger.a(str, str2);
        }
    }

    public static void Loge(String str, String str2) {
        Log.e(str, str2);
        if (sCopyLogger != null) {
            sCopyLogger.b(str, str2);
        }
    }

    public static void Logi(String str, String str2) {
        Log.i(str, str2);
        if (sCopyLogger != null) {
            sCopyLogger.c(str, str2);
        }
    }
}
