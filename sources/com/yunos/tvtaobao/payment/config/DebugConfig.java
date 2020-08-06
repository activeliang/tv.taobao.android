package com.yunos.tvtaobao.payment.config;

import android.app.ActivityManager;
import com.yunos.tvtaobao.payment.BuildConfig;

public class DebugConfig {
    public static boolean whetherIsMonkey() {
        if (BuildConfig.IsMonkey.booleanValue()) {
            return ActivityManager.isUserAMonkey();
        }
        return BuildConfig.IsMonkey.booleanValue();
    }
}
