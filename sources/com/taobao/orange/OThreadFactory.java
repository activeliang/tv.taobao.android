package com.taobao.orange;

import com.taobao.orange.util.OLog;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class OThreadFactory {
    static final int POOL_WAIT_TIMES = 60;
    public static final int PRIORITY = 2;
    private static final String TAG = "OThreadPool";
    private static ScheduledThreadPoolExecutor corePoolExecutor = new TBThreadPoolExecutor(4, new TBThreadFactory());
    static final AtomicInteger integer = new AtomicInteger();

    static class TBThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "Orange:" + OThreadFactory.integer.getAndIncrement());
            thread.setPriority(5);
            return thread;
        }
    }

    static class TBThreadPoolExecutor extends ScheduledThreadPoolExecutor {
        TBThreadPoolExecutor(int i, ThreadFactory threadFactory) {
            super(i, threadFactory);
            setKeepAliveTime(60, TimeUnit.SECONDS);
            allowCoreThreadTimeOut(true);
        }
    }

    private static ScheduledThreadPoolExecutor getCoreExecutor() {
        return corePoolExecutor;
    }

    public static void execute(Runnable command) {
        execute(command, 0);
    }

    public static void execute(Runnable command, long delay) {
        try {
            getCoreExecutor().schedule(command, delay, TimeUnit.MILLISECONDS);
        } catch (Throwable t) {
            OLog.e(TAG, "execute", t, new Object[0]);
        }
    }
}
