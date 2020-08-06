package android.taobao.windvane.packageapp.monitor;

import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.util.NetWork;
import android.text.TextUtils;
import java.util.Hashtable;
import java.util.Map;

public class AppInfoMonitor {
    private static boolean isFirstTime = true;
    private static Map<String, UtData> map = new Hashtable();
    private static long startTime = 0;

    public static void start(String name, int updateType) {
        UtData data = new UtData();
        data.download_start = System.currentTimeMillis();
        data.update_type = updateType;
        if (!map.containsKey(name)) {
            data.is_wifi = NetWork.isWiFiActive();
            data.update_start_time = data.download_start;
        }
        map.put(name, data);
        if (isFirstTime) {
            startTime = System.currentTimeMillis() - WVPackageAppManager.getInstance().pkgInitTime;
        }
    }

    public static void download(String name) {
        UtData data = map.get(name);
        if (data != null) {
            data.download_end = System.currentTimeMillis();
        }
    }

    public static void success(ZipAppInfo info) {
        UtData data = map.get(info.getNameandVersion());
        if (data != null) {
            data.operate_end = System.currentTimeMillis();
            data.success = true;
            upload(info, data);
        }
    }

    public static void error(ZipAppInfo info, int error_type, String error_message) {
        UtData data = map.get(info.getNameandVersion());
        if (data != null) {
            data.operate_end = System.currentTimeMillis();
            data.success = false;
            data.error_type = error_type;
            data.error_message = error_message;
            upload(info, data);
        }
    }

    public static void upload(ZipAppInfo info, UtData data) {
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            if (isFirstTime) {
                WVMonitorService.getPackageMonitorInterface().commitPackageUpdateStartInfo(startTime, System.currentTimeMillis() - WVPackageAppManager.getInstance().pkgInitTime);
                isFirstTime = false;
            }
            String name = info.getNameandVersion();
            int index = name.indexOf(95);
            ZipAppInfo zipAppInfo = info;
            WVMonitorService.getPackageMonitorInterface().packageApp(zipAppInfo, name.substring(0, index), name.substring(index + 1), String.valueOf(data.update_type), data.success, data.operate_end - data.download_start, data.download_end - data.download_start, data.error_type, data.error_message, data.is_wifi, data.update_start_time);
            if (!TextUtils.isEmpty(name) && map != null) {
                map.remove(name);
            }
        }
    }

    private static class UtData {
        public long download_end;
        public long download_start;
        public String error_message;
        public int error_type;
        public boolean is_wifi;
        public long operate_end;
        public boolean success;
        public long update_start_time;
        public int update_type;

        private UtData() {
        }
    }
}
