package mtopsdk.mtop.util;

import android.os.Process;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class MtopSDKThreadPoolExecutorFactory {
    private static final int CALLBACK_EXECUTOR_SIZE = 2;
    private static final int DEFAULT_CORE_POOL_SIZE = 3;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int QUEENSIZE = 128;
    private static final int REQUEST_CORE_POOL_SIZE = 4;
    private static final String TAG = "mtopsdk.MtopSDKThreadPoolExecutorFactory";
    private static volatile ExecutorService[] callbackExecutors;
    private static int priority = 10;
    private static volatile ThreadPoolExecutor requestExecutor;
    private static volatile ThreadPoolExecutor threadPoolExecutor;

    private static class MtopSDKThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger();
        int priority = 10;
        private String type = "";

        public MtopSDKThreadFactory(int proiority) {
            this.priority = proiority;
        }

        public MtopSDKThreadFactory(int priority2, String type2) {
            this.priority = priority2;
            this.type = type2;
        }

        public Thread newThread(Runnable r) {
            StringBuilder threadName = new StringBuilder(32);
            threadName.append("MTOPSDK ");
            if (StringUtils.isNotBlank(this.type)) {
                threadName.append(this.type).append(" ");
            } else {
                threadName.append("DefaultPool ");
            }
            threadName.append("Thread:").append(this.mCount.getAndIncrement());
            return new Thread(r, threadName.toString()) {
                public void run() {
                    Process.setThreadPriority(MtopSDKThreadFactory.this.priority);
                    super.run();
                }
            };
        }
    }

    public static ThreadPoolExecutor getDefaultThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            synchronized (MtopSDKThreadPoolExecutorFactory.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = createExecutor(3, 3, 60, 128, new MtopSDKThreadFactory(priority));
                }
            }
        }
        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor getRequestThreadPoolExecutor() {
        if (requestExecutor == null) {
            synchronized (MtopSDKThreadPoolExecutorFactory.class) {
                if (requestExecutor == null) {
                    requestExecutor = createExecutor(4, 4, 60, 0, new MtopSDKThreadFactory(priority, "RequestPool"));
                }
            }
        }
        return requestExecutor;
    }

    public static ExecutorService[] getCallbackExecutorServices() {
        if (callbackExecutors == null) {
            synchronized (MtopSDKThreadPoolExecutorFactory.class) {
                if (callbackExecutors == null) {
                    ExecutorService[] executorServices = new ExecutorService[2];
                    for (int i = 0; i < 2; i++) {
                        executorServices[i] = createExecutor(1, 1, 60, 0, new MtopSDKThreadFactory(priority, "CallbackPool" + i));
                    }
                    callbackExecutors = executorServices;
                }
            }
        }
        return callbackExecutors;
    }

    public static void setDefaultThreadPoolExecutor(ThreadPoolExecutor executor) {
        if (executor != null) {
            threadPoolExecutor = executor;
        }
    }

    public static void setCallbackExecutorServices(ExecutorService[] callbackExecutors2) {
        if (callbackExecutors2 != null && callbackExecutors2.length > 0) {
            callbackExecutors = callbackExecutors2;
        }
    }

    public static void setRequestThreadPoolExecutor(ThreadPoolExecutor executor) {
        if (executor != null) {
            requestExecutor = executor;
        }
    }

    public static Future<?> submit(Runnable runnable) {
        try {
            return getDefaultThreadPoolExecutor().submit(runnable);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[submit]submit runnable to Mtop Default ThreadPool error ---" + e.toString());
            return null;
        }
    }

    public static Future<?> submitCallbackTask(int id, Runnable runnable) {
        try {
            ExecutorService[] callBackExecutors = getCallbackExecutorServices();
            return callBackExecutors[Math.abs(id % callBackExecutors.length)].submit(runnable);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[submitCallbackTask]submit runnable to Mtop Callback ThreadPool error ---" + e.toString());
            return null;
        }
    }

    public static Future<?> submitRequestTask(Runnable runnable) {
        try {
            return getRequestThreadPoolExecutor().submit(runnable);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, "[submitRequestTask]submit runnable to Mtop Request ThreadPool error ---" + e.toString());
            return null;
        }
    }

    public static ThreadPoolExecutor createExecutor(int coreSize, int maxSize, int keepAliveTime, int queenSize, ThreadFactory factory) {
        BlockingQueue<Runnable> queue;
        if (queenSize > 0) {
            queue = new LinkedBlockingQueue<>(queenSize);
        } else {
            queue = new LinkedBlockingQueue<>();
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxSize, (long) keepAliveTime, TimeUnit.SECONDS, queue, factory);
        if (keepAliveTime > 0) {
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }
}
