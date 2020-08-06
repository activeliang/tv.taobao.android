package com.alibaba.analytics.core.logbuilder;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class LogPriorityMgr implements SystemConfigMgr.IKVChangeListener {
    private static final String TAG_KEY = "loglevel";
    private static LogPriorityMgr instance;
    private Map<String, String> mLogLevelMap = Collections.synchronizedMap(new HashMap());

    LogPriorityMgr() {
        SystemConfigMgr.getInstance().register(TAG_KEY, this);
        onChange(TAG_KEY, SystemConfigMgr.getInstance().get(TAG_KEY));
    }

    public static synchronized LogPriorityMgr getInstance() {
        LogPriorityMgr logPriorityMgr;
        synchronized (LogPriorityMgr.class) {
            if (instance == null) {
                instance = new LogPriorityMgr();
            }
            logPriorityMgr = instance;
        }
        return logPriorityMgr;
    }

    public String getLogLevel(String eventId) {
        String level = getConfigLogLevel(eventId);
        return !TextUtils.isEmpty(level) ? level : "3";
    }

    public String getConfigLogLevel(String eventId) {
        return this.mLogLevelMap.get(eventId);
    }

    public void onChange(String key, String value) {
        this.mLogLevelMap.clear();
        if (!TextUtils.isEmpty(value)) {
            try {
                JSONObject valueObj = new JSONObject(value);
                Iterator<String> keys = valueObj.keys();
                while (keys.hasNext()) {
                    String k = keys.next();
                    String level = valueObj.optString(k);
                    if (!TextUtils.isEmpty(level) && !TextUtils.isEmpty(level)) {
                        this.mLogLevelMap.put(k, level);
                    }
                }
            } catch (Throwable th) {
            }
        }
    }
}
