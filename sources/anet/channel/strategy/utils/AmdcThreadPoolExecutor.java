package anet.channel.strategy.utils;

import anet.channel.util.ALog;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AmdcThreadPoolExecutor {
    private static final String TAG = "awcn.AmdcThreadPoolExecutor";
    /* access modifiers changed from: private */
    public static AtomicInteger seq = new AtomicInteger(0);
    private static ScheduledThreadPoolExecutor threadPoolExecutor = null;

    static ScheduledThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            synchronized (AmdcThreadPoolExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
                        public Thread newThread(Runnable runnable) {
                            Thread thread = new Thread(runnable, "AMDC" + AmdcThreadPoolExecutor.seq.incrementAndGet());
                            ALog.i("awcn.AmdcThreadPoolExecutor", "thread created!", (String) null, "name", thread.getName());
                            thread.setPriority(5);
                            return thread;
                        }
                    });
                    threadPoolExecutor.setKeepAliveTime(60, TimeUnit.SECONDS);
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                }
            }
        }
        return threadPoolExecutor;
    }

    public static void submitTask(Runnable runnable) {
        try {
            getThreadPoolExecutor().submit(runnable);
        } catch (Exception e) {
            ALog.e("awcn.AmdcThreadPoolExecutor", "submit task failed", (String) null, e, new Object[0]);
        }
    }

    public static void scheduleTask(Runnable runnable, long timeInMs) {
        try {
            getThreadPoolExecutor().schedule(runnable, timeInMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e("awcn.AmdcThreadPoolExecutor", "schedule task failed", (String) null, e, new Object[0]);
        }
    }
}
