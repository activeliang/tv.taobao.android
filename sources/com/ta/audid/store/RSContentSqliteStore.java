package com.ta.audid.store;

import com.ta.audid.Variables;
import com.ta.audid.db.Entity;
import java.util.ArrayList;
import java.util.List;

public class RSContentSqliteStore {
    private static RSContentSqliteStore mInstance = null;

    private RSContentSqliteStore() {
    }

    public static synchronized RSContentSqliteStore getInstance() {
        RSContentSqliteStore rSContentSqliteStore;
        synchronized (RSContentSqliteStore.class) {
            if (mInstance == null) {
                mInstance = new RSContentSqliteStore();
            }
            rSContentSqliteStore = mInstance;
        }
        return rSContentSqliteStore;
    }

    public synchronized void insertStringList(List<String> logs) {
        if (logs != null) {
            if (logs.size() >= 1) {
                if (count() > 200) {
                    clearOldLogByCount(100);
                }
                List<RSContent> rsContents = new ArrayList<>();
                for (String log : logs) {
                    rsContents.add(new RSContent(log));
                }
                Variables.getInstance().getDbMgr().insert((List<? extends Entity>) rsContents);
            }
        }
    }

    private int count() {
        return Variables.getInstance().getDbMgr().count(RSContent.class);
    }

    private int clearOldLogByCount(int count) {
        return Variables.getInstance().getDbMgr().delete(RSContent.class, " _id in ( select _id from " + Variables.getInstance().getDbMgr().getTablename(RSContent.class) + " ORDER BY _id ASC LIMIT " + count + " )", (String[]) null);
    }

    public synchronized int find(String content) {
        return Variables.getInstance().getDbMgr().count(RSContent.class, "content = '" + content + "'");
    }

    public synchronized void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) RSContent.class);
    }
}
