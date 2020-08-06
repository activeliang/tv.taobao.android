package android.taobao.windvane.jsbridge.api;

import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.utils.DeviceInfo;
import android.taobao.windvane.jsbridge.utils.YearClass;

public class WVNativeDetector extends WVApiPlugin {
    public boolean execute(String action, String params, WVCallBackContext callback) {
        if ("getDeviceYear".equals(action)) {
            detectYearClass(params, callback);
            return true;
        } else if ("getCurrentUsage".equals(action)) {
            getCurrentUsage(params, callback);
            return true;
        } else if (!"getModelInfo".equals(action)) {
            return false;
        } else {
            getModelInfo(callback, params);
            return true;
        }
    }

    private void detectYearClass(String params, WVCallBackContext callback) {
        int year = YearClass.get(this.mContext);
        if (year == -1) {
            callback.error();
            return;
        }
        WVResult result = new WVResult();
        result.addData("deviceYear", Integer.toString(year));
        callback.success(result);
    }

    public void getModelInfo(WVCallBackContext context, String param) {
        WVResult result = new WVResult();
        result.addData("model", Build.MODEL);
        result.addData("brand", Build.BRAND);
        context.success(result);
    }

    private void getCurrentUsage(String params, WVCallBackContext callback) {
        WVResult result = new WVResult();
        if (GlobalConfig.context == null) {
            callback.error();
            return;
        }
        float totalMemory = (float) (DeviceInfo.getTotalMemory(GlobalConfig.context) / 1048576);
        float cpuUsage = DeviceInfo.getProcessCpuRate();
        float usedMemory = totalMemory - ((float) (DeviceInfo.getFreeMemorySize(GlobalConfig.context) / 1048576));
        result.addData("cpuUsage", Float.toString(cpuUsage));
        result.addData("memoryUsage", Float.toString(usedMemory / totalMemory));
        result.addData("totalMemory", Float.toString(totalMemory));
        result.addData("usedMemory", Float.toString(usedMemory));
        callback.success(result);
    }
}
