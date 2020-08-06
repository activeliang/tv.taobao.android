package com.taobao.orange.launch;

import android.app.Application;
import com.taobao.orange.OConstant;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.util.OLog;
import java.io.Serializable;
import java.util.HashMap;

public class HTaoLaunchOrange implements Serializable {
    private static final String TAG = "HTaoLaunchOrange";

    public void init(Application application, HashMap<String, Object> params) {
        OLog.d(TAG, "init start", new Object[0]);
        String appkey = "23070080";
        String appVersion = "*";
        int orangeEnv = OConstant.ENV.ONLINE.getEnvMode();
        try {
            appVersion = (String) params.get("appVersion");
            orangeEnv = ((Integer) params.get(OConstant.LAUNCH_ENVINDEX)).intValue();
            if (orangeEnv == OConstant.ENV.ONLINE.getEnvMode()) {
                appkey = (String) params.get(OConstant.LAUNCH_ONLINEAPPKEY);
            } else if (orangeEnv == OConstant.ENV.PREPARE.getEnvMode()) {
                appkey = (String) params.get(OConstant.LAUNCH_PREAPPKEY);
            } else {
                appkey = (String) params.get(OConstant.LAUNCH_TESTAPPKEY);
            }
        } catch (Throwable t) {
            OLog.e(TAG, "init", t, new Object[0]);
        }
        OrangeConfig.getInstance().init(application, appkey, appVersion, orangeEnv);
    }
}
