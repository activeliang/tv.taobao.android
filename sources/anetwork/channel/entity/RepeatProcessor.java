package anetwork.channel.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class RepeatProcessor {
    private static final int THREAD_POOL_SIZE = 2;
    /* access modifiers changed from: private */
    public static AtomicInteger id = new AtomicInteger(0);
    private static final ExecutorService[] threadPool = new ExecutorService[2];

    static {
        for (int i = 0; i < 2; i++) {
            threadPool[i] = Executors.newSingleThreadExecutor(new ThreadFactory() {
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, String.format("RepeaterThread:%d", new Object[]{Integer.valueOf(RepeatProcessor.id.getAndIncrement())}));
                }
            });
        }
    }

    public static void submitTask(int id2, Runnable task) {
        threadPool[Math.abs(id2 % 2)].submit(task);
    }
}
