package android.taobao.windvane.util;

import android.taobao.windvane.config.GlobalConfig;

public class EnvUtil {
    private static boolean DEBUG = false;
    private static boolean inited = false;
    private static boolean openSpdyforDebug = false;

    public static boolean isDebug() {
        return TaoLog.getLogStatus() && isAppDebug();
    }

    public static boolean isAppDebug() {
        if (!inited) {
            init();
        }
        return DEBUG;
    }

    private static synchronized void init() {
        boolean z = true;
        synchronized (EnvUtil.class) {
            if (!inited) {
                try {
                    if ((GlobalConfig.context.getApplicationInfo().flags & 2) == 0) {
                        z = false;
                    }
                    DEBUG = z;
                } catch (Exception e) {
                }
                inited = true;
            }
        }
    }

    public static boolean isOpenSpdyforDebug() {
        return openSpdyforDebug;
    }

    public static void setOpenSpdyforDebug(boolean openSpdyforDebug2) {
        openSpdyforDebug = openSpdyforDebug2;
    }
}
