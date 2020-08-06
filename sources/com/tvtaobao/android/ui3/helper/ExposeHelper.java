package com.tvtaobao.android.ui3.helper;

import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExposeHelper {
    private static ExposeHelper instance = null;
    /* access modifiers changed from: private */
    public HandlerThread exposeCheckThread;
    /* access modifiers changed from: private */
    public Handler exposeCheckThreadHandler;
    private AtomicBoolean isReleaseDoing;
    /* access modifiers changed from: private */
    public HashMap<String, WeakReference<ExposeCheckTaskImpl>> taskCache;

    public interface ExposeCheckListener {
        boolean onExpose(View view, Rect rect);
    }

    public static ExposeHelper getInstance() {
        if (instance == null) {
            synchronized (ExposeHelper.class) {
                if (instance == null) {
                    instance = new ExposeHelper();
                }
            }
        }
        return instance;
    }

    public static void release() {
        if (instance != null) {
            synchronized (ExposeHelper.class) {
                if (instance != null) {
                    instance.doRelease();
                    instance = null;
                }
            }
        }
    }

    private ExposeHelper() {
        this.isReleaseDoing = new AtomicBoolean(false);
        this.exposeCheckThreadHandler = null;
        this.exposeCheckThread = null;
        this.taskCache = null;
        this.exposeCheckThread = new HandlerThread("ExposeHelper_CheckThread_" + System.currentTimeMillis());
        this.exposeCheckThread.start();
        this.exposeCheckThreadHandler = new Handler(this.exposeCheckThread.getLooper());
        this.taskCache = new HashMap<>();
    }

    public void checkExpose(View view, ExposeCheckListener listener) {
        checkExpose(view, listener, 60);
    }

    public void checkExpose(View view, ExposeCheckListener listener, long interval) {
        if (!this.isReleaseDoing.get()) {
            cancelCheck(view);
            if (this.exposeCheckThreadHandler != null) {
                final ExposeCheckListener exposeCheckListener = listener;
                final View view2 = view;
                final long j = interval;
                this.exposeCheckThreadHandler.post(new Runnable() {
                    public void run() {
                        if (!(exposeCheckListener == null || view2 == null)) {
                            ExposeCheckTaskImpl task = new ExposeCheckTaskImpl(view2, exposeCheckListener, j);
                            ExposeHelper.this.exposeCheckThreadHandler.post(task);
                            ExposeHelper.this.taskCache.put(view2.toString(), new WeakReference(task));
                        }
                        ExposeHelper.this.doTaskCacheManage();
                    }
                });
            }
        }
    }

    public void cancelCheck(final View view) {
        if (!this.isReleaseDoing.get() && this.exposeCheckThreadHandler != null) {
            this.exposeCheckThreadHandler.post(new Runnable() {
                public void run() {
                    if (view != null) {
                        Object tmp = ExposeHelper.this.taskCache.remove(view.toString());
                        if ((tmp instanceof WeakReference) && (((WeakReference) tmp).get() instanceof ExposeCheckTaskImpl)) {
                            ExposeHelper.this.exposeCheckThreadHandler.removeCallbacks((ExposeCheckTaskImpl) ((WeakReference) tmp).get());
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void doTaskCacheManage() {
        if (!this.isReleaseDoing.get() && this.exposeCheckThreadHandler != null) {
            this.exposeCheckThreadHandler.post(new Runnable() {
                public void run() {
                    try {
                        for (Map.Entry<String, WeakReference<ExposeCheckTaskImpl>> entry : ExposeHelper.this.taskCache.entrySet()) {
                            if (entry.getValue() == null || entry.getValue().get() == null) {
                                ExposeHelper.this.taskCache.remove(entry.getKey());
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void doRelease() {
        if (!this.isReleaseDoing.getAndSet(true) && this.exposeCheckThreadHandler != null) {
            this.exposeCheckThreadHandler.post(new Runnable() {
                public void run() {
                    ExposeHelper.this.exposeCheckThreadHandler.removeCallbacks((Runnable) null);
                    ExposeHelper.this.exposeCheckThread.quit();
                    ExposeHelper.this.taskCache.clear();
                    Handler unused = ExposeHelper.this.exposeCheckThreadHandler = null;
                    HandlerThread unused2 = ExposeHelper.this.exposeCheckThread = null;
                    HashMap unused3 = ExposeHelper.this.taskCache = null;
                }
            });
        }
    }

    private class ExposeCheckTaskImpl implements Runnable {
        private long checkInterval = 60;
        private int emptyTagIndex = -15136736;
        private Rect viewRect = new Rect();
        private WeakReference<View> viewWeakReference;

        public ExposeCheckTaskImpl(View view, ExposeCheckListener exposeCheckListener, long checkInterval2) {
            this.checkInterval = checkInterval2;
            if (view != null) {
                view.setTag(this.emptyTagIndex, exposeCheckListener);
                this.viewWeakReference = new WeakReference<>(view);
            }
        }

        public void run() {
            try {
                if (this.viewWeakReference != null) {
                    View viewInstance = (View) this.viewWeakReference.get();
                    if ((viewInstance instanceof View) && viewInstance != null) {
                        Object listener = viewInstance.getTag(this.emptyTagIndex);
                        if (listener instanceof ExposeCheckListener) {
                            boolean exposeRtn = false;
                            if (viewInstance.getGlobalVisibleRect(this.viewRect)) {
                                exposeRtn = ((ExposeCheckListener) listener).onExpose(viewInstance, this.viewRect);
                            }
                            if (!exposeRtn) {
                                ExposeHelper.this.exposeCheckThreadHandler.postDelayed(this, this.checkInterval);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
