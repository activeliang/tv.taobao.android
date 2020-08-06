package com.alibaba.appmonitor.offline;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.selfmonitor.CrashDispatcher;
import com.alibaba.analytics.core.selfmonitor.CrashListener;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.delegate.BackgroundTrigger;
import com.alibaba.appmonitor.event.EventRepo;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.model.Metric;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class TempEventMgr implements BackgroundTrigger.AppStatusChangeCallback, CrashListener, SystemConfigMgr.IKVChangeListener {
    private static final int DB_MAX_COUNT = 50000;
    private static final int MAX_SIZE = 100;
    private static final String OFFLINE_DURATION = "offline_duration";
    private static final int ONE_HOUR_SEC = 3600;
    private static final int ONE_SECOND = 1000;
    private static TempEventMgr instance = new TempEventMgr();
    private Runnable commitTask = new Runnable() {
        public void run() {
            TempEventMgr.this.commitEventsToComputer();
        }
    };
    private List<TempEvent> mAlarmEventLists = Collections.synchronizedList(new ArrayList());
    private ScheduledFuture mCommitFuture = null;
    private List<TempEvent> mCounterEventLists = Collections.synchronizedList(new ArrayList());
    private long mCurrentDuration = -2;
    private List<Metric> mMetricLists = Collections.synchronizedList(new ArrayList());
    private List<TempEvent> mStatEventLists = Collections.synchronizedList(new ArrayList());
    private ScheduledFuture mStoreFuture = null;
    private Runnable storeTask = new Runnable() {
        public void run() {
            TempEventMgr.this.store();
        }
    };

    private TempEventMgr() {
        BackgroundTrigger.registerCallback(this);
        CrashDispatcher.getInstance().addCrashListener(this);
        SystemConfigMgr.getInstance().register(OFFLINE_DURATION, this);
        TaskExecutor.getInstance().submit(new CleanTableTask());
        startCommitTask();
    }

    public static TempEventMgr getInstance() {
        return instance;
    }

    public void add(EventType eventType, TempEvent event) {
        Logger.d();
        if (EventType.ALARM == eventType) {
            this.mAlarmEventLists.add(event);
        } else if (EventType.COUNTER == eventType) {
            this.mCounterEventLists.add(event);
        } else if (EventType.STAT == eventType) {
            this.mStatEventLists.add(event);
        }
        if (this.mAlarmEventLists.size() >= 100 || this.mCounterEventLists.size() >= 100 || this.mStatEventLists.size() >= 100) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.storeTask, 0);
        } else if (this.mStoreFuture == null || (this.mStoreFuture != null && this.mStoreFuture.isDone())) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule(this.mStoreFuture, this.storeTask, 30000);
        }
    }

    public void add(Metric m) {
        Logger.d();
        if (m != null) {
            this.mMetricLists.add(m);
        }
        if (this.mMetricLists.size() >= 100) {
            this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.storeTask, 0);
        } else {
            this.mStoreFuture = TaskExecutor.getInstance().schedule(this.mStoreFuture, this.storeTask, 30000);
        }
    }

    public Metric getMetric(String module, String monitorPoint) {
        List<? extends Entity> find = Variables.getInstance().getDbMgr().find(Metric.class, "module=\"" + module + "\" and " + "monitor_point=\"" + monitorPoint + "\"", (String) null, 1);
        if (find == null || find.size() <= 0) {
            return null;
        }
        return (Metric) find.get(0);
    }

    public void store() {
        Logger.d();
        clearAndStore(this.mAlarmEventLists);
        clearAndStore(this.mCounterEventLists);
        clearAndStore(this.mStatEventLists);
        clearAndSyncMetric(this.mMetricLists);
    }

    private void clearAndStore(List<?> list) {
        if (list != null && list.size() > 0) {
            synchronized (list) {
                try {
                    ArrayList temp = new ArrayList(list);
                    try {
                        list.clear();
                        Variables.getInstance().getDbMgr().insert((List<? extends Entity>) temp);
                    } catch (Throwable th) {
                        th = th;
                        ArrayList arrayList = temp;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
    }

    private void clearAndSyncMetric(List<Metric> list) {
        if (list != null) {
            List<Metric> updateList = new ArrayList<>();
            List<Metric> insertList = new ArrayList<>();
            synchronized (list) {
                for (int i = 0; i < list.size(); i++) {
                    Metric m = list.get(i);
                    Metric dbMetric = getMetric(m.getModule(), m.getMonitorPoint());
                    if (dbMetric != null) {
                        m._id = dbMetric._id;
                        updateList.add(m);
                    } else {
                        insertList.add(m);
                    }
                }
                list.clear();
            }
            if (updateList.size() > 0) {
                Variables.getInstance().getDbMgr().update((List<? extends Entity>) updateList);
            }
            if (insertList.size() > 0) {
                Variables.getInstance().getDbMgr().insert((List<? extends Entity>) insertList);
            }
        }
    }

    public List<? extends TempEvent> getExpireEvents(EventType type, int count) {
        return Variables.getInstance().getDbMgr().find(getCls(type), "commit_time<" + ((System.currentTimeMillis() / 1000) - (getDuration() / 1000)), "access,sub_access,module,monitor_point", count);
    }

    public List<? extends TempEvent> get(EventType type, int count) {
        return Variables.getInstance().getDbMgr().find(getCls(type), (String) null, (String) null, count);
    }

    private Class<? extends Entity> getCls(EventType type) {
        if (EventType.ALARM == type) {
            return TempAlarm.class;
        }
        if (EventType.COUNTER == type) {
            return TempCounter.class;
        }
        if (EventType.STAT == type) {
            return TempStat.class;
        }
        return TempEvent.class;
    }

    public void onBackground() {
        Logger.d();
        this.mStoreFuture = TaskExecutor.getInstance().schedule((ScheduledFuture) null, this.storeTask, 0);
    }

    public void onForeground() {
    }

    /* access modifiers changed from: private */
    public void commitEventsToComputer() {
        Logger.d();
        EventType[] arr$ = EventType.values();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            EventType type = arr$[i$];
            while (true) {
                List<? extends TempEvent> events = getExpireEvents(type, 500);
                Logger.d((String) null, "type", type, "events.size()", Integer.valueOf(events.size()));
                if (events.size() == 0) {
                    i$++;
                } else {
                    for (int i = 0; i < events.size(); i++) {
                        switch (type) {
                            case ALARM:
                                TempAlarm alarm = (TempAlarm) events.get(i);
                                if (!alarm.isSuccessEvent()) {
                                    EventRepo.getRepo().alarmEventFailIncr(type.getEventId(), alarm.module, alarm.monitorPoint, alarm.arg, alarm.errCode, alarm.errMsg, Long.valueOf(alarm.commitTime), alarm.access, alarm.accessSubType);
                                    break;
                                } else {
                                    EventRepo.getRepo().alarmEventSuccessIncr(type.getEventId(), alarm.module, alarm.monitorPoint, alarm.arg, Long.valueOf(alarm.commitTime), alarm.access, alarm.accessSubType);
                                    break;
                                }
                            case COUNTER:
                                TempCounter counter = (TempCounter) events.get(i);
                                EventRepo.getRepo().countEventCommit(type.getEventId(), counter.module, counter.monitorPoint, counter.arg, counter.value, Long.valueOf(counter.commitTime), counter.access, counter.accessSubType);
                                break;
                            case STAT:
                                TempStat stat = (TempStat) events.get(i);
                                EventRepo.getRepo().commitStatEvent(type.getEventId(), stat.module, stat.monitorPoint, stat.getMeasureVauleSet(), stat.getDimensionValue());
                                break;
                        }
                    }
                    delete(events);
                }
            }
        }
    }

    private void delete(List<? extends TempEvent> events) {
        Variables.getInstance().getDbMgr().delete((List<? extends Entity>) events);
    }

    public void onCrash(Thread thread, Throwable ex) {
        Logger.d();
        store();
    }

    public void clear() {
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) TempAlarm.class);
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) TempCounter.class);
        Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) TempStat.class);
    }

    public void onChange(String key, String value) {
        if (OFFLINE_DURATION.equalsIgnoreCase(key)) {
            startCommitTask();
        }
    }

    private void startCommitTask() {
        long duration = getDuration();
        if (this.mCurrentDuration != duration) {
            this.mCurrentDuration = duration;
            this.mCommitFuture = TaskExecutor.getInstance().scheduleAtFixedRate(this.mCommitFuture, this.commitTask, this.mCurrentDuration);
        }
    }

    private long getDuration() {
        int duration;
        int duration2 = SystemConfigMgr.getInstance().getInt(OFFLINE_DURATION);
        if (duration2 <= 0) {
            duration = 21600000;
        } else if (duration2 <= ONE_HOUR_SEC) {
            duration = 3600000;
        } else {
            duration = duration2 * 1000;
        }
        return (long) duration;
    }

    private class CleanTableTask implements Runnable {
        private CleanTableTask() {
        }

        public void run() {
            TempEventMgr.this.clearTempAlarmTable();
            TempEventMgr.this.clearTempCounterTable();
            TempEventMgr.this.clearTempStatTable();
        }
    }

    /* access modifiers changed from: private */
    public void clearTempCounterTable() {
        clearEvent(TempCounter.class);
    }

    /* access modifiers changed from: private */
    public void clearTempAlarmTable() {
        clearEvent(TempAlarm.class);
    }

    /* access modifiers changed from: private */
    public void clearTempStatTable() {
        clearEvent(TempStat.class);
    }

    private void clearEvent(Class<? extends Entity> cls) {
        clearExpiredEvent(cls);
        if (Variables.getInstance().getDbMgr().count(cls) > DB_MAX_COUNT) {
            clearEventByCount(cls, 10000);
        }
    }

    private long clearEventByCount(Class<? extends Entity> cls, int count) {
        return (long) Variables.getInstance().getDbMgr().delete(cls, " _id in ( select _id from " + Variables.getInstance().getDbMgr().getTablename(cls) + "  ORDER BY  _id ASC LIMIT " + count + " )", (String[]) null);
    }

    private int clearExpiredEvent(Class<? extends Entity> cls) {
        Calendar cal1 = Calendar.getInstance();
        cal1.add(5, -7);
        return Variables.getInstance().getDbMgr().delete(cls, "commit_time< " + (cal1.getTimeInMillis() / 1000), (String[]) null);
    }
}
