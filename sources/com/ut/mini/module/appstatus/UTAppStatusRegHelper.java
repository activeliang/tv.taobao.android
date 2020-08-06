package com.ut.mini.module.appstatus;

import android.annotation.TargetApi;
import android.app.Application;

public class UTAppStatusRegHelper {
    @TargetApi(14)
    public static void registerAppStatusCallbacks(UTAppStatusCallbacks aCallbacks) {
        if (aCallbacks != null) {
            UTAppStatusMonitor.getInstance().registerAppStatusCallbacks(aCallbacks);
        }
    }

    @TargetApi(14)
    public static void unRegisterAppStatusCallbacks(UTAppStatusCallbacks aCallbacks) {
        if (aCallbacks != null) {
            UTAppStatusMonitor.getInstance().unregisterAppStatusCallbacks(aCallbacks);
        }
    }

    @TargetApi(14)
    public static void registeActivityLifecycleCallbacks(Application aApplicationInstance) {
        if (aApplicationInstance != null) {
            aApplicationInstance.registerActivityLifecycleCallbacks(UTAppStatusMonitor.getInstance());
        }
    }

    @TargetApi(14)
    public static void unregisterActivityLifecycleCallbacks(Application aApplicationInstance) {
        if (aApplicationInstance != null) {
            aApplicationInstance.unregisterActivityLifecycleCallbacks(UTAppStatusMonitor.getInstance());
        }
    }
}
