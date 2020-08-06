package com.alibaba.analytics.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskExecutor {
    public static final int MSG_ABTEST = 3;
    public static final int MSG_BACKGROUND = 4;
    public static final int MSG_CLEAN = 5;
    public static final int MSG_CLOSE_DB = 9;
    public static final int MSG_COMMIT = 6;
    public static final int MSG_CONFIG = 7;
    public static final int MSG_ORANGE_CONFIG = 8;
    public static final int MSG_STORE = 1;
    public static final int MSG_UPLOAD = 2;
    protected static final String TAG = "TaskExecutor";
    public static TaskExecutor instance;
    /* access modifiers changed from: private */
    public static final AtomicInteger integer = new AtomicInteger();
    private static int prop = 1;
    private static ScheduledExecutorService threadPoolExecutor;

    static class TBThreadFactory implements ThreadFactory {
        private int priority;

        public TBThreadFactory(int proiority) {
            this.priority = proiority;
        }

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "AppMonitor:" + TaskExecutor.integer.getAndIncrement());
            thread.setPriority(this.priority);
            return thread;
        }
    }

    private static synchronized ScheduledExecutorService getDefaulThreadPoolExecutor() {
        ScheduledExecutorService scheduledExecutorService;
        synchronized (TaskExecutor.class) {
            if (threadPoolExecutor == null) {
                threadPoolExecutor = Executors.newScheduledThreadPool(3, new TBThreadFactory(prop));
            }
            scheduledExecutorService = threadPoolExecutor;
        }
        return scheduledExecutorService;
    }

    public static synchronized TaskExecutor getInstance() {
        TaskExecutor taskExecutor;
        synchronized (TaskExecutor.class) {
            if (instance == null) {
                instance = new TaskExecutor();
            }
            taskExecutor = instance;
        }
        return taskExecutor;
    }

    public final ScheduledFuture schedule(ScheduledFuture future, Runnable r, long delayMillis) {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        return getDefaulThreadPoolExecutor().schedule(r, delayMillis, TimeUnit.MILLISECONDS);
    }

    public final ScheduledFuture scheduleAtFixedRate(ScheduledFuture future, Runnable r, long period) {
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
        return getDefaulThreadPoolExecutor().scheduleAtFixedRate(r, 1000, period, TimeUnit.MILLISECONDS);
    }

    public void submit(Runnable task) {
        try {
            getDefaulThreadPoolExecutor().submit(task);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
