package io.reactivex.internal.schedulers;

import io.reactivex.plugins.RxJavaPlugins;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SchedulerPoolFactory {
    static final Map<ScheduledThreadPoolExecutor, Object> POOLS = new ConcurrentHashMap();
    public static final boolean PURGE_ENABLED;
    static final String PURGE_ENABLED_KEY = "rx2.purge-enabled";
    public static final int PURGE_PERIOD_SECONDS;
    static final String PURGE_PERIOD_SECONDS_KEY = "rx2.purge-period-seconds";
    static final AtomicReference<ScheduledExecutorService> PURGE_THREAD = new AtomicReference<>();

    private SchedulerPoolFactory() {
        throw new IllegalStateException("No instances!");
    }

    static {
        boolean purgeEnable = true;
        int purgePeriod = 1;
        Properties properties = System.getProperties();
        if (properties.containsKey(PURGE_ENABLED_KEY)) {
            purgeEnable = Boolean.getBoolean(PURGE_ENABLED_KEY);
        }
        if (purgeEnable && properties.containsKey(PURGE_PERIOD_SECONDS_KEY)) {
            purgePeriod = Integer.getInteger(PURGE_PERIOD_SECONDS_KEY, 1).intValue();
        }
        PURGE_ENABLED = purgeEnable;
        PURGE_PERIOD_SECONDS = purgePeriod;
        start();
    }

    public static void start() {
        if (PURGE_ENABLED) {
            while (true) {
                ScheduledExecutorService curr = PURGE_THREAD.get();
                if (curr == null || curr.isShutdown()) {
                    ScheduledExecutorService next = Executors.newScheduledThreadPool(1, new RxThreadFactory("RxSchedulerPurge"));
                    if (PURGE_THREAD.compareAndSet(curr, next)) {
                        next.scheduleAtFixedRate(new ScheduledTask(), (long) PURGE_PERIOD_SECONDS, (long) PURGE_PERIOD_SECONDS, TimeUnit.SECONDS);
                        return;
                    }
                    next.shutdownNow();
                } else {
                    return;
                }
            }
        }
    }

    public static void shutdown() {
        ScheduledExecutorService exec = PURGE_THREAD.get();
        if (exec != null) {
            exec.shutdownNow();
        }
        POOLS.clear();
    }

    public static ScheduledExecutorService create(ThreadFactory factory) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, factory);
        if (PURGE_ENABLED && (exec instanceof ScheduledThreadPoolExecutor)) {
            POOLS.put((ScheduledThreadPoolExecutor) exec, exec);
        }
        return exec;
    }

    static final class ScheduledTask implements Runnable {
        ScheduledTask() {
        }

        public void run() {
            try {
                Iterator it = new ArrayList(SchedulerPoolFactory.POOLS.keySet()).iterator();
                while (it.hasNext()) {
                    ScheduledThreadPoolExecutor e = (ScheduledThreadPoolExecutor) it.next();
                    if (e.isShutdown()) {
                        SchedulerPoolFactory.POOLS.remove(e);
                    } else {
                        e.purge();
                    }
                }
            } catch (Throwable e2) {
                RxJavaPlugins.onError(e2);
            }
        }
    }
}
