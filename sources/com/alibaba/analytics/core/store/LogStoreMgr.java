package com.alibaba.analytics.core.store;

import android.taobao.windvane.util.ConfigStorage;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SystemUtils;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.delegate.BackgroundTrigger;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

public class LogStoreMgr implements BackgroundTrigger.AppStatusChangeCallback {
    private static final int DELETE = 2;
    private static final int INSERT = 1;
    private static final int LOG_COUNT_CHECK = 5000;
    private static final Object Lock_Object = new Object();
    private static final int MAX_LOG_COUNT = 9000;
    private static final int MAX_LOG_SIZE = 100;
    private static final long STORE_INTERVAL = 5000;
    private static final String TAG = "LogStoreMgr";
    private static int logCount = 0;
    private static LogStoreMgr mInstance = new LogStoreMgr();
    public static SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private List<ILogChangeListener> mLogChangeListeners = Collections.synchronizedList(new ArrayList());
    private List<Log> mLogs = new CopyOnWriteArrayList();
    private ScheduledFuture mOneMinDBMonitorFuture = null;
    /* access modifiers changed from: private */
    public ILogStore mStore = new LogSqliteStore(Variables.getInstance().getContext());
    private ScheduledFuture mStoreFuture = null;
    private Runnable mStoreTask = new Runnable() {
        public void run() {
            LogStoreMgr.this.store();
        }
    };
    private ScheduledFuture mThrityMinDBMonitorFuture = null;

    private LogStoreMgr() {
        TaskExecutor.getInstance().submit(new CleanDbTask());
        BackgroundTrigger.registerCallback(this);
    }

    public static LogStoreMgr getInstance() {
        return mInstance;
    }

    public void add(Log log) {
        if (Logger.isDebug()) {
            Logger.i(TAG, "Log", log.getContent());
        }
        this.mLogs.add(log);
        if (this.mLogs.size() >= 100 || Variables.getInstance().isRealTimeDebug()) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.mStoreTask, 0);
        } else if (this.mStoreFuture == null || this.mStoreFuture.isDone()) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule(this.mStoreFuture, this.mStoreTask, STORE_INTERVAL);
        }
        synchronized (Lock_Object) {
            logCount++;
            if (logCount > 5000) {
                logCount = 0;
                TaskExecutor.getInstance().submit(new CleanLogTask());
            }
        }
    }

    public void addLogAndSave(Log log) {
        add(log);
        store();
    }

    public int delete(List<Log> logs) {
        return this.mStore.delete(logs);
    }

    public void update(List<Log> logs) {
        this.mStore.update(logs);
    }

    public void updateLogPriority(List<Log> logs) {
        this.mStore.updateLogPriority(logs);
    }

    public List<Log> get(int maxCount) {
        return this.mStore.get(maxCount);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001e, code lost:
        if (r1 == null) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0024, code lost:
        if (r1.size() <= 0) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0026, code lost:
        r5.mStore.insert(r1);
        dispatcherLogChangeEvent(1, r1.size());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void store() {
        /*
            r5 = this;
            monitor-enter(r5)
            com.alibaba.analytics.utils.Logger.d()     // Catch:{ all -> 0x0043 }
            r1 = 0
            java.util.List<com.alibaba.analytics.core.model.Log> r4 = r5.mLogs     // Catch:{ Throwable -> 0x0038 }
            monitor-enter(r4)     // Catch:{ Throwable -> 0x0038 }
            java.util.List<com.alibaba.analytics.core.model.Log> r3 = r5.mLogs     // Catch:{ all -> 0x0035 }
            int r3 = r3.size()     // Catch:{ all -> 0x0035 }
            if (r3 <= 0) goto L_0x001d
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0035 }
            java.util.List<com.alibaba.analytics.core.model.Log> r3 = r5.mLogs     // Catch:{ all -> 0x0035 }
            r2.<init>(r3)     // Catch:{ all -> 0x0035 }
            java.util.List<com.alibaba.analytics.core.model.Log> r3 = r5.mLogs     // Catch:{ all -> 0x0046 }
            r3.clear()     // Catch:{ all -> 0x0046 }
            r1 = r2
        L_0x001d:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x0033
            int r3 = r1.size()     // Catch:{ Throwable -> 0x0038 }
            if (r3 <= 0) goto L_0x0033
            com.alibaba.analytics.core.store.ILogStore r3 = r5.mStore     // Catch:{ Throwable -> 0x0038 }
            r3.insert(r1)     // Catch:{ Throwable -> 0x0038 }
            r3 = 1
            int r4 = r1.size()     // Catch:{ Throwable -> 0x0038 }
            r5.dispatcherLogChangeEvent(r3, r4)     // Catch:{ Throwable -> 0x0038 }
        L_0x0033:
            monitor-exit(r5)
            return
        L_0x0035:
            r3 = move-exception
        L_0x0036:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            throw r3     // Catch:{ Throwable -> 0x0038 }
        L_0x0038:
            r0 = move-exception
            java.lang.String r3 = "LogStoreMgr"
            java.lang.String r4 = ""
            android.util.Log.w(r3, r4, r0)     // Catch:{ all -> 0x0043 }
            goto L_0x0033
        L_0x0043:
            r3 = move-exception
            monitor-exit(r5)
            throw r3
        L_0x0046:
            r3 = move-exception
            r1 = r2
            goto L_0x0036
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.store.LogStoreMgr.store():void");
    }

    public void clear() {
        Logger.d(TAG, "[clear]");
        this.mStore.clear();
        this.mLogs.clear();
    }

    public long count() {
        Logger.d(TAG, "[count] memory count:", Integer.valueOf(this.mLogs.size()), " db count:", Integer.valueOf(this.mStore.count()));
        return (long) (this.mStore.count() + this.mLogs.size());
    }

    @Deprecated
    public long memoryCount() {
        return (long) this.mLogs.size();
    }

    public long dbCount() {
        return (long) this.mStore.count();
    }

    public void registerLogChangeListener(ILogChangeListener listener) {
        this.mLogChangeListeners.add(listener);
    }

    public void unRegisterChangeListener(ILogChangeListener listener) {
        this.mLogChangeListeners.remove(listener);
    }

    private void dispatcherLogChangeEvent(int event, int size) {
        Logger.d();
        for (int i = 0; i < this.mLogChangeListeners.size(); i++) {
            ILogChangeListener listener = this.mLogChangeListeners.get(i);
            if (listener != null) {
                switch (event) {
                    case 1:
                        listener.onInsert((long) size, dbCount());
                        break;
                    case 2:
                        listener.onDelete((long) size, dbCount());
                        break;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public int clearOldLogByTime() {
        Logger.d();
        Calendar cal1 = Calendar.getInstance();
        cal1.add(5, -3);
        return this.mStore.clearOldLogByField("time", String.valueOf(cal1.getTimeInMillis()));
    }

    /* access modifiers changed from: private */
    public int clearOldLogByCount(int dbCount) {
        int delCount = 0;
        if (dbCount > 9000) {
            delCount = this.mStore.clearOldLogByCount((dbCount - 9000) + 1000);
        }
        Logger.d(TAG, "clearOldLogByCount", Integer.valueOf(delCount));
        return dbCount;
    }

    public void onBackground() {
        this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.mStoreTask, 0);
        this.mOneMinDBMonitorFuture = TaskExecutor.getInstance().schedule(this.mOneMinDBMonitorFuture, new MonitorDBTask().setMin(1), 60000);
        this.mThrityMinDBMonitorFuture = TaskExecutor.getInstance().schedule(this.mThrityMinDBMonitorFuture, new MonitorDBTask().setMin(30), ConfigStorage.DEFAULT_SMALL_MAX_AGE);
    }

    public void onForeground() {
    }

    class CleanDbTask implements Runnable {
        CleanDbTask() {
        }

        public void run() {
            int ret;
            Logger.d();
            int deleteCount = LogStoreMgr.this.clearOldLogByTime();
            if (deleteCount > 0) {
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.CLEAN_DB, "time_ex", Double.valueOf((double) deleteCount)));
            }
            int dbCount = LogStoreMgr.this.mStore.count();
            if (dbCount > 9000 && (ret = LogStoreMgr.this.clearOldLogByCount(dbCount)) > 0) {
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.CLEAN_DB, "count_ex", Double.valueOf((double) ret)));
            }
        }
    }

    class CleanLogTask implements Runnable {
        CleanLogTask() {
        }

        public void run() {
            Logger.d(LogStoreMgr.TAG, "CleanLogTask");
            int dbCount = LogStoreMgr.this.mStore.count();
            if (dbCount > 9000) {
                int unused = LogStoreMgr.this.clearOldLogByCount(dbCount);
            }
        }
    }

    class MonitorDBTask implements Runnable {
        private int min = 0;

        MonitorDBTask() {
        }

        public MonitorDBTask setMin(int min2) {
            this.min = min2;
            return this;
        }

        public void run() {
            try {
                int dbLeft = LogStoreMgr.this.mStore.count();
                double dbFileSize = LogStoreMgr.this.mStore.getDbFileSize();
                double freeSize = SystemUtils.getSystemFreeSize();
                Map<String, Object> args = new HashMap<>();
                args.put("min", Integer.valueOf(this.min));
                args.put("dbLeft", Integer.valueOf(dbLeft));
                args.put("dbFileSize", Double.valueOf(dbFileSize));
                args.put("freeSize", Double.valueOf(freeSize));
                LogStoreMgr.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.DB_MONITOR, JSON.toJSONString(args), Double.valueOf(1.0d)));
            } catch (Throwable th) {
            }
        }
    }
}
