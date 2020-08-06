package com.alibaba.analytics.core;

import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.UTRealtimeConfBiz;
import com.alibaba.analytics.core.logbuilder.LogPriorityMgr;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.core.sync.UploadLogFromCache;
import com.alibaba.analytics.utils.Logger;
import java.util.List;
import java.util.Map;

public class LogProcessor {
    public static void process(Map<String, String> map) {
        Logger.d();
        if (map != null) {
            String eventId = map.get(LogField.EVENTID.toString());
            if (!map.containsKey("_priority")) {
                if ("2201".equalsIgnoreCase(eventId)) {
                    map.put("_priority", "4");
                }
                if ("2202".equalsIgnoreCase(eventId)) {
                    map.put("_priority", Constants.LogTransferLevel.L6);
                }
            }
            String priority = "3";
            if (map.containsKey("_priority")) {
                priority = map.remove("_priority");
            }
            String configPriority = LogPriorityMgr.getInstance().getConfigLogLevel(eventId);
            if (!TextUtils.isEmpty(configPriority)) {
                priority = configPriority;
            }
            boolean lNeedSync = false;
            if (map.containsKey("_sls")) {
                lNeedSync = true;
                map.remove("_sls");
            }
            int topicId = 0;
            if (UTRealtimeConfBiz.getInstance().isRealtimeLogSampled()) {
                topicId = UTRealtimeConfBiz.getInstance().getTopicId(map);
            }
            Log log = new Log(priority, (List<String>) null, eventId, map);
            if (topicId > 0) {
                Logger.d("", "topicId", Integer.valueOf(topicId));
                log.setTopicId(topicId);
                UploadLogFromCache.getInstance().addLog(log);
            }
            if (lNeedSync) {
                LogStoreMgr.getInstance().addLogAndSave(log);
            } else {
                LogStoreMgr.getInstance().add(log);
            }
        }
    }
}
