package com.bumptech.glide.load.engine;

import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ActiveResources {
    @VisibleForTesting
    final Map<Key, ResourceWeakReference> activeEngineResources;
    @Nullable
    private volatile DequeuedResourceCallback cb;
    private final boolean isActiveResourceRetentionAllowed;
    private volatile boolean isShutdown;
    private EngineResource.ResourceListener listener;
    private final Executor monitorClearedResourcesExecutor;
    private final ReferenceQueue<EngineResource<?>> resourceReferenceQueue;

    @VisibleForTesting
    interface DequeuedResourceCallback {
        void onResourceDequeued();
    }

    ActiveResources(boolean isActiveResourceRetentionAllowed2) {
        this(isActiveResourceRetentionAllowed2, Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(@NonNull final Runnable r) {
                return new Thread(new Runnable() {
                    public void run() {
                        Process.setThreadPriority(10);
                        r.run();
                    }
                }, "glide-active-resources");
            }
        }));
    }

    @VisibleForTesting
    ActiveResources(boolean isActiveResourceRetentionAllowed2, Executor monitorClearedResourcesExecutor2) {
        this.activeEngineResources = new HashMap();
        this.resourceReferenceQueue = new ReferenceQueue<>();
        this.isActiveResourceRetentionAllowed = isActiveResourceRetentionAllowed2;
        this.monitorClearedResourcesExecutor = monitorClearedResourcesExecutor2;
        monitorClearedResourcesExecutor2.execute(new Runnable() {
            public void run() {
                ActiveResources.this.cleanReferenceQueue();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setListener(EngineResource.ResourceListener listener2) {
        synchronized (listener2) {
            synchronized (this) {
                this.listener = listener2;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void activate(Key key, EngineResource<?> resource) {
        ResourceWeakReference removed = this.activeEngineResources.put(key, new ResourceWeakReference(key, resource, this.resourceReferenceQueue, this.isActiveResourceRetentionAllowed));
        if (removed != null) {
            removed.reset();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void deactivate(Key key) {
        ResourceWeakReference removed = this.activeEngineResources.remove(key);
        if (removed != null) {
            removed.reset();
        }
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public synchronized EngineResource<?> get(Key key) {
        EngineResource<?> active;
        ResourceWeakReference activeRef = this.activeEngineResources.get(key);
        if (activeRef == null) {
            active = null;
        } else {
            active = (EngineResource) activeRef.get();
            if (active == null) {
                cleanupActiveReference(activeRef);
            }
        }
        return active;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cleanupActiveReference(@android.support.annotation.NonNull com.bumptech.glide.load.engine.ActiveResources.ResourceWeakReference r6) {
        /*
            r5 = this;
            com.bumptech.glide.load.engine.EngineResource$ResourceListener r2 = r5.listener
            monitor-enter(r2)
            monitor-enter(r5)     // Catch:{ all -> 0x0030 }
            java.util.Map<com.bumptech.glide.load.Key, com.bumptech.glide.load.engine.ActiveResources$ResourceWeakReference> r1 = r5.activeEngineResources     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.Key r3 = r6.key     // Catch:{ all -> 0x0033 }
            r1.remove(r3)     // Catch:{ all -> 0x0033 }
            boolean r1 = r6.isCacheable     // Catch:{ all -> 0x0033 }
            if (r1 == 0) goto L_0x0013
            com.bumptech.glide.load.engine.Resource<?> r1 = r6.resource     // Catch:{ all -> 0x0033 }
            if (r1 != 0) goto L_0x0016
        L_0x0013:
            monitor-exit(r5)     // Catch:{ all -> 0x0033 }
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
        L_0x0015:
            return
        L_0x0016:
            com.bumptech.glide.load.engine.EngineResource r0 = new com.bumptech.glide.load.engine.EngineResource     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.engine.Resource<?> r1 = r6.resource     // Catch:{ all -> 0x0033 }
            r3 = 1
            r4 = 0
            r0.<init>(r1, r3, r4)     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.Key r1 = r6.key     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.engine.EngineResource$ResourceListener r3 = r5.listener     // Catch:{ all -> 0x0033 }
            r0.setResourceListener(r1, r3)     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.engine.EngineResource$ResourceListener r1 = r5.listener     // Catch:{ all -> 0x0033 }
            com.bumptech.glide.load.Key r3 = r6.key     // Catch:{ all -> 0x0033 }
            r1.onResourceReleased(r3, r0)     // Catch:{ all -> 0x0033 }
            monitor-exit(r5)     // Catch:{ all -> 0x0033 }
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            goto L_0x0015
        L_0x0030:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0030 }
            throw r1
        L_0x0033:
            r1 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0033 }
            throw r1     // Catch:{ all -> 0x0030 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.ActiveResources.cleanupActiveReference(com.bumptech.glide.load.engine.ActiveResources$ResourceWeakReference):void");
    }

    /* access modifiers changed from: package-private */
    public void cleanReferenceQueue() {
        while (!this.isShutdown) {
            try {
                cleanupActiveReference((ResourceWeakReference) this.resourceReferenceQueue.remove());
                DequeuedResourceCallback current = this.cb;
                if (current != null) {
                    current.onResourceDequeued();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setDequeuedResourceCallback(DequeuedResourceCallback cb2) {
        this.cb = cb2;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void shutdown() {
        this.isShutdown = true;
        if (this.monitorClearedResourcesExecutor instanceof ExecutorService) {
            com.bumptech.glide.util.Executors.shutdownAndAwaitTermination((ExecutorService) this.monitorClearedResourcesExecutor);
        }
    }

    @VisibleForTesting
    static final class ResourceWeakReference extends WeakReference<EngineResource<?>> {
        final boolean isCacheable;
        final Key key;
        @Nullable
        Resource<?> resource;

        ResourceWeakReference(@NonNull Key key2, @NonNull EngineResource<?> referent, @NonNull ReferenceQueue<? super EngineResource<?>> queue, boolean isActiveResourceRetentionAllowed) {
            super(referent, queue);
            this.key = (Key) Preconditions.checkNotNull(key2);
            this.resource = (!referent.isCacheable() || !isActiveResourceRetentionAllowed) ? null : (Resource) Preconditions.checkNotNull(referent.getResource());
            this.isCacheable = referent.isCacheable();
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.resource = null;
            clear();
        }
    }
}
