package android.taobao.windvane.thread;

import android.taobao.windvane.util.TaoLog;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WVThreadPool {
    private static final int CORE_POOL_SIZE = (CPU_COUNT + 1);
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE = 500;
    private static final int MAX_POOL_SIZE = ((CPU_COUNT * 2) + 1);
    private static String TAG = "WVThreadPool";
    private static WVThreadPool threadManager;
    private Executor executor = null;

    public void setClientExecutor(Executor executor2) {
        if (this.executor == null && executor2 != null) {
            TAG += "tb";
            this.executor = executor2;
        }
    }

    public static WVThreadPool getInstance() {
        if (threadManager == null) {
            synchronized (WVThreadPool.class) {
                if (threadManager == null) {
                    threadManager = new WVThreadPool();
                }
            }
        }
        return threadManager;
    }

    public void execute(Runnable command) {
        if (this.executor == null) {
            this.executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        }
        if (command == null) {
            TaoLog.w(TAG, "execute task is null.");
        } else {
            this.executor.execute(command);
        }
    }
}
