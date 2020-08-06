package com.alibaba.appmonitor.delegate;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.event.EventRepo;
import com.alibaba.appmonitor.event.EventType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

class CommitTask implements Runnable {
    private static final String TAG = "CommitTask";
    private static boolean init = false;
    private static HashMap<Integer, ScheduledFuture> mFutureMap = new HashMap<>();
    private static Map<Integer, CommitTask> uploadTasks;
    private int eventId;
    private int interval = 300000;
    private long startTime;

    static void init() {
        if (!init) {
            Logger.d(TAG, "init StatisticsAlarmEvent");
            uploadTasks = new ConcurrentHashMap();
            for (EventType eventType : EventType.values()) {
                if (eventType.isOpen()) {
                    int eventId2 = eventType.getEventId();
                    CommitTask uploadTask = new CommitTask(eventId2, eventType.getForegroundStatisticsInterval() * 1000);
                    uploadTasks.put(Integer.valueOf(eventId2), uploadTask);
                    mFutureMap.put(Integer.valueOf(eventId2), TaskExecutor.getInstance().schedule(mFutureMap.get(Integer.valueOf(eventId2)), uploadTask, (long) uploadTask.interval));
                }
            }
            init = true;
        }
    }

    static void destroy() {
        for (Integer eventId2 : mFutureMap.keySet()) {
            ScheduledFuture future = mFutureMap.get(eventId2);
            if (future != null && !future.isDone()) {
                future.cancel(true);
            }
        }
        init = false;
        uploadTasks = null;
        mFutureMap.clear();
    }

    static void setStatisticsInterval(int eventId2, int statisticsInterval) {
        synchronized (uploadTasks) {
            CommitTask uploadTask = uploadTasks.get(Integer.valueOf(eventId2));
            if (uploadTask == null) {
                if (statisticsInterval > 0) {
                    CommitTask uploadTask2 = new CommitTask(eventId2, statisticsInterval * 1000);
                    uploadTasks.put(Integer.valueOf(eventId2), uploadTask2);
                    mFutureMap.put(Integer.valueOf(eventId2), TaskExecutor.getInstance().schedule(mFutureMap.get(Integer.valueOf(eventId2)), uploadTask2, (long) uploadTask2.interval));
                }
            } else if (statisticsInterval <= 0) {
                uploadTasks.remove(Integer.valueOf(eventId2));
            } else if (uploadTask.interval != statisticsInterval * 1000) {
                uploadTask.interval = statisticsInterval * 1000;
                long currentTime = System.currentTimeMillis();
                long next = ((long) uploadTask.interval) - (currentTime - uploadTask.startTime);
                if (next < 0) {
                    next = 0;
                }
                ScheduledFuture future = mFutureMap.get(Integer.valueOf(eventId2));
                TaskExecutor.getInstance().schedule(future, uploadTask, next);
                mFutureMap.put(Integer.valueOf(eventId2), future);
                uploadTask.startTime = currentTime;
            }
        }
    }

    private CommitTask(int eventId2, int interval2) {
        this.eventId = eventId2;
        this.interval = interval2;
        this.startTime = System.currentTimeMillis();
    }

    public void run() {
        Logger.d(TAG, "check&commit event", Integer.valueOf(this.eventId));
        EventRepo.getRepo().uploadEvent(this.eventId);
        if (uploadTasks.containsValue(this)) {
            this.startTime = System.currentTimeMillis();
            mFutureMap.put(Integer.valueOf(this.eventId), TaskExecutor.getInstance().schedule(mFutureMap.get(Integer.valueOf(this.eventId)), this, (long) this.interval));
        }
    }

    static void uploadAllEvent() {
        for (EventType eventType : EventType.values()) {
            EventRepo.getRepo().uploadEvent(eventType.getEventId());
        }
    }
}
