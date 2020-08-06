package com.alibaba.motu.crashreporter.lab;

import android.util.Log;
import com.alibaba.motu.crashreporter.LogUtil;
import com.alibaba.motu.crashreporter.Utils;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FinalizeFake {
    private static final long MAX_FINALIZE_NANOS = 10000000000L;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_SECOND = 1000000000;
    static StackTraceElement[] STACK_TRACE_ELEMENT = new StackTraceElement[0];
    static Class<?> mDaemonsClazz;
    static Object[] mDaemonsInnerClazzInstances = new Object[5];
    static Method[] mDaemonsInnerClazzStartMethods = new Method[5];
    static Method[] mDaemonsInnerClazzStopMethods = new Method[5];
    static Class<?>[] mDaemonsInnerClazzes = new Class[5];
    static Class<?> mFinalizerReferenceClazz;
    static Method mFinalizerReferenceClazz_RemoveMethod;
    static Method mObjectClazz_FinalizeMethod;
    static ReferenceQueue<Object> mQueue;
    static ThreadGroup mSystemThreadGroup;
    static Class<?> mThreadGroupClazz;
    volatile boolean initSuccess = false;

    public FinalizeFake() {
        try {
            initialize();
            this.initSuccess = true;
        } catch (Exception e) {
            LogUtil.e("FinalizeFake initialize", e);
        }
    }

    public void startFakeFinalizerDaemon() {
        if (this.initSuccess) {
            try {
                mDaemonsInnerClazzStopMethods[1].invoke(mDaemonsInnerClazzInstances[1], new Object[0]);
            } catch (Exception e) {
                if (!(e instanceof IllegalStateException) || !"not running".equals(e.getMessage())) {
                    throw e;
                }
            } catch (Exception e2) {
                if (!(e2 instanceof IllegalStateException) || !"not running".equals(e2.getMessage())) {
                    throw e2;
                }
            } catch (Exception e3) {
                resumeFinalizerDaemon();
                return;
            }
            mDaemonsInnerClazzStopMethods[2].invoke(mDaemonsInnerClazzInstances[2], new Object[0]);
            FakeFinalizerDaemon.INSTANCE.start();
            FakeFinalizerWatchdogDaemon.INSTANCE.start();
        }
    }

    public void resumeFinalizerDaemon() {
        if (this.initSuccess) {
            try {
                FakeFinalizerDaemon.INSTANCE.stop();
                FakeFinalizerWatchdogDaemon.INSTANCE.stop();
            } catch (Exception e) {
            }
            try {
                mDaemonsInnerClazzStartMethods[1].invoke(mDaemonsInnerClazzInstances[1], new Object[0]);
            } catch (Exception e2) {
                if (!(e2 instanceof IllegalAccessException) || !"already running".equals(e2.getMessage())) {
                    throw e2;
                }
            } catch (Exception e3) {
                if (!(e3 instanceof IllegalAccessException) || !"already running".equals(e3.getMessage())) {
                    throw e3;
                }
                return;
            } catch (Exception e4) {
                return;
            }
            mDaemonsInnerClazzStartMethods[2].invoke(mDaemonsInnerClazzInstances[2], new Object[0]);
        }
    }

    private void initialize() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException {
        mObjectClazz_FinalizeMethod = Object.class.getDeclaredMethod("finalize", new Class[0]);
        mObjectClazz_FinalizeMethod.setAccessible(true);
        mThreadGroupClazz = Class.forName("java.lang.ThreadGroup");
        try {
            Field systemThreadGroupField = mThreadGroupClazz.getDeclaredField("systemThreadGroup");
            systemThreadGroupField.setAccessible(true);
            mSystemThreadGroup = (ThreadGroup) systemThreadGroupField.get(mThreadGroupClazz);
        } catch (NoSuchFieldException e) {
            Field systemThreadGroupField2 = mThreadGroupClazz.getDeclaredField("mSystem");
            systemThreadGroupField2.setAccessible(true);
            mSystemThreadGroup = (ThreadGroup) systemThreadGroupField2.get(mThreadGroupClazz);
        }
        mFinalizerReferenceClazz = Class.forName("java.lang.ref.FinalizerReference");
        Field queueField = mFinalizerReferenceClazz.getDeclaredField("queue");
        queueField.setAccessible(true);
        mQueue = (ReferenceQueue) queueField.get(mFinalizerReferenceClazz);
        mFinalizerReferenceClazz_RemoveMethod = mFinalizerReferenceClazz.getDeclaredMethod("remove", new Class[]{mFinalizerReferenceClazz});
        String[] clazzNames = {"java.lang.Daemons$ReferenceQueueDaemon", "java.lang.Daemons$FinalizerDaemon", "java.lang.Daemons$FinalizerWatchdogDaemon", "java.lang.Daemons$HeapTrimmerDaemon", "java.lang.Daemons$GCDaemon"};
        mDaemonsClazz = Class.forName("java.lang.Daemons");
        for (Class<?> clazz : mDaemonsClazz.getDeclaredClasses()) {
            int i = 0;
            while (true) {
                if (i < clazzNames.length) {
                    if (clazz != null && clazz.getName().equals(clazzNames[i])) {
                        mDaemonsInnerClazzes[i] = clazz;
                        Field instanceField = clazz.getDeclaredField("INSTANCE");
                        instanceField.setAccessible(true);
                        mDaemonsInnerClazzInstances[i] = instanceField.get(clazz);
                        mDaemonsInnerClazzStartMethods[i] = clazz.getMethod("start", new Class[0]);
                        mDaemonsInnerClazzStopMethods[i] = clazz.getMethod("stop", new Class[0]);
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
        }
    }

    private static class FakeFinalizerDaemon extends FakeDaemon {
        /* access modifiers changed from: private */
        public static final FakeFinalizerDaemon INSTANCE = new FakeFinalizerDaemon();
        /* access modifiers changed from: private */
        public volatile Object finalizingObject;
        /* access modifiers changed from: private */
        public volatile long finalizingStartedNanos;
        private final ReferenceQueue<Object> queue = FinalizeFake.mQueue;

        FakeFinalizerDaemon() {
            super("FakeFinalizerDaemon");
        }

        public void run() {
            while (isRunning()) {
                try {
                    doFinalize(this.queue.remove());
                } catch (InterruptedException e) {
                }
            }
        }

        private void doFinalize(Reference<?> reference) {
            try {
                FinalizeFake.mFinalizerReferenceClazz_RemoveMethod.invoke(FinalizeFake.mFinalizerReferenceClazz, new Object[]{reference});
                Object object = reference.get();
                reference.clear();
                try {
                    this.finalizingStartedNanos = System.nanoTime();
                    this.finalizingObject = object;
                    synchronized (FakeFinalizerWatchdogDaemon.INSTANCE) {
                        FakeFinalizerWatchdogDaemon.INSTANCE.notify();
                    }
                    FinalizeFake.mObjectClazz_FinalizeMethod.invoke(object, new Object[0]);
                    this.finalizingObject = null;
                } catch (Throwable ex) {
                    Log.e("GCMagic", "Uncaught exception thrown by (" + object + ") finalizer", ex);
                    this.finalizingObject = null;
                }
            } catch (Throwable ex2) {
                Log.e("GCMagic", "FinalizerReference remove exception by finalizer", ex2);
            }
        }
    }

    private static abstract class FakeDaemon implements Runnable {
        private String name;
        private Thread thread;

        public abstract void run();

        protected FakeDaemon(String name2) {
            this.name = name2;
        }

        public synchronized void start() {
            if (this.thread != null) {
                throw new IllegalStateException("already running");
            }
            this.thread = new Thread(FinalizeFake.mSystemThreadGroup, this, this.name);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        /* access modifiers changed from: protected */
        public synchronized boolean isRunning() {
            return this.thread != null;
        }

        public synchronized void interrupt() {
            interrupt(this.thread);
        }

        public synchronized void interrupt(Thread thread2) {
            if (thread2 == null) {
                throw new IllegalStateException("not running");
            }
            thread2.interrupt();
        }

        public void stop() {
            Thread threadToStop;
            synchronized (this) {
                threadToStop = this.thread;
                this.thread = null;
            }
            if (threadToStop == null) {
                throw new IllegalStateException("not running");
            }
            interrupt(threadToStop);
            while (true) {
                try {
                    threadToStop.join();
                    break;
                } catch (InterruptedException e) {
                }
            }
        }

        public synchronized StackTraceElement[] getStackTrace() {
            return this.thread != null ? this.thread.getStackTrace() : FinalizeFake.STACK_TRACE_ELEMENT;
        }
    }

    private static class FakeFinalizerWatchdogDaemon extends FakeDaemon {
        /* access modifiers changed from: private */
        public static final FakeFinalizerWatchdogDaemon INSTANCE = new FakeFinalizerWatchdogDaemon();

        FakeFinalizerWatchdogDaemon() {
            super("FakeFinalizerWatchdogDaemon");
        }

        public void run() {
            while (isRunning()) {
                if (waitForObject() && !waitForFinalization() && !isDebuggerActive() && FakeFinalizerDaemon.INSTANCE.finalizingObject != null) {
                    FakeFinalizerDaemon.INSTANCE.interrupt();
                }
            }
        }

        private boolean isDebuggerActive() {
            return Utils.VMRuntimeUtils.isDebuggerActive();
        }

        private boolean waitForObject() {
            while (FakeFinalizerDaemon.INSTANCE.finalizingObject == null) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        return false;
                    }
                }
            }
            return true;
        }

        private void sleepFor(long startNanos, long durationNanos) {
            while (true) {
                long sleepMills = (durationNanos - (System.nanoTime() - startNanos)) / 1000000;
                if (sleepMills > 0) {
                    try {
                        Thread.sleep(sleepMills);
                    } catch (InterruptedException e) {
                        if (!isRunning()) {
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
        }

        private boolean waitForFinalization() {
            long startTime = FakeFinalizerDaemon.INSTANCE.finalizingStartedNanos;
            sleepFor(startTime, FinalizeFake.MAX_FINALIZE_NANOS);
            return FakeFinalizerDaemon.INSTANCE.finalizingObject == null || FakeFinalizerDaemon.INSTANCE.finalizingStartedNanos != startTime;
        }
    }
}
