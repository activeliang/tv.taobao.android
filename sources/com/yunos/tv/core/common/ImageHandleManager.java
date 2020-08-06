package com.yunos.tv.core.common;

import android.content.Context;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageHandleManager {
    private static ImageHandleManager mImageHandleManager = null;
    private final String TAG = "ImageHandleManager";
    private ThreadPoolExecutor mThreadPoolExecutor = null;

    public static ImageHandleManager getImageHandleManager(Context context) {
        if (mImageHandleManager == null) {
            mImageHandleManager = new ImageHandleManager(context.getApplicationContext());
        }
        return mImageHandleManager;
    }

    public synchronized void purge() {
        if (this.mThreadPoolExecutor != null) {
            this.mThreadPoolExecutor.purge();
        }
    }

    public synchronized void shutdown() {
        if (this.mThreadPoolExecutor != null) {
            this.mThreadPoolExecutor.shutdownNow();
        }
    }

    public synchronized void executeTask(Runnable runnable) {
        if (runnable != null) {
            if (this.mThreadPoolExecutor == null) {
                onInitThreadPoolExecutor();
            }
            this.mThreadPoolExecutor.execute(runnable);
        }
    }

    private ImageHandleManager(Context context) {
        onInitThreadPoolExecutor();
    }

    private synchronized void onInitThreadPoolExecutor() {
        ZpLogger.d("ImageHandleManager", "onInitThreadPoolExecutor ;  mThreadPoolExecutor = " + this.mThreadPoolExecutor);
        if (this.mThreadPoolExecutor == null) {
            this.mThreadPoolExecutor = new ThreadPoolExecutor(0, 2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(), createThreadFactory());
            this.mThreadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
            ZpLogger.i("ImageHandleManager", "onInitThreadPoolExecutor ： run initialize ok");
        }
    }

    private ThreadFactory createThreadFactory() {
        return new ImageHandleManagerThreadFactory();
    }

    private class ImageHandleManagerThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final int threadPriority = 3;

        ImageHandleManagerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "ImageHandleManager: pool-" + this.poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(this.threadPriority);
            return t;
        }
    }
}
