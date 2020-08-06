package com.ut.mini.extend;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import com.alibaba.analytics.utils.Logger;
import com.ut.mini.core.WVUserTrack;

public class WindvaneExtend {
    public static void registerWindvane(boolean init) {
        if (!UTExtendSwitch.bWindvaneExtend) {
            Logger.w("UTAnalytics", "user disable WVTBUserTrack ");
        } else if (init) {
            Logger.w("UTAnalytics", "Has registered WVTBUserTrack plugin,not need to register again! ");
        } else {
            try {
                WVPluginManager.registerPlugin("WVTBUserTrack", (Class<? extends WVApiPlugin>) WVUserTrack.class, true);
                Logger.d("UTAnalytics", "register WVTBUserTrack Success");
            } catch (Throwable e) {
                Logger.e("UTAnalytics", "Exception", e.toString());
            }
        }
    }
}
