package com.alibaba.analytics.core.selfmonitor;

import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.logbuilder.SessionTimeAndIndexMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

public class ConfigArrivedMonitor implements SystemConfigMgr.IKVChangeListener {
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private String mCurrentValue = null;

    public void start() {
        SystemConfigMgr.getInstance().register("test_config_arrival_rate", this);
    }

    public void end() {
        SystemConfigMgr.getInstance().register("test_config_arrival_rate", this);
    }

    public void onChange(String key, String value) {
        Logger.d((String) null, "key", key, "value", value);
        if (value != null && !value.equalsIgnoreCase(this.mCurrentValue)) {
            Map args = new HashMap();
            args.put("value", value);
            args.put("arrival_time", Long.valueOf(System.currentTimeMillis() / 1000));
            args.put("app_start_time", Long.valueOf(SessionTimeAndIndexMgr.getInstance().getSessionTimestamp()));
            mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.CONFIG_ARRIVE, JSON.toJSONString(args), Double.valueOf(1.0d)));
            Logger.d((String) null, "json", JSON.toJSONString(args));
        }
        this.mCurrentValue = value;
    }
}
