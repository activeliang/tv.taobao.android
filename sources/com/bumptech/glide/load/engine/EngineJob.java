package com.bumptech.glide.load.engine;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
    private static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    private final GlideExecutor animationExecutor;
    final ResourceCallbacksAndExecutors cbs;
    DataSource dataSource;
    private DecodeJob<R> decodeJob;
    private final GlideExecutor diskCacheExecutor;
    EngineResource<?> engineResource;
    private final EngineResourceFactory engineResourceFactory;
    GlideException exception;
    private boolean hasLoadFailed;
    private boolean hasResource;
    private boolean isCacheable;
    private volatile boolean isCancelled;
    private Key key;
    private final EngineJobListener listener;
    private boolean onlyRetrieveFromCache;
    private final AtomicInteger pendingCallbacks;
    private final Pools.Pool<EngineJob<?>> pool;
    private Resource<?> resource;
    private final GlideExecutor sourceExecutor;
    private final GlideExecutor sourceUnlimitedExecutor;
    private final StateVerifier stateVerifier;
    private boolean useAnimationPool;
    private boolean useUnlimitedSourceGeneratorPool;

    EngineJob(GlideExecutor diskCacheExecutor2, GlideExecutor sourceExecutor2, GlideExecutor sourceUnlimitedExecutor2, GlideExecutor animationExecutor2, EngineJobListener listener2, Pools.Pool<EngineJob<?>> pool2) {
        this(diskCacheExecutor2, sourceExecutor2, sourceUnlimitedExecutor2, animationExecutor2, listener2, pool2, DEFAULT_FACTORY);
    }

    @VisibleForTesting
    EngineJob(GlideExecutor diskCacheExecutor2, GlideExecutor sourceExecutor2, GlideExecutor sourceUnlimitedExecutor2, GlideExecutor animationExecutor2, EngineJobListener listener2, Pools.Pool<EngineJob<?>> pool2, EngineResourceFactory engineResourceFactory2) {
        this.cbs = new ResourceCallbacksAndExecutors();
        this.stateVerifier = StateVerifier.newInstance();
        this.pendingCallbacks = new AtomicInteger();
        this.diskCacheExecutor = diskCacheExecutor2;
        this.sourceExecutor = sourceExecutor2;
        this.sourceUnlimitedExecutor = sourceUnlimitedExecutor2;
        this.animationExecutor = animationExecutor2;
        this.listener = listener2;
        this.pool = pool2;
        this.engineResourceFactory = engineResourceFactory2;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public synchronized EngineJob<R> init(Key key2, boolean isCacheable2, boolean useUnlimitedSourceGeneratorPool2, boolean useAnimationPool2, boolean onlyRetrieveFromCache2) {
        this.key = key2;
        this.isCacheable = isCacheable2;
        this.useUnlimitedSourceGeneratorPool = useUnlimitedSourceGeneratorPool2;
        this.useAnimationPool = useAnimationPool2;
        this.onlyRetrieveFromCache = onlyRetrieveFromCache2;
        return this;
    }

    public synchronized void start(DecodeJob<R> decodeJob2) {
        GlideExecutor executor;
        this.decodeJob = decodeJob2;
        if (decodeJob2.willDecodeFromCache()) {
            executor = this.diskCacheExecutor;
        } else {
            executor = getActiveSourceExecutor();
        }
        executor.execute(decodeJob2);
    }

    /* access modifiers changed from: package-private */
    public synchronized void addCallback(ResourceCallback cb, Executor callbackExecutor) {
        boolean z = true;
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            this.cbs.add(cb, callbackExecutor);
            if (this.hasResource) {
                incrementPendingCallbacks(1);
                callbackExecutor.execute(new CallResourceReady(cb));
            } else if (this.hasLoadFailed) {
                incrementPendingCallbacks(1);
                callbackExecutor.execute(new CallLoadFailed(cb));
            } else {
                if (this.isCancelled) {
                    z = false;
                }
                Preconditions.checkArgument(z, "Cannot add callbacks to a cancelled EngineJob");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void callCallbackOnResourceReady(ResourceCallback cb) {
        try {
            cb.onResourceReady(this.engineResource, this.dataSource);
        } catch (Throwable t) {
            throw new CallbackException(t);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void callCallbackOnLoadFailed(ResourceCallback cb) {
        try {
            cb.onLoadFailed(this.exception);
        } catch (Throwable t) {
            throw new CallbackException(t);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void removeCallback(ResourceCallback cb) {
        this.stateVerifier.throwIfRecycled();
        this.cbs.remove(cb);
        if (this.cbs.isEmpty()) {
            cancel();
            if ((this.hasResource || this.hasLoadFailed) && this.pendingCallbacks.get() == 0) {
                release();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean onlyRetrieveFromCache() {
        return this.onlyRetrieveFromCache;
    }

    private GlideExecutor getActiveSourceExecutor() {
        if (this.useUnlimitedSourceGeneratorPool) {
            return this.sourceUnlimitedExecutor;
        }
        return this.useAnimationPool ? this.animationExecutor : this.sourceExecutor;
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        if (!isDone()) {
            this.isCancelled = true;
            this.decodeJob.cancel();
            this.listener.onEngineJobCancelled(this, this.key);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean isCancelled() {
        return this.isCancelled;
    }

    private boolean isDone() {
        return this.hasLoadFailed || this.hasResource || this.isCancelled;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0058, code lost:
        r8.listener.onEngineJobComplete(r8, r2, r3);
        r4 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0065, code lost:
        if (r4.hasNext() == false) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        r1 = r4.next();
        r1.executor.execute(new com.bumptech.glide.load.engine.EngineJob.CallResourceReady(r8, r1.cb));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007a, code lost:
        decrementPendingCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyCallbacksOfResult() {
        /*
            r8 = this;
            monitor-enter(r8)
            com.bumptech.glide.util.pool.StateVerifier r4 = r8.stateVerifier     // Catch:{ all -> 0x0025 }
            r4.throwIfRecycled()     // Catch:{ all -> 0x0025 }
            boolean r4 = r8.isCancelled     // Catch:{ all -> 0x0025 }
            if (r4 == 0) goto L_0x0014
            com.bumptech.glide.load.engine.Resource<?> r4 = r8.resource     // Catch:{ all -> 0x0025 }
            r4.recycle()     // Catch:{ all -> 0x0025 }
            r8.release()     // Catch:{ all -> 0x0025 }
            monitor-exit(r8)     // Catch:{ all -> 0x0025 }
        L_0x0013:
            return
        L_0x0014:
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r4 = r8.cbs     // Catch:{ all -> 0x0025 }
            boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x0025 }
            if (r4 == 0) goto L_0x0028
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0025 }
            java.lang.String r5 = "Received a resource without any callbacks to notify"
            r4.<init>(r5)     // Catch:{ all -> 0x0025 }
            throw r4     // Catch:{ all -> 0x0025 }
        L_0x0025:
            r4 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0025 }
            throw r4
        L_0x0028:
            boolean r4 = r8.hasResource     // Catch:{ all -> 0x0025 }
            if (r4 == 0) goto L_0x0035
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0025 }
            java.lang.String r5 = "Already have resource"
            r4.<init>(r5)     // Catch:{ all -> 0x0025 }
            throw r4     // Catch:{ all -> 0x0025 }
        L_0x0035:
            com.bumptech.glide.load.engine.EngineJob$EngineResourceFactory r4 = r8.engineResourceFactory     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.Resource<?> r5 = r8.resource     // Catch:{ all -> 0x0025 }
            boolean r6 = r8.isCacheable     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.EngineResource r4 = r4.build(r5, r6)     // Catch:{ all -> 0x0025 }
            r8.engineResource = r4     // Catch:{ all -> 0x0025 }
            r4 = 1
            r8.hasResource = r4     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r4 = r8.cbs     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r0 = r4.copy()     // Catch:{ all -> 0x0025 }
            int r4 = r0.size()     // Catch:{ all -> 0x0025 }
            int r4 = r4 + 1
            r8.incrementPendingCallbacks(r4)     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.Key r2 = r8.key     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.EngineResource<?> r3 = r8.engineResource     // Catch:{ all -> 0x0025 }
            monitor-exit(r8)     // Catch:{ all -> 0x0025 }
            com.bumptech.glide.load.engine.EngineJobListener r4 = r8.listener
            r4.onEngineJobComplete(r8, r2, r3)
            java.util.Iterator r4 = r0.iterator()
        L_0x0061:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x007a
            java.lang.Object r1 = r4.next()
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbackAndExecutor r1 = (com.bumptech.glide.load.engine.EngineJob.ResourceCallbackAndExecutor) r1
            java.util.concurrent.Executor r5 = r1.executor
            com.bumptech.glide.load.engine.EngineJob$CallResourceReady r6 = new com.bumptech.glide.load.engine.EngineJob$CallResourceReady
            com.bumptech.glide.request.ResourceCallback r7 = r1.cb
            r6.<init>(r7)
            r5.execute(r6)
            goto L_0x0061
        L_0x007a:
            r8.decrementPendingCallbacks()
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.EngineJob.notifyCallbacksOfResult():void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void incrementPendingCallbacks(int count) {
        Preconditions.checkArgument(isDone(), "Not yet complete!");
        if (this.pendingCallbacks.getAndAdd(count) == 0 && this.engineResource != null) {
            this.engineResource.acquire();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void decrementPendingCallbacks() {
        this.stateVerifier.throwIfRecycled();
        Preconditions.checkArgument(isDone(), "Not yet complete!");
        int decremented = this.pendingCallbacks.decrementAndGet();
        Preconditions.checkArgument(decremented >= 0, "Can't decrement below 0");
        if (decremented == 0) {
            if (this.engineResource != null) {
                this.engineResource.release();
            }
            release();
        }
    }

    private synchronized void release() {
        if (this.key == null) {
            throw new IllegalArgumentException();
        }
        this.cbs.clear();
        this.key = null;
        this.engineResource = null;
        this.resource = null;
        this.hasLoadFailed = false;
        this.isCancelled = false;
        this.hasResource = false;
        this.decodeJob.release(false);
        this.decodeJob = null;
        this.exception = null;
        this.dataSource = null;
        this.pool.release(this);
    }

    public void onResourceReady(Resource<R> resource2, DataSource dataSource2) {
        synchronized (this) {
            this.resource = resource2;
            this.dataSource = dataSource2;
        }
        notifyCallbacksOfResult();
    }

    public void onLoadFailed(GlideException e) {
        synchronized (this) {
            this.exception = e;
        }
        notifyCallbacksOfException();
    }

    public void reschedule(DecodeJob<?> job) {
        getActiveSourceExecutor().execute(job);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0045, code lost:
        r7.listener.onEngineJobComplete(r7, r2, (com.bumptech.glide.load.engine.EngineResource<?>) null);
        r3 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0053, code lost:
        if (r3.hasNext() == false) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0055, code lost:
        r1 = r3.next();
        r1.executor.execute(new com.bumptech.glide.load.engine.EngineJob.CallLoadFailed(r7, r1.cb));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0068, code lost:
        decrementPendingCallbacks();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyCallbacksOfException() {
        /*
            r7 = this;
            monitor-enter(r7)
            com.bumptech.glide.util.pool.StateVerifier r3 = r7.stateVerifier     // Catch:{ all -> 0x0020 }
            r3.throwIfRecycled()     // Catch:{ all -> 0x0020 }
            boolean r3 = r7.isCancelled     // Catch:{ all -> 0x0020 }
            if (r3 == 0) goto L_0x000f
            r7.release()     // Catch:{ all -> 0x0020 }
            monitor-exit(r7)     // Catch:{ all -> 0x0020 }
        L_0x000e:
            return
        L_0x000f:
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r3 = r7.cbs     // Catch:{ all -> 0x0020 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0020 }
            if (r3 == 0) goto L_0x0023
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0020 }
            java.lang.String r4 = "Received an exception without any callbacks to notify"
            r3.<init>(r4)     // Catch:{ all -> 0x0020 }
            throw r3     // Catch:{ all -> 0x0020 }
        L_0x0020:
            r3 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0020 }
            throw r3
        L_0x0023:
            boolean r3 = r7.hasLoadFailed     // Catch:{ all -> 0x0020 }
            if (r3 == 0) goto L_0x0030
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0020 }
            java.lang.String r4 = "Already failed once"
            r3.<init>(r4)     // Catch:{ all -> 0x0020 }
            throw r3     // Catch:{ all -> 0x0020 }
        L_0x0030:
            r3 = 1
            r7.hasLoadFailed = r3     // Catch:{ all -> 0x0020 }
            com.bumptech.glide.load.Key r2 = r7.key     // Catch:{ all -> 0x0020 }
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r3 = r7.cbs     // Catch:{ all -> 0x0020 }
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbacksAndExecutors r0 = r3.copy()     // Catch:{ all -> 0x0020 }
            int r3 = r0.size()     // Catch:{ all -> 0x0020 }
            int r3 = r3 + 1
            r7.incrementPendingCallbacks(r3)     // Catch:{ all -> 0x0020 }
            monitor-exit(r7)     // Catch:{ all -> 0x0020 }
            com.bumptech.glide.load.engine.EngineJobListener r3 = r7.listener
            r4 = 0
            r3.onEngineJobComplete(r7, r2, r4)
            java.util.Iterator r3 = r0.iterator()
        L_0x004f:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0068
            java.lang.Object r1 = r3.next()
            com.bumptech.glide.load.engine.EngineJob$ResourceCallbackAndExecutor r1 = (com.bumptech.glide.load.engine.EngineJob.ResourceCallbackAndExecutor) r1
            java.util.concurrent.Executor r4 = r1.executor
            com.bumptech.glide.load.engine.EngineJob$CallLoadFailed r5 = new com.bumptech.glide.load.engine.EngineJob$CallLoadFailed
            com.bumptech.glide.request.ResourceCallback r6 = r1.cb
            r5.<init>(r6)
            r4.execute(r5)
            goto L_0x004f
        L_0x0068:
            r7.decrementPendingCallbacks()
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.EngineJob.notifyCallbacksOfException():void");
    }

    @NonNull
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    private class CallLoadFailed implements Runnable {
        private final ResourceCallback cb;

        CallLoadFailed(ResourceCallback cb2) {
            this.cb = cb2;
        }

        public void run() {
            synchronized (EngineJob.this) {
                if (EngineJob.this.cbs.contains(this.cb)) {
                    EngineJob.this.callCallbackOnLoadFailed(this.cb);
                }
                EngineJob.this.decrementPendingCallbacks();
            }
        }
    }

    private class CallResourceReady implements Runnable {
        private final ResourceCallback cb;

        CallResourceReady(ResourceCallback cb2) {
            this.cb = cb2;
        }

        public void run() {
            synchronized (EngineJob.this) {
                if (EngineJob.this.cbs.contains(this.cb)) {
                    EngineJob.this.engineResource.acquire();
                    EngineJob.this.callCallbackOnResourceReady(this.cb);
                    EngineJob.this.removeCallback(this.cb);
                }
                EngineJob.this.decrementPendingCallbacks();
            }
        }
    }

    static final class ResourceCallbacksAndExecutors implements Iterable<ResourceCallbackAndExecutor> {
        private final List<ResourceCallbackAndExecutor> callbacksAndExecutors;

        ResourceCallbacksAndExecutors() {
            this(new ArrayList(2));
        }

        ResourceCallbacksAndExecutors(List<ResourceCallbackAndExecutor> callbacksAndExecutors2) {
            this.callbacksAndExecutors = callbacksAndExecutors2;
        }

        /* access modifiers changed from: package-private */
        public void add(ResourceCallback cb, Executor executor) {
            this.callbacksAndExecutors.add(new ResourceCallbackAndExecutor(cb, executor));
        }

        /* access modifiers changed from: package-private */
        public void remove(ResourceCallback cb) {
            this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(cb));
        }

        /* access modifiers changed from: package-private */
        public boolean contains(ResourceCallback cb) {
            return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(cb));
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.callbacksAndExecutors.isEmpty();
        }

        /* access modifiers changed from: package-private */
        public int size() {
            return this.callbacksAndExecutors.size();
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.callbacksAndExecutors.clear();
        }

        /* access modifiers changed from: package-private */
        public ResourceCallbacksAndExecutors copy() {
            return new ResourceCallbacksAndExecutors(new ArrayList(this.callbacksAndExecutors));
        }

        private static ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback cb) {
            return new ResourceCallbackAndExecutor(cb, Executors.directExecutor());
        }

        @NonNull
        public Iterator<ResourceCallbackAndExecutor> iterator() {
            return this.callbacksAndExecutors.iterator();
        }
    }

    static final class ResourceCallbackAndExecutor {
        final ResourceCallback cb;
        final Executor executor;

        ResourceCallbackAndExecutor(ResourceCallback cb2, Executor executor2) {
            this.cb = cb2;
            this.executor = executor2;
        }

        public boolean equals(Object o) {
            if (o instanceof ResourceCallbackAndExecutor) {
                return this.cb.equals(((ResourceCallbackAndExecutor) o).cb);
            }
            return false;
        }

        public int hashCode() {
            return this.cb.hashCode();
        }
    }

    @VisibleForTesting
    static class EngineResourceFactory {
        EngineResourceFactory() {
        }

        public <R> EngineResource<R> build(Resource<R> resource, boolean isMemoryCacheable) {
            return new EngineResource<>(resource, isMemoryCacheable, true);
        }
    }
}
