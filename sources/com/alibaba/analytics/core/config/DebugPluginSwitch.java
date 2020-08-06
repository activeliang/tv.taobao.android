package com.alibaba.analytics.core.config;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;

public class DebugPluginSwitch implements SystemConfigMgr.IKVChangeListener {
    public static final String KEY = "sw_plugin";

    public void onChange(String key, String value) {
        if (KEY.equalsIgnoreCase(key)) {
            boolean lIsDebugPluginOn = false;
            try {
                lIsDebugPluginOn = Boolean.parseBoolean(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lIsDebugPluginOn) {
                Variables.getInstance().turnOffDebugPlugin();
            }
        }
    }
}
