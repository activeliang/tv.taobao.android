package com.tvlife.imageloader.core;

import android.graphics.Bitmap;
import com.tvlife.imageloader.core.DefaultConfigurationFactory;
import com.tvlife.imageloader.core.imageaware.ImageAware;
import com.tvlife.imageloader.utils.L;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ImageLoaderEngine {
    private final Map<Integer, String> cacheKeysForImageAwares = Collections.synchronizedMap(new HashMap());
    final ImageLoaderConfiguration configuration;
    private final Map<String, ImageAware> loadingUrl = Collections.synchronizedMap(new HashMap());
    private Bitmap mBitmap;
    private final AtomicBoolean networkDenied = new AtomicBoolean(false);
    private final Object pauseLock = new Object();
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicBoolean slowNetwork = new AtomicBoolean(false);
    private Executor taskDistributor;
    /* access modifiers changed from: private */
    public Executor taskExecutor;
    private final Map<String, ReentrantLock> uriLocks = new WeakHashMap();

    ImageLoaderEngine(ImageLoaderConfiguration configuration2) {
        this.configuration = configuration2;
        this.taskExecutor = null;
        this.taskExecutor = createTaskExecutor("Url");
        this.taskDistributor = creatTaskDistributor();
        this.loadingUrl.clear();
    }

    /* access modifiers changed from: package-private */
    public void submit(final LoadAndDisplayImageTask task) throws RejectedExecutionException {
        if (this.taskDistributor == null || ((ExecutorService) this.taskDistributor).isShutdown()) {
            this.taskDistributor = null;
            this.taskDistributor = creatTaskDistributor();
        }
        this.taskDistributor.execute(new Runnable() {
            public void run() {
                ImageLoaderEngine.this.initExecutorsIfNeed();
                if (ImageLoaderEngine.this.taskExecutor != null) {
                    ImageLoaderEngine.this.taskExecutor.execute(task);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void submit(ProcessAndDisplayImageTask task) {
        initExecutorsIfNeed();
    }

    /* access modifiers changed from: private */
    public void initExecutorsIfNeed() {
        if (this.taskExecutor == null || ((ExecutorService) this.taskExecutor).isShutdown()) {
            this.taskExecutor = null;
            this.taskExecutor = createTaskExecutor("Url");
        }
    }

    private Executor createTaskExecutor(String name) {
        return DefaultConfigurationFactory.createExecutor(name, this.configuration.threadPoolSize, this.configuration.threadPriority, this.configuration.tasksProcessingType);
    }

    private Executor creatTaskDistributor() {
        return Executors.newCachedThreadPool(new DefaultConfigurationFactory.CachedThreadPoolThreadFactory("TaskDistributor"));
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUriForView(ImageAware imageAware) {
        return this.cacheKeysForImageAwares.get(Integer.valueOf(imageAware.getId()));
    }

    /* access modifiers changed from: package-private */
    public void prepareDisplayTaskFor(ImageAware imageAware, String memoryCacheKey) {
        this.cacheKeysForImageAwares.put(Integer.valueOf(imageAware.getId()), memoryCacheKey);
        this.loadingUrl.put(memoryCacheKey, imageAware);
    }

    /* access modifiers changed from: package-private */
    public void cancelLoadTaskFor(String memoryCacheKey) {
        this.loadingUrl.remove(memoryCacheKey);
    }

    /* access modifiers changed from: package-private */
    public void cancelLoadAllTaskFor() {
        this.loadingUrl.clear();
        this.cacheKeysForImageAwares.clear();
        L.i("taskExecutor = " + this.taskExecutor + "; taskDistributor = " + this.taskDistributor, new Object[0]);
        if (this.taskExecutor != null && (this.taskExecutor instanceof ThreadPoolExecutor)) {
            ((ThreadPoolExecutor) this.taskExecutor).purge();
        }
        if (this.taskDistributor != null && (this.taskDistributor instanceof ThreadPoolExecutor)) {
            ((ThreadPoolExecutor) this.taskDistributor).purge();
        }
    }

    public boolean loadIfNeed(String memoryCacheKey) {
        return this.loadingUrl.containsKey(memoryCacheKey);
    }

    /* access modifiers changed from: package-private */
    public Bitmap getTestBitmap() {
        return this.mBitmap;
    }

    /* access modifiers changed from: package-private */
    public void setTestBitmap(Bitmap bm) {
        this.mBitmap = bm;
    }

    /* access modifiers changed from: package-private */
    public void cancelDisplayTaskFor(ImageAware imageAware) {
        this.cacheKeysForImageAwares.remove(Integer.valueOf(imageAware.getId()));
        cancelLoadTaskFor(getLoadingUriForView(imageAware));
    }

    /* access modifiers changed from: package-private */
    public void denyNetworkDownloads(boolean denyNetworkDownloads) {
        this.networkDenied.set(denyNetworkDownloads);
    }

    /* access modifiers changed from: package-private */
    public void handleSlowNetwork(boolean handleSlowNetwork) {
        this.slowNetwork.set(handleSlowNetwork);
    }

    /* access modifiers changed from: package-private */
    public void pause() {
        this.paused.set(true);
    }

    /* access modifiers changed from: package-private */
    public void resume() {
        this.paused.set(false);
        synchronized (this.pauseLock) {
            this.pauseLock.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        cancelLoadAllTaskFor();
        if (this.taskExecutor != null) {
            ((ExecutorService) this.taskExecutor).shutdownNow();
        }
        this.cacheKeysForImageAwares.clear();
        this.uriLocks.clear();
        this.loadingUrl.clear();
    }

    /* access modifiers changed from: package-private */
    public void fireCallback(Runnable r) {
    }

    /* access modifiers changed from: package-private */
    public ReentrantLock getLockForUri(String uri) {
        ReentrantLock lock = this.uriLocks.get(uri);
        if (lock != null) {
            return lock;
        }
        ReentrantLock lock2 = new ReentrantLock();
        this.uriLocks.put(uri, lock2);
        return lock2;
    }

    /* access modifiers changed from: package-private */
    public AtomicBoolean getPause() {
        return this.paused;
    }

    /* access modifiers changed from: package-private */
    public Object getPauseLock() {
        return this.pauseLock;
    }

    /* access modifiers changed from: package-private */
    public boolean isNetworkDenied() {
        return this.networkDenied.get();
    }

    /* access modifiers changed from: package-private */
    public boolean isSlowNetwork() {
        return this.slowNetwork.get();
    }
}
