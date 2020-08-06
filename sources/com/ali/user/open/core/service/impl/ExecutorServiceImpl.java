package com.ali.user.open.core.service.impl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.trace.SDKLogger;
import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ExecutorServiceImpl implements MemberExecutorService {
    private final Handler handler;
    private final HandlerThread handlerThread = new HandlerThread("SDK Looper Thread");
    private ThreadPoolExecutor mExecutor;
    private final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue(128);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ExecutorServiceImpl() {
        this.handlerThread.start();
        synchronized (this.handlerThread) {
            while (this.handlerThread.getLooper() == null) {
                try {
                    this.handlerThread.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        this.handler = new Handler(this.handlerThread.getLooper());
        this.mExecutor = new ThreadPoolExecutor(4, 8, (long) 1, TimeUnit.SECONDS, this.mPoolWorkQueue, new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "ExecutorTask #" + this.mCount.getAndIncrement());
            }
        }, new CoordinatorRejectHandler(this.mPoolWorkQueue));
    }

    public void postHandlerTask(Runnable r) {
        this.handler.post(r);
    }

    public void postUITask(final Runnable r) {
        this.mainHandler.post(new Runnable() {
            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    SDKLogger.e("kernel", t.getMessage(), t);
                }
            }
        });
    }

    public void postTask(Runnable r) {
        this.mExecutor.execute(r);
    }

    public static class CoordinatorRejectHandler implements RejectedExecutionHandler {
        private BlockingQueue<Runnable> mPoolWorkQueue;

        public CoordinatorRejectHandler(BlockingQueue<Runnable> mPoolWorkQueue2) {
            this.mPoolWorkQueue = mPoolWorkQueue2;
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            Object[] array = this.mPoolWorkQueue.toArray();
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (Object a : array) {
                if (a.getClass().isAnonymousClass()) {
                    sb.append(getOuterClass(a));
                    sb.append(',').append(' ');
                } else {
                    sb.append(a.getClass());
                    sb.append(',').append(' ');
                }
            }
            sb.append(']');
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString() + " in " + sb.toString());
        }

        private Object getOuterClass(Object inner) {
            try {
                Field outer = inner.getClass().getDeclaredField("this$0");
                outer.setAccessible(true);
                return outer.get(inner);
            } catch (Exception e1) {
                e1.printStackTrace();
                return inner;
            }
        }
    }

    public Looper getDefaultLooper() {
        return this.handlerThread.getLooper();
    }
}
