package com.ta.audid.store;

import com.ta.audid.Variables;
import com.ta.audid.db.Entity;
import com.ta.audid.utils.UtdidLogger;
import java.util.ArrayList;
import java.util.List;

public class UtdidContentSqliteStore {
    public static final int MAX_LOG_COUNT = 4;
    private static UtdidContentSqliteStore mInstance = null;

    private UtdidContentSqliteStore() {
    }

    public static synchronized UtdidContentSqliteStore getInstance() {
        UtdidContentSqliteStore utdidContentSqliteStore;
        synchronized (UtdidContentSqliteStore.class) {
            if (mInstance == null) {
                mInstance = new UtdidContentSqliteStore();
            }
            utdidContentSqliteStore = mInstance;
        }
        return utdidContentSqliteStore;
    }

    public synchronized void insertStringList(List<String> logs) {
        UtdidLogger.d();
        if (logs == null || logs.size() < 1) {
            UtdidLogger.d("", "logs is empty");
        } else {
            UtdidLogger.d("", "logs", Integer.valueOf(logs.size()));
            if (count() > 4) {
                clearOldLogByCount(2);
            }
            List<UtdidContent> utdidContents = new ArrayList<>();
            for (String log : logs) {
                utdidContents.add(new UtdidContent(log));
            }
            Variables.getInstance().getDbMgr().insert((List<? extends Entity>) utdidContents);
        }
    }

    private int count() {
        return Variables.getInstance().getDbMgr().count(UtdidContent.class);
    }

    private int clearOldLogByCount(int count) {
        return Variables.getInstance().getDbMgr().delete(UtdidContent.class, " _id in ( select _id from " + Variables.getInstance().getDbMgr().getTablename(UtdidContent.class) + " ORDER BY _id ASC LIMIT " + count + " )", (String[]) null);
    }

    public synchronized int delete(List<UtdidContent> logs) {
        return Variables.getInstance().getDbMgr().delete((List<? extends Entity>) logs);
    }

    public synchronized List<UtdidContent> get(int maxCount) {
        return Variables.getInstance().getDbMgr().find(UtdidContent.class, (String) null, "priority DESC , time DESC ", maxCount);
    }

    public synchronized void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) UtdidContent.class);
    }
}
