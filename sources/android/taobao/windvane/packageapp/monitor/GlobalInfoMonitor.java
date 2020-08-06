package android.taobao.windvane.packageapp.monitor;

import android.taobao.windvane.util.TaoLog;

public class GlobalInfoMonitor {
    public static void error(int error_type, String error_message) {
        TaoLog.e("WVPackageApp", "failed to install app. error_type : " + error_type + " error_message : " + error_message);
    }
}
