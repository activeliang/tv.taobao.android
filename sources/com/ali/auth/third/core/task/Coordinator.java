package com.ali.auth.third.core.task;

import android.annotation.TargetApi;
import android.os.Looper;
import android.os.Process;
import android.taobao.windvane.cache.WVFileInfo;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Coordinator {
    private static final int CORE_POOL_SIZE = 8;
    private static final int KEEP_ALIVE = 10;
    private static final int MAXIMUM_POOL_SIZE = 128;
    public static final int QUEUE_PRIORITY_NORMAL = 30;
    protected static final String TAG = "Coordinator";
    protected static final BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue(100, new PriorityComparator());
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "login#" + r.getClass().getName());
        }
    };
    static CoordThreadPoolExecutor sThreadPoolExecutor = new CoordThreadPoolExecutor(8, 128, 10, TimeUnit.SECONDS, mPoolWorkQueue, sThreadFactory, new CoordinatorRejectHandler());

    public interface PriorityQueue {
        int getQueuePriority();
    }

    static class PriorityComparator<Runnable> implements Comparator<Runnable> {
        public int compare(Runnable c1, Runnable c2) {
            if ((c1 instanceof StandaloneTask) && (c2 instanceof StandaloneTask)) {
                StandaloneTask s1 = (StandaloneTask) c1;
                StandaloneTask s2 = (StandaloneTask) c2;
                if (s1.getQueuePriority() > s2.getQueuePriority()) {
                    return 1;
                }
                if (s1.getQueuePriority() < s2.getQueuePriority()) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public static void execute(Runnable runnable) {
        sThreadPoolExecutor.execute(runnable, 30);
    }

    public static void execute(Runnable runnable, int queuePriority) {
        sThreadPoolExecutor.execute(runnable, queuePriority);
    }

    protected static void runWithTiming(Runnable runnable) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            int priority = 10;
            if (10 < 1) {
                priority = 1;
            }
            Process.setThreadPriority(priority);
        }
        try {
            runnable.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static class CoordinatorRejectHandler implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            Object[] array = Coordinator.mPoolWorkQueue.toArray();
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (Object a : array) {
                if (a.getClass().isAnonymousClass()) {
                    sb.append(Coordinator.getOuterClass(a));
                    sb.append(WVFileInfo.DIVISION).append(' ');
                } else {
                    sb.append(a);
                    sb.append('>').append(' ');
                }
            }
            sb.append(']');
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString() + " in " + sb.toString());
        }
    }

    protected static Object getOuterClass(Object inner) {
        try {
            Field outer = inner.getClass().getDeclaredField("this$0");
            outer.setAccessible(true);
            return outer.get(inner);
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
            return inner;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return inner;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return inner;
        }
    }

    public static ThreadPoolExecutor getDefaultThreadPoolExecutor() {
        return sThreadPoolExecutor;
    }

    public static class CoordThreadPoolExecutor extends ThreadPoolExecutor {
        public CoordThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
            allowCoreThreadTimeOut(true);
        }

        public void execute(Runnable command, int queuePriority) {
            if (command instanceof StandaloneTask) {
                super.execute(command);
                return;
            }
            StandaloneTask s1 = new StandaloneTask(command);
            if (queuePriority < 1) {
                queuePriority = 1;
            }
            s1.mPriorityQueue = queuePriority;
            super.execute(s1);
        }

        /* access modifiers changed from: protected */
        public void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            if (r instanceof StandaloneTask) {
                t.setName(((StandaloneTask) r).mRunnable + "");
            } else {
                t.setName(r + "");
            }
        }

        /* access modifiers changed from: protected */
        @TargetApi(11)
        public void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
        }
    }

    static class StandaloneTask implements Runnable, PriorityQueue {
        int mPriorityQueue = 30;
        final Runnable mRunnable;

        public StandaloneTask(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public int getQueuePriority() {
            if (this.mRunnable instanceof PriorityQueue) {
                return ((PriorityQueue) this.mRunnable).getQueuePriority();
            }
            return this.mPriorityQueue;
        }

        public void run() {
            Coordinator.runWithTiming(this.mRunnable);
        }
    }
}
