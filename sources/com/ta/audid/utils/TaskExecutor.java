package com.ta.audid.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskExecutor {
    private static TaskExecutor instance;
    /* access modifiers changed from: private */
    public static final AtomicInteger integer = new AtomicInteger();
    private static ScheduledExecutorService threadPoolExecutor;

    static class TBThreadFactory implements ThreadFactory {
        private int priority;

        public TBThreadFactory(int priority2) {
            this.priority = priority2;
        }

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "utdid:" + TaskExecutor.integer.getAndIncrement());
            thread.setPriority(this.priority);
            return thread;
        }
    }

    private static synchronized ScheduledExecutorService getDefaultThreadPoolExecutor() {
        ScheduledExecutorService scheduledExecutorService;
        synchronized (TaskExecutor.class) {
            if (threadPoolExecutor == null) {
                threadPoolExecutor = Executors.newScheduledThreadPool(3, new TBThreadFactory(1));
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
        return getDefaultThreadPoolExecutor().schedule(r, delayMillis, TimeUnit.MILLISECONDS);
    }

    public final ScheduledFuture scheduleAtFixedRate(ScheduledFuture future, Runnable r, long delayMillis, long period) {
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
        return getDefaultThreadPoolExecutor().scheduleAtFixedRate(r, delayMillis, period, TimeUnit.MILLISECONDS);
    }

    public void submit(Runnable task) {
        try {
            getDefaultThreadPoolExecutor().submit(task);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
