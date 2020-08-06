package com.alibaba.analytics.core.config.timestamp;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class ConfigTimeStampMgr {
    private static ConfigTimeStampMgr instance = null;
    /* access modifiers changed from: private */
    public static Map<String, String> mKvMap = Collections.synchronizedMap(new HashMap());
    private ScheduledFuture future;
    private Runnable storeTask = new Runnable() {
        public void run() {
            Context c = Variables.getInstance().getContext();
            if (c != null) {
                List<TimeStampEntity> entities = new ArrayList<>(ConfigTimeStampMgr.mKvMap.size());
                for (String name : ConfigTimeStampMgr.mKvMap.keySet()) {
                    entities.add(new TimeStampEntity(name, (String) ConfigTimeStampMgr.mKvMap.get(name)));
                }
                Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) TimeStampEntity.class);
                Variables.getInstance().getDbMgr().insert((List<? extends Entity>) entities);
                return;
            }
            Logger.w("storeTask.run()", "context", c);
        }
    };

    private ConfigTimeStampMgr() {
        List<? extends Entity> find;
        if (Variables.getInstance().getContext() != null && (find = Variables.getInstance().getDbMgr().find(TimeStampEntity.class, (String) null, (String) null, -1)) != null) {
            for (int i = 0; i < find.size(); i++) {
                mKvMap.put(((TimeStampEntity) find.get(i)).namespace, ((TimeStampEntity) find.get(i)).timestamp);
            }
        }
    }

    public static synchronized ConfigTimeStampMgr getInstance() {
        ConfigTimeStampMgr configTimeStampMgr;
        synchronized (ConfigTimeStampMgr.class) {
            if (instance == null) {
                instance = new ConfigTimeStampMgr();
            }
            configTimeStampMgr = instance;
        }
        return configTimeStampMgr;
    }

    public void put(String namespace, String value) {
        mKvMap.put(namespace, value);
        this.future = TaskExecutor.getInstance().schedule(this.future, this.storeTask, 10000);
    }

    public String get(String namespace) {
        String ret = mKvMap.get(namespace);
        if (TextUtils.isEmpty(ret)) {
            return "0";
        }
        return ret;
    }
}
