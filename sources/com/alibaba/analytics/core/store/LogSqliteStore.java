package com.alibaba.analytics.core.store;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.utils.Logger;
import java.util.List;

class LogSqliteStore implements ILogStore {
    private static final String TAG = "UTSqliteLogStore";
    String countSql = "SELECT count(*) FROM %s";
    String deleteSql = "DELETE FROM  %s where _id in ( select _id from %s  ORDER BY priority ASC ,  _id ASC LIMIT %d )";
    String querySql = "SELECT * FROM %s ORDER BY %s ASC LIMIT %d";

    LogSqliteStore(Context context) {
    }

    public synchronized boolean insert(List<Log> logs) {
        Variables.getInstance().getDbMgr().insert((List<? extends Entity>) logs);
        return true;
    }

    public synchronized int delete(List<Log> logs) {
        return Variables.getInstance().getDbMgr().delete((List<? extends Entity>) logs);
    }

    public synchronized List<Log> get(int maxCount) {
        return Variables.getInstance().getDbMgr().find(Log.class, (String) null, "priority DESC , time DESC ", maxCount);
    }

    public synchronized int count() {
        return Variables.getInstance().getDbMgr().count(Log.class);
    }

    public synchronized void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) Log.class);
    }

    public synchronized int clearOldLogByField(String logField, String value) {
        Logger.d();
        return Variables.getInstance().getDbMgr().delete(Log.class, logField + "< ?", new String[]{value});
    }

    public int clearOldLogByCount(int count) {
        Logger.d();
        return Variables.getInstance().getDbMgr().delete(Log.class, " _id in ( select _id from " + Variables.getInstance().getDbMgr().getTablename(Log.class) + "  ORDER BY " + "priority" + " ASC , _id ASC LIMIT " + count + " )", (String[]) null);
    }

    public double getDbFileSize() {
        return Variables.getInstance().getDbMgr().getDbFileSize();
    }

    public synchronized void update(List<Log> logs) {
        Variables.getInstance().getDbMgr().update((List<? extends Entity>) logs);
    }

    public synchronized void updateLogPriority(List<Log> logs) {
        Variables.getInstance().getDbMgr().updateLogPriority(logs);
    }
}
