package anet.channel.thread;

import anet.channel.util.ALog;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorFactory {
    private static final String TAG = "awcn.ThreadPoolExecutorFactory";
    private static volatile PriorityExecutor priorityExecutor;
    private static volatile ScheduledExecutorService scheduleThreadPoolExecutor;

    static class TBThreadFactory implements ThreadFactory {
        String name;
        AtomicInteger seq = new AtomicInteger(0);

        TBThreadFactory(String name2) {
            this.name = name2;
        }

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, this.name + this.seq.incrementAndGet());
            ALog.i(ThreadPoolExecutorFactory.TAG, "thread created!", (String) null, "name", thread.getName());
            thread.setPriority(5);
            return thread;
        }
    }

    public static Future<?> submitScheduledTask(Runnable task) {
        return getScheduledExecutor().submit(task);
    }

    public static Future<?> submitScheduledTask(Runnable task, long delay, TimeUnit unit) {
        return getScheduledExecutor().schedule(task, delay, unit);
    }

    public static Future<?> submitPriorityTask(Runnable runnable) {
        return submitPriorityTask(runnable, 0);
    }

    public static Future<?> submitPriorityTask(Runnable runnable, int priority) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "submit priority task", (String) null, "priority", Integer.valueOf(priority));
        }
        DispatcherTask dispatcherTask = new DispatcherTask(runnable, priority);
        getPriorityExecutor().submit(dispatcherTask);
        return dispatcherTask;
    }

    static ScheduledExecutorService getScheduledExecutor() {
        if (scheduleThreadPoolExecutor == null) {
            synchronized (ThreadPoolExecutorFactory.class) {
                if (scheduleThreadPoolExecutor == null) {
                    scheduleThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new TBThreadFactory("AWCN Scheduler"));
                }
            }
        }
        return scheduleThreadPoolExecutor;
    }

    static ThreadPoolExecutor getPriorityExecutor() {
        if (priorityExecutor == null) {
            synchronized (ThreadPoolExecutorFactory.class) {
                if (priorityExecutor == null) {
                    priorityExecutor = new PriorityExecutor(1, new TBThreadFactory("AWCN Dispatcher"));
                    priorityExecutor.allowCoreThreadTimeOut(true);
                }
            }
        }
        return priorityExecutor;
    }
}
